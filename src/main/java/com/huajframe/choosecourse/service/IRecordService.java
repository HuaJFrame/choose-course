package com.huajframe.choosecourse.service;

import com.huajframe.choosecourse.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huajframe.choosecourse.entity.Student;
import com.huajframe.choosecourse.vo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hua JFrame
 * @since 2023-03-22
 */
public interface IRecordService extends IService<Record> {

    /**
     * 选课
     * @param student    学生信息
     * @param classesId   课程id
     */
    void choose(Student student, Long classesId);


    /**
     * 根据学生信息和课程id获取独有的选课接口
     * @param student 学生信息
     * @param classesId 课程id
     * @return
     */
    String createPath(Student student, Long classesId);

    /**
     * 判断请求路径是否合法
     * 
     * @param student 学生信息
     * @param classesId 课程id
     * @param path 请求路径
     * @return
     */
    boolean checkPath(Student student, Long classesId, String path);

    /**
     * 模拟课程报道
     *
     * @param student 学生信息
     * @param recordId 选课记录id
     *
     * @return
     */
    RespBean updateStatus(Student student, Long recordId);

    /**
     * 学生超时未报道
     * @param recordId 选课记录id
     */
    void timeoutNotCheck(Long recordId);
}
