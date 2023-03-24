package com.huajframe.choosecourse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajframe.choosecourse.entity.Classes;
import com.huajframe.choosecourse.entity.Course;
import com.huajframe.choosecourse.entity.Record;
import com.huajframe.choosecourse.entity.Student;
import com.huajframe.choosecourse.enums.RecordStatusEnums;
import com.huajframe.choosecourse.enums.RespBeanEnum;
import com.huajframe.choosecourse.limit.annotation.AccessLimit;
import com.huajframe.choosecourse.rabbitmq.MqSender;
import com.huajframe.choosecourse.service.IClassesService;
import com.huajframe.choosecourse.service.ICourseService;
import com.huajframe.choosecourse.service.IRecordService;
import com.huajframe.choosecourse.util.JsonUtil;
import com.huajframe.choosecourse.vo.ChooseCourseMessage;
import com.huajframe.choosecourse.vo.ClassesVo;
import com.huajframe.choosecourse.vo.RecordDetailVo;
import com.huajframe.choosecourse.vo.RespBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hua JFrame
 * @since 2023-03-22
 */
@Controller
@RequestMapping("/record")
public class RecordController implements InitializingBean {

    @Autowired
    private IRecordService recordService;
    @Autowired
    private IClassesService classesService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    // @Autowired
    // private RedisScript<Long> redisScript;
    @Autowired
    private MqSender mqSender;
    @Autowired
    private ICourseService courseService;


    /**
     * 执行选课
     *
     * @param student cookie中的学生信息
     * @param classesId 课程id
     * @param path 请求路径
     * @return 订单详细页面
     */
    @PostMapping("/{path}/doChoose")
    @ResponseBody
    public RespBean doSeckill(Student student,
                              Long classesId,
                              @PathVariable String path) {

        //判断用户的请求地址是否正确
        boolean check = recordService.checkPath(student, classesId, path);
        if (!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }

        //判断是否重复选课----直接从redis中获取选课
        Record record = (Record) redisTemplate.opsForValue().get("record:" + classesId + ":" + student.getStudentNumber());
        if (record != null) {
            //如果redis中有选课记录，返回重复选课
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        //预减人数
        Long number = redisTemplate.opsForValue().decrement("classes:" + classesId);
        if(number == null || number < 0){
            if(number != null){
                //让名额显示为0
                redisTemplate.opsForValue().increment("classes:" + classesId);
            }
            return RespBean.error(RespBeanEnum.EMPTY_STUDENT);
        }

        //预减人数，发送lua脚本
        // Long number = redisTemplate.execute(
        //         redisScript,
        //         Collections.singletonList("classes:" + classesId),
        //         Collections.emptyList()
        // );
        // if(number == null || number < 0){
        //     //该课程还有名额,redis减名额
        //     return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        // }

        // 将选课信息放入消息队列中
        ChooseCourseMessage message = new ChooseCourseMessage(student, classesId);
        mqSender.sendChooseCourseMessage(JsonUtil.object2JsonStr(message));
        //返回0表示该订单正在派对中
        return RespBean.success(0);
    }

    /**
     * 轮询获取选课结果接口
     * @param student 学生
     * @param classesId 课程id
     * @return recordId:成功，-1：选课失败，0：排队中
     */
    @GetMapping(value = "/result")
    @ResponseBody
    public RespBean getResult(Student student, Long classesId){
        //轮询redis
        Record record = (Record) redisTemplate.opsForValue().get("record:" + classesId + ":" + student.getStudentNumber());
        if(record == null){
            //redis没有查数据库
            record = recordService.getOne(
                    new QueryWrapper<Record>()
                            .eq("student_id", student.getId())
                            .eq("classes_id", classesId)
                            .and(wrapper -> {
                                wrapper.eq("status", RecordStatusEnums.UN_CHECK)
                                        .or()
                                        .eq("status", RecordStatusEnums.CHECKED);
                            })
            );
        }
        if (record != null) {
            //如果选课记录存在，返回选课记录id
            redisTemplate.opsForValue().set("record:" + classesId + ":" + student.getStudentNumber(), record, 120,
                    TimeUnit.SECONDS);
            return RespBean.success(record.getId());
        }
        Integer number = (Integer) redisTemplate.opsForValue().get("classes:" + classesId);
        if(number == null || number == 0){
            //没抢到, -1选课失败
            return RespBean.success(-1);
        }
        //返回订单排队中
        return RespBean.success(0);
    }

    /**
     * 获取选课地址
     *
     * @param student 学生
     * @param classesId 课程id
     * @return 选课地址
     */
    @AccessLimit(second = 5, maxCount = 3)
    @GetMapping(value = "/path")
    @ResponseBody
    public RespBean getPath(Student student, Long classesId) {
        String str = recordService.createPath(student, classesId);
        return RespBean.success(str);
    }

    /**
     * 将班级人数预先加载到redis中，模拟预热
     *
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<ClassesVo> list = classesService.findClassesVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(classesVo -> {
            Long id = classesVo.getId();
            redisTemplate.opsForValue().set("classes:" + id, classesVo.getStudentNum());
        });

    }

    /**
     *  获取到选课记录详细信息
     *
     * @param recordId 选课记录id
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(Long recordId){
        Record record = recordService.getById(recordId);
        Classes classes = classesService.getById(record.getClassesId());
        Course course = courseService.getById(record.getCourseId());

        RecordDetailVo recordDetailVo = new RecordDetailVo(record, classes, course);
        return RespBean.success(recordDetailVo);
    }

    /**
     * 模拟课程报道
     *
     * @param student 学生信息
     * @param recordId 选课记录id
     * @return
     */
    @RequestMapping("/check")
    @ResponseBody
    public RespBean doPay(Student student, Long recordId){
        return recordService.updateStatus(student, recordId);
    }
}
