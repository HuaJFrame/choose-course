package com.huajframe.choosecourse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 选课记录枚举
 * @author Hua JFarmer
 */
@Getter
@AllArgsConstructor
public enum RecordStatusEnums {
    /**
     * 未报到
     */
    UN_CHECK(0),
    /**
     * 已经报道
     */
    CHECKED(1),
    /**
     * 超时
     */
    TIMEOUT(2);

    private int status;
}
