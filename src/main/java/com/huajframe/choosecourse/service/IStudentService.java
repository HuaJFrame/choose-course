package com.huajframe.choosecourse.service;

import com.huajframe.choosecourse.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huajframe.choosecourse.vo.LoginVo;
import com.huajframe.choosecourse.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hua JFrame
 * @since 2023-03-22
 */
public interface IStudentService extends IService<Student> {

    /**
     * 登录
     *
     * @param request
     * @param response
     * @param loginVo 前端传入
     * @return 登录结果
     */
    RespBean login(HttpServletRequest request, HttpServletResponse response, LoginVo loginVo);

    /**
     * 根据cookie在redis中取出学生信息
     * @param request
     * @param response
     * @return 学生信息
     */
    Student getStudentByCookie(HttpServletRequest request, HttpServletResponse response);

    /**
     * 模拟学生修改密码时redis等需要做哪些处理
     * @param studentToken cookie中token
     * @param id  学生id
     * @param password 新密码，前端经过一次加密后
     * @return
     */
    RespBean updatePassword(String studentToken, Long id, String password);
}
