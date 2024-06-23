package com.example.smartstudy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册或修改账号
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEditDto {
    //用户名
    private String username;
    private String userId;
    private String phone;
    private String code;
    //用于判断是注册还是修改
    private String purpose;
    private String password;
}
