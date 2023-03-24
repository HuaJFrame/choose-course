package com.huajframe.choosecourse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hua JFrame
 * @since 2023-03-22
 */
@Data
@TableName("t_record")
public class Record {

    /**
     * 选课记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 班级ID
     */
    private Long classesId;

    /**
     * 收获地址ID
     */
    private Integer deliverAddrId;

    /**
     * 选课状态，0 - 为报道， 1 - 已报道
     */
    private int status;

    /**
     * 记录创建时间
     */
    private Date createDate;

    /**
     * 报道时间
     */
    private Date reportDate;
}
