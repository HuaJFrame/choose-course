package com.huajframe.choosecourse;

import com.huajframe.choosecourse.entity.Student;
import com.huajframe.choosecourse.service.IStudentService;
import com.huajframe.choosecourse.util.MD5Util;
import com.huajframe.choosecourse.vo.RespBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ChooseCourseApplicationTests {

    private static final int STUDENT_COUNT = 10000;

    @Autowired
    private IStudentService studentService;

    /**
     * 生成学生数据，将studentToken写入文件
     *
     * @throws IOException
     */
    // @Test
    void contextLoads() throws IOException {
        List<Student> studentsList = new ArrayList<>(STUDENT_COUNT);
        long count = 20190000000L;
        for (int i = 1; i <= STUDENT_COUNT; i++) {
            Student student = new Student();
            ++count;
            student.setStudentNumber("B" + count);
            student.setName("test" + i);
            student.setSalt("asdfg1243");
            student.setPassword(
                    MD5Util.inputPassToDBPass("123456",
                            student.getSalt())
            );
            studentsList.add(student);
        }

        System.out.println("数据初始化完成，准备插入数据库中-------");

        //插入数据库中
        studentService.saveBatch(studentsList);

        System.out.println("插入完成，准备获取token写入文件---------");

        //插入完成，准备获取token写入文件
        File file = new File("F:\\apache-jmeter-5.4.1\\TEST Result\\ChooseCourse\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        studentsList.forEach(student -> {
            //获取到uuid
            String studentToken = sendPostRequest(student.getStudentNumber());
            //换行
            try {
                //写入文件
                writer.write(student.getStudentNumber() + "," +studentToken);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.flush();
        writer.close();
        System.out.println("执行完毕");
    }

    /**
     * 发送http请求，获取到token
     * @param studentNumber 学号
     * @return studentToken
     */
    public static String sendPostRequest(String studentNumber) {
        String url = "http://localhost:9090/login/doLogin";
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //设置参数
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("studentNumber", studentNumber);
        params.add("password", MD5Util.inputPassToFormPass("123456"));
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        HttpMethod method = HttpMethod.POST;
        //执行HTTP请求，将返回的结构使用RestVO类格式化
        ResponseEntity<RespBean> response = client.exchange(url, method, requestEntity, RespBean.class);
        //获取到studentToken
        return (String) response.getBody().getObj();
    }

}
