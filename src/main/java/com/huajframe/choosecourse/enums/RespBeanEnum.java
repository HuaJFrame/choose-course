package com.huajframe.choosecourse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态枚举
 * @author Hua JFarmer
 */
@Getter
@AllArgsConstructor
public enum RespBeanEnum {
    //通用状态码
    SUCCESS(200,"success"),
    ERROR(500,"服务端异常"),
    //登录模块
    LOGIN_ERROR(500210,"学号或者密码错误"),
    STUDENT_NUMBER_ERROR(500211,"学号格式错误"),
    BIND_ERROR(500212, "参数校验错"),
    UNLOGIN(500213, "暂时无法访问, 请先登录"),
    LOGINED(500214, "亲，已经登录过了啊！"),
    STUDENT_NUMBER_NOT_EXIST(500213, "学号不存在"),
    PASSWORD_UPDATE_FAIL(500214, "密码更新失败"),
    //选课模块
    CHOOSE_CLOSE(500500, "选课已经结束"),
    EMPTY_STUDENT(500501, "名额不足"),
    REPEAT_ERROR(500502, "请勿重复选课"),
    REQUEST_ILLEGAL(500503, "请求非法"),
    REQUEST_FREQUENTLY(500504, "请求频繁，请稍后再试"),
    //报道模块
    CHECK_ERROR(500600, "报道失败");


    private final Integer code;
    private final String message;
}
