package com.huajframe.choosecourse.validator.annotation;

import com.huajframe.choosecourse.validator.IsStudentNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义学号校验的注解
 *
 * @author Hua JFarmer
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IsStudentNumberValidator.class})
public @interface IsStudentNumber {
    String message() default "学号格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
