package com.huajframe.choosecourse.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.huajframe.choosecourse.entity.Classes;
import com.huajframe.choosecourse.entity.Record;
import com.huajframe.choosecourse.dao.RecordMapper;
import com.huajframe.choosecourse.entity.Student;
import com.huajframe.choosecourse.enums.RecordStatusEnums;
import com.huajframe.choosecourse.enums.RespBeanEnum;
import com.huajframe.choosecourse.rabbitmq.MqSender;
import com.huajframe.choosecourse.service.IClassesService;
import com.huajframe.choosecourse.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huajframe.choosecourse.service.IStudentService;
import com.huajframe.choosecourse.util.MD5Util;
import com.huajframe.choosecourse.util.UUIDUtil;
import com.huajframe.choosecourse.vo.RespBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hua JFrame
 * @since 2023-03-22
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    @Autowired
    private IClassesService classesService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private MqSender mqSender;
    @Autowired
    private IStudentService studentService;

    /**
     * 选课
     *
     * @param student   学生信息
     * @param classesId 课程id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void choose(Student student, Long classesId) {
        boolean result = classesService.update(
                new UpdateWrapper<Classes>()
                        .setSql("student_num = student_num - 1")
                        .setSql("current_student_num = current_student_num + 1")
                        .eq("id", classesId)
                        .gt("student_num", 0)
        );
        //减人数失败
        if(!result){
            //课程人数不足存入redis中
            redisTemplate.opsForValue().set("classes:" + classesId, 0);
            return;
        }
        //生成选课记录
        Record record = new Record();
        record.setClassesId(classesId);
        record.setCreateDate(new Date());
        record.setStudentId(student.getId());
        record.setDeliverAddrId(1);
        record.setStatus(RecordStatusEnums.UN_CHECK.getStatus());

        Classes classes = classesService.getById(classesId);
        record.setCourseId(classes.getCourseId());

        //插入数据库
        boolean save = save(record);
        if(!save){
            return;
        }

        redisTemplate.opsForValue().set("record:" + classesId + ":" + student.getStudentNumber(), record);
        //超时报道到选课关闭
        mqSender.sendRecordWaitTimeout(record.getId());
    }

    /**
     * 根据学生信息和课程id获取独有的选课接口
     *
     * @param student   学生信息
     * @param classesId 课程id
     * @return
     */
    @Override
    public String createPath(Student student, Long classesId) {
        String str = MD5Util.inputPassToFormPass(UUIDUtil.uuid());
        redisTemplate.opsForValue().set("choosePath:" + student.getStudentNumber() + ":" +
                classesId, str, 60, TimeUnit.SECONDS);
        return str;
    }

    /**
     * 判断请求路径是否合法
     *
     * @param student 学生信息
     * @param classesId 课程id
     * @param path    请求路径
     * @return
     */
    @Override
    public boolean checkPath(Student student, Long classesId, String path) {
        if (student == null || StringUtils.isEmpty(path)){
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("choosePath:" +
                student.getStudentNumber() + ":" + classesId);
        return path.equals(redisPath);
    }

    /**
     * 模拟课程报道
     *
     * @param student  学生信息
     * @param recordId 选课记录id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespBean updateStatus(Student student, Long recordId) {
        boolean result = update(
                new UpdateWrapper<Record>()
                        .set("report_date", new Date())
                        .set("status", RecordStatusEnums.CHECKED.getStatus())
                        .eq("id", recordId)
                        .eq("status", RecordStatusEnums.UN_CHECK.getStatus())
        );
        if(!result){
            return RespBean.error(RespBeanEnum.CHECK_ERROR);
        }
        redisTemplate.delete("record:" + recordId + ":" + student.getId());
        return RespBean.success();
    }

    /**
     * 学生超时未报道
     *
     * @param recordId 选课记录id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void timeoutNotCheck(Long recordId) {
        boolean result = update(
                new UpdateWrapper<Record>()
                        .set("status", RecordStatusEnums.TIMEOUT.getStatus())
                        .eq("id", recordId)
                        .eq("status", RecordStatusEnums.UN_CHECK.getStatus())
        );
        //修改选课记录状态成功
        if(!result){
            return;
        }
        Record record = getById(recordId);
        //对应课程信息要改变
        Long classesId = record.getClassesId();
        boolean result2 = classesService.update(
                new UpdateWrapper<Classes>()
                        .setSql("student_num = student_num + 1")
                        .setSql("current_student_num = current_student_num - 1")
                        .eq("id", classesId)
        );
        if(!result2){
            return;
        }
        Student student = studentService.getById(record.getStudentId());
        redisTemplate.opsForValue().increment("classes:" + classesId);
        //更新redis中的信息
        redisTemplate.opsForValue().set("record:" + classesId + ":" + student.getStudentNumber(), record);
    }
}
