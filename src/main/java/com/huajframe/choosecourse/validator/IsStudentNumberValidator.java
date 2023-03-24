package com.huajframe.choosecourse.validator;


import com.huajframe.choosecourse.util.ValidatorUtil;
import com.huajframe.choosecourse.validator.annotation.IsStudentNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 学号校验类
 * @author Hua JFarmer
 */
public class IsStudentNumberValidator implements ConstraintValidator<IsStudentNumber,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return ValidatorUtil.isStudentNumber(s);
    }
}
