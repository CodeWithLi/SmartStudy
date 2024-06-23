package com.example.smartstudy.handler;

import com.example.smartstudy.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");

        // 设置字符编码为UTF-8
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Result<String> result = new Result<>();
        result.setCode(HttpStatus.FORBIDDEN.value());
        result.setMsg("没有权限访问该资源");

        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
