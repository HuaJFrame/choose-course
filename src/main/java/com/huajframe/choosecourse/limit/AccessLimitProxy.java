package com.huajframe.choosecourse.limit;

import com.huajframe.choosecourse.enums.RespBeanEnum;
import com.huajframe.choosecourse.limit.annotation.AccessLimit;
import com.huajframe.choosecourse.service.IStudentService;
import com.huajframe.choosecourse.vo.RespBean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 限流实现类
 *
 * 实现思路就是根据请求路径和学生学号使用计数限流
 * @author Hua JFarmer
 */
@Component
@Aspect
public class AccessLimitProxy {

    @Autowired
    private IStudentService studentService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(com.huajframe.choosecourse.limit.annotation.AccessLimit)")
    public Object before(ProceedingJoinPoint pjp) throws Throwable {
        //获取到对应的注解信息
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        AccessLimit accessLimit = signature.getMethod().getDeclaredAnnotation(AccessLimit.class);
        //从注解中获得时间和最大访问数量
        int second = accessLimit.second();
        int maxCount = accessLimit.maxCount();
        boolean all = accessLimit.all();
        //获取到请求路径
        String url;
        //获取到学生学号
        Long studentNumber;

        HttpServletResponse response;
        try {
            //获取到request
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            url = request.getRequestURI();
            response = requestAttributes.getResponse();
            studentNumber = studentService.getStudentByCookie(request, response).getId();
        }catch (Exception e){
            //怕空指针异常
            return RespBean.error(RespBeanEnum.ERROR);
        }
        //通过redis实现计数限流
        String key = url;
        if (all){
            //如果对所有人限流，加上学生学号
            key = key + ":" + studentNumber;
        }
        BoundValueOperations<String, Object> boundValueOps = redisTemplate.boundValueOps(key);
        Long count = boundValueOps.increment();
        if(count == 1){
            boundValueOps.set(1, second, TimeUnit.SECONDS);
        }else if(count > maxCount){
            return RespBean.error(RespBeanEnum.REQUEST_FREQUENTLY);
        }
        return pjp.proceed();
    }
}
