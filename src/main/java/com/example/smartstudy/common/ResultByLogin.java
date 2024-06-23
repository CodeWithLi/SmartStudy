package com.example.smartstudy.common;

import cn.hutool.http.HttpStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResultByLogin<T> implements Serializable {
    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private int status;//响应状态

    private String token;//token值

    private Integer userStatus;//0不可登录，1为管理员,2为超级管理员

    public static <T> ResultByLogin<T> success(T object, String token) {
        ResultByLogin<T> result = new ResultByLogin<T>();
        result.data = object;
        result.status = HttpStatus.HTTP_OK;
        result.code = 1;
        result.token = token;
        return result;
    }


    public static <T> ResultByLogin<T> successByStatus(T object, String token, Integer userStatus) {
        ResultByLogin<T> result = new ResultByLogin<T>();
        result.data = object;
        result.status = HttpStatus.HTTP_OK;
        result.code = 1;
        result.token = token;
        result.userStatus = userStatus;
        return result;
    }


    public static <T> ResultByLogin<T> error(String msg) {
        ResultByLogin<T> result = new ResultByLogin<T>();
        result.msg = msg;
        result.status = HttpStatus.HTTP_ACCEPTED;
        result.code = 0;
        return result;
    }


}