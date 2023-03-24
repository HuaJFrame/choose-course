package com.huajframe.choosecourse.dao;

import com.huajframe.choosecourse.entity.Classes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huajframe.choosecourse.vo.ClassesVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hua JFrame
 * @since 2023-03-22
 */
public interface ClassesMapper extends BaseMapper<Classes> {

    /**
     * 查询课程和班级对应细信息
     * @return
     */
    List<ClassesVo> findClassesVo();


    /**
     * 根据课程id获取课程详情
     *
     *
     * @param classesId 课程id
     * @return
     */
    ClassesVo findClassesVoByClassesId(Long classesId);
}
