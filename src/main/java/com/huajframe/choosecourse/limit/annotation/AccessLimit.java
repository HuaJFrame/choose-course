package com.huajframe.choosecourse.limit.annotation;

import java.lang.annotation.*;

/**
 * 判断接口是否访问过于频繁
 * 计数限流
 * @author Hua JFarmer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AccessLimit {

    /**
     * 时间，单位秒
     */
    int second();

    /**
     * 最大访问次数
     */
    int maxCount();

    /**
     * 是否对所有人限流，还是只对当前访问者限制访问次数，默认为当前访问者
     */
    boolean all() default  false;

}
