package com.example.smartstudy.model.dto.Security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    //用户真实信息
    private String name;
    //用户性别
    private String sex;
    //用户昵称
    private String username;
    //手机号
    private String phone;
    //身份证号
    private String idNumber;
    //密码
    private String password;
    //地址
    private String address;
    //生日
    private String birthday;
    //身份证头像
    private String headImage;
    //是否删除
    private String isDelete;
    //头像
    private String imageName;
    //用户类型
    private String belongStatus;
    //用户权限
    private List<String> authorities;
    //用户角色
    private String role;

}
