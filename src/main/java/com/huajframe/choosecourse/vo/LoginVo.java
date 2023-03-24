package com.huajframe.choosecourse.vo;

import com.huajframe.choosecourse.validator.annotation.IsStudentNumber;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {
    @NotNull
    @IsStudentNumber
    private String studentNumber;

    @NotNull
    @Length(min = 6, max = 32)
    private String password;
}
