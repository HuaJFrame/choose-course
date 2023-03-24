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
@TableName("t_student")
public class Student {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生学号
     */
    private String studentNumber;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * MD5(MD5(pw明文+固定salt)+salt)
     */
    private String password;

    private String salt;
}
