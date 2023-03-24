package com.huajframe.choosecourse.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验学号是否正确
 * @author Hua JFarmer
 */
public class ValidatorUtil {
    /**
     * 学号正则
     */
    private static final Pattern STUDENT_NUMBER_PATTERN = Pattern.compile("[A-Z][0-9]{11}$");

    public static boolean isStudentNumber(String studentNumber){
        if (StringUtils.isEmpty(studentNumber)) {
            return false;
        }
        Matcher matcher = STUDENT_NUMBER_PATTERN.matcher(studentNumber);
        return matcher.matches();
    }
}
