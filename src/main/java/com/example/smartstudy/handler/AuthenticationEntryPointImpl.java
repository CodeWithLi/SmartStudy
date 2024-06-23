package com.example.smartstudy.handler;

import com.example.smartstudy.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");

        // 设置字符编码为UTF-8
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Result<String> result = new Result<>();
        result.setCode(HttpStatus.FORBIDDEN.value());
        result.setMsg("认证失败，请重新登录或重新访问");

        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
