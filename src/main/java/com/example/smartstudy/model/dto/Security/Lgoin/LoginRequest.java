package com.example.smartstudy.model.dto.Security.Lgoin;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
//    @Pattern(regexp = "^1[3-9]\\d{9}$",message = "手机号码格式错误")
    private String phone;
//    @Pattern(regexp = "^\\d{18}$",message = "身份证号码格式错误")
    private String idNumber;
    private String userName;
    @NotBlank(message = "密码不为空")
    @Size(min = 6,max = 20,message = "密码为6-20个字符")
    private String password;
    @NotNull(message = "登录类型不为空")
    private LoginType loginType;

    public void setLoginType(String loginType) {
        if (!StringUtils.isBlank(loginType)&&!"".equals(loginType)) {
            this.loginType =LoginType.valueOf(loginType) ;
        }
    }
}
