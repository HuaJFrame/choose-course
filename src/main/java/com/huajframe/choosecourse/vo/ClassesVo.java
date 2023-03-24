package com.huajframe.choosecourse.vo;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Hua JFarmer
 */
@Data
@NoArgsConstructor
public class ClassesVo {

    /**
     * 课程id
     */
    private Long id;

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

    public ClassesVo(Long id){
        this.id = id;
    }

}
