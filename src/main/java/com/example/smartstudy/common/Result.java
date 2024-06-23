package com.example.smartstudy.common;


import cn.hutool.http.HttpStatus;
import lombok.Data;

import java.io.Serializable;

@Data
/**
 * 设置统一返回结果
 */
public class Result<T> implements Serializable {
    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private int status;//响应状态


    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        result.status = HttpStatus.HTTP_OK;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.status = HttpStatus.HTTP_OK;
        result.code = 1;
        return result;
    }


    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<T>();
        result.msg = msg;
        result.status = HttpStatus.HTTP_ACCEPTED;
        result.code = 0;
        return result;
    }
}