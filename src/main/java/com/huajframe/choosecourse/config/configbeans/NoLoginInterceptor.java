package com.huajframe.choosecourse.config.configbeans;

import com.huajframe.choosecourse.entity.Student;
import com.huajframe.choosecourse.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 未登录拦截器
 *
 * @author Hua JFarmer
 */
@Component
public class NoLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private IStudentService studentService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Student student = studentService.getStudentByCookie(request, response);
        //为空，就是没登陆
        if(student == null){
            response.sendRedirect("/login/toLogin");
            return false;
        }
        return true;
    }
}
