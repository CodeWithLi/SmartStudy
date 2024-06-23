package com.example.smartstudy.constant;

public enum UserRole {

    //管理员
    admin(1),

    //认证用户
    user(3),

    //未认证用户
    normalUser(2);

    private Integer code;


    private UserRole(Integer code){
        this.code=code;
    }


    public Integer getCode(){
        return this.code;
    }
}
