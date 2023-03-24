package com.huajframe.choosecourse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/**
 * <p>
 * 
 * </p>
 *
 * @author Hua JFrame
 * @since 2023-03-22
 */
@Data
@TableName("t_course")
public class Course {

    /**
     * 课程ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程编号
     */
    private String courseNumber;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程图片
     */
    private String courseImg;

    /**
     * 课程详情
     */
    private String courseDetail;

    /**
     * 课程类别  0 - 公开课  1 - 选修课  2 - 专业课 
     */
    private int category;
}
