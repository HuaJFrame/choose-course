package com.huajframe.choosecourse.exception;

import com.huajframe.choosecourse.enums.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 全局异常
 * @author Hua JFarmer
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalException extends RuntimeException{
    /**
     * 返回前端信息集合
     */
    private RespBeanEnum respBeanEnum;
}
