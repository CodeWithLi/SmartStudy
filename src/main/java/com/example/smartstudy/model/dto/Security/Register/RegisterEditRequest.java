package com.example.smartstudy.model.dto.Security.Register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEditRequest {
    //用户名
    private String username;
    private String userId;
    @Pattern(regexp = "^1[3-9]\\d{9}$",message = "手机号码格式错误")
    private String phone;
    @Pattern(regexp = "^\\d{4}$",message = "短信格式错误格式错误")
    private String code;
    //用于判断是注册还是修改
    private String purpose;
//    @NotBlank(message = "密码不为空")
//    @Size(min = 6,max = 20,message = "密码为6-20个字符")    @NotBlank(message = "密码不为空")
//    @Size(min = 6,max = 20,message = "密码为6-20个字符")
    private String password;
    private String role;
    private Integer roleId;
}
