package com.huajframe.choosecourse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.huajframe.choosecourse.dao")
@EnableTransactionManagement
public class ChooseCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChooseCourseApplication.class, args);
    }

}
