package com.huajframe.choosecourse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Timestamp;

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
@TableName("t_classes")
public class Classes {

    /**
     * 班级ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程Id
     */
    private Long courseId;

    /**
     * 教室位置
     */
    private String classroom;

    /**
     * 最大班级人数
     */
    private Integer studentNum;

    /**
     * 已选人数
     */
    private Integer currentStudentNum;

    /**
     * 选课开始时间
     */
    private Timestamp startDate;

    /**
     * 选课结束时间
     */
    private Timestamp endDate;
}
