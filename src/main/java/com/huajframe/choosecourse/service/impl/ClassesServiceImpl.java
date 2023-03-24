package com.huajframe.choosecourse.service.impl;

import com.huajframe.choosecourse.dao.CourseMapper;
import com.huajframe.choosecourse.entity.Classes;
import com.huajframe.choosecourse.dao.ClassesMapper;
import com.huajframe.choosecourse.service.IClassesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huajframe.choosecourse.vo.ClassesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hua JFrame
 * @since 2023-03-22
 */
@Service
public class ClassesServiceImpl extends ServiceImpl<ClassesMapper, Classes> implements IClassesService {

    @Autowired
    private ClassesMapper classesMapper;
    /**
     * 获取选修课程开班信息
     *
     * @return 选修课程开班信息
     */
    @Override
    public List<ClassesVo> findClassesVo() {
        return classesMapper.findClassesVo();
    }

    /**
     * 根据课程编号获取课程详情
     *
     * @param classesId 课程id
     * @return 某个课程的详细信息
     */
    @Override
    public ClassesVo findClassesVoByClassesId(Long classesId) {
        return classesMapper.findClassesVoByClassesId(classesId);
    }
}
