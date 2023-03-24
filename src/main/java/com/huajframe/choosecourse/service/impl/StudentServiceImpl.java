package com.huajframe.choosecourse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huajframe.choosecourse.entity.Student;
import com.huajframe.choosecourse.dao.StudentMapper;
import com.huajframe.choosecourse.enums.RespBeanEnum;
import com.huajframe.choosecourse.exception.GlobalException;
import com.huajframe.choosecourse.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huajframe.choosecourse.util.CookieUtil;
import com.huajframe.choosecourse.util.MD5Util;
import com.huajframe.choosecourse.util.UUIDUtil;
import com.huajframe.choosecourse.vo.LoginVo;
import com.huajframe.choosecourse.vo.RespBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hua JFrame
 * @since 2023-03-22
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 登录
     *
     * @param request
     * @param response
     * @param loginVo  前端传入
     * @return 登录结果
     */
    @Override
    public RespBean login(HttpServletRequest request, HttpServletResponse response, LoginVo loginVo) {
        String studentNumber = loginVo.getStudentNumber();
        String password = loginVo.getPassword();
        //根据电话号码查询学生
        Student student = studentMapper.selectOne(
                new QueryWrapper<Student>()
                        .eq("student_number", studentNumber)
        );
        //学生为空
        if(student == null){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //校验密码，根据前端传入的fromPass和数据库的盐生成的密码并和实际密码比对
        String expPassToDBPass = MD5Util.formPassToDBPass(password, student.getSalt());
        if (!expPassToDBPass.equals(student.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 生成cookie
        String token = UUIDUtil.uuid();
        //存入redis
        redisTemplate.opsForValue().set("student:" + token, student);
        //设置cookie
        CookieUtil.setCookie(request, response, "studentToken", token);
        return RespBean.success(token);
    }

    /**
     * 根据cookie在redis中取出学生信息
     *
     * @param request
     * @param response
     * @return 学生信息
     */
    @Override
    public Student getStudentByCookie(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.getCookieValue(request, "studentToken");
        if(StringUtils.isEmpty(token)){
            return null;
        }
        Student student = (Student) redisTemplate.opsForValue().get("student:" + token);
        if(student != null){
            //重新设置一下cookie
            CookieUtil.setCookie(request, response, "studentToken", token);
        }
        return student;
    }

    /**
     * 模拟学生修改密码时redis等需要做哪些处理
     *
     * @param studentToken cookie中token
     * @param id           学生id
     * @param password     新密码，前端经过一次加密后
     * @return
     */
    @Override
    public RespBean updatePassword(String studentToken, Long id, String password) {
        Student student = studentMapper.selectById(id);
        if(student == null){
            throw new GlobalException(RespBeanEnum.STUDENT_NUMBER_NOT_EXIST);
        }
        student.setPassword(MD5Util.formPassToDBPass(password, student.getSalt()));
        int count = studentMapper.updateById(student);
        if(count < 1){
            return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
        }else{
            //删除redis缓存token
            redisTemplate.opsForValue().decrement("student:" + studentToken);
            return RespBean.success();
        }
    }
}
