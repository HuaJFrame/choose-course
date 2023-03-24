package com.huajframe.choosecourse.vo;

import com.huajframe.choosecourse.entity.Classes;
import com.huajframe.choosecourse.entity.Course;
import com.huajframe.choosecourse.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Hua JFarmer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDetailVo {
    private Record record;
    private Classes classes;
    private Course course;
}
