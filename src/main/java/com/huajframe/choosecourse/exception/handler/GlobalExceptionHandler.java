package com.huajframe.choosecourse.exception.handler;

import com.huajframe.choosecourse.enums.RespBeanEnum;
import com.huajframe.choosecourse.exception.GlobalException;
import com.huajframe.choosecourse.vo.RespBean;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author Hua JFarmer
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BindException.class, GlobalException.class})
    public RespBean exceptionHandler(Exception e){
        if(e instanceof GlobalException){
            //如果是全局错误（自己抛出）返回全局错误中自己注入的信息
            GlobalException globalException = (GlobalException) e;
            RespBeanEnum respBeanEnum = globalException.getRespBeanEnum();
            return RespBean.error(respBeanEnum);
        }else if(e instanceof BindException){
            //数据校验错误返回错误的原因，具体到哪个参数
            BindException bindException = (BindException) e;
            RespBean respBean = new RespBean();
            respBean.setCode(RespBeanEnum.BIND_ERROR.getCode());
            respBean.setMessage("参数校验异常：" + bindException
                    .getBindingResult()
                    .getAllErrors()
                    .get(0)
                    .getDefaultMessage());
            return respBean;
        }
        //其余错误返回服务器异常
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
