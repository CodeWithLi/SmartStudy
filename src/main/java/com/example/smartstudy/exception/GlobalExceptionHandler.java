package com.example.smartstudy.exception;

import cn.hutool.core.util.EnumUtil;
import com.example.smartstudy.common.Result;
import com.example.smartstudy.model.dto.Security.Lgoin.LoginExceptionName;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends BaseException {

    //实现全局异常处理
    @ExceptionHandler(BaseException.class)
    public Result<String> exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleMethodValidationException(MethodArgumentNotValidException ex) {
        // 提取验证错误信息
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            if (EnumUtil.contains(LoginExceptionName.class,fieldError.getField())){
                errorMessage.append(fieldError.getDefaultMessage()).append("; ");
            }
        }
        log.error("方法级别校验异常：{}", errorMessage);
        return Result.error(errorMessage.toString()+"请求参数错误");
    }
}
