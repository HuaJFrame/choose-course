package com.huajframe.choosecourse.service;

import com.huajframe.choosecourse.entity.Classes;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huajframe.choosecourse.vo.ClassesVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hua JFrame
 * @since 2023-03-22
 */
public interface IClassesService extends IService<Classes> {

    /**
     * 获取选修课程开班信息
     * @return 选修课程开班信息
     */
    List<ClassesVo> findClassesVo();

    /**
     * 根据课程编号获取课程详情
     * @param classesId 课程id
     * @return 某个课程的详细信息
     */
    ClassesVo findClassesVoByClassesId(Long classesId);
}
