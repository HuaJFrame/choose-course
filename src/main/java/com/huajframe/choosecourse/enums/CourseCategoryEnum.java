package com.huajframe.choosecourse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hua JFarmer
 */
@Getter
@AllArgsConstructor
public enum CourseCategoryEnum {

    /**
     * 公开课
     */
    PUBLIC(0),
    /**
     * 选修课
     */
    OPTIONAL(1),
    /**
     * 专业课
     */
    PROFESSIONALSi(2);

    private int code;
}
