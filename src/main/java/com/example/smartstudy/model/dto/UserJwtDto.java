package com.example.smartstudy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 用于用户登录
 * 使用身份证或手机号登录
 */
public class UserJwtDto {
    //身份证
    private String idNumber;
    //手机号
    private String phone;
}
