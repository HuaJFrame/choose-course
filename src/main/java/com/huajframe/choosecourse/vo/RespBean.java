package com.huajframe.choosecourse.vo;

import com.huajframe.choosecourse.enums.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回结果对象
 * @author Hua JFarmer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    /**
     * 状态码
     */
    private long code;
    /**
     * 消息
     */
    private String message;
    /**
     * 数据
     */
    private Object obj;

    /**
     * 成功返回结果
     */
    public static RespBean success() {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),
                RespBeanEnum.SUCCESS.getMessage(), null);
    }
    /**
     * 成功返回结果
     *
     * @param obj
     */
    public static RespBean success(Object obj) {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),
                RespBeanEnum.SUCCESS.getMessage(), obj);
    }
    /**
     * 失败返回结果
     *
     * @param respBeanEnum
     * @return
     */
    public static RespBean error(RespBeanEnum respBeanEnum) {
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(),
                null);
    }
}
