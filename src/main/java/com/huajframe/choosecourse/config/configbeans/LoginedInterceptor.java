package com.huajframe.choosecourse.config.configbeans;

import com.huajframe.choosecourse.entity.Student;
import com.huajframe.choosecourse.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 避免学生重复登录的拦截器
 * 导致redis中数据不正确
 *
 * @author Hua JFarmer
 */
@Component
public class LoginedInterceptor implements HandlerInterceptor {

    @Autowired
    private IStudentService studentService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Student student = studentService.getStudentByCookie(request, response);
        //学生未登录允许访问登录界面，已经登录禁止访问
        if(student != null){
            response.sendRedirect("/classes/toList");
            return false;
        }
        return true;
    }
}
