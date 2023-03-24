package com.huajframe.choosecourse.vo;

import com.huajframe.choosecourse.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hua JFarmer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChooseCourseMessage {
    private Student student;
    private Long classesId;
}
