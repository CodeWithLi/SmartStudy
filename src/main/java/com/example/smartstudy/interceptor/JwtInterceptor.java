package com.example.smartstudy.interceptor;

import com.example.smartstudy.constant.JwtClaimsConstant;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.properties.JwtProperties;
import com.example.smartstudy.utils.BaseUtils;
import com.example.smartstudy.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

//    @Autowired
//    private JwtProperties jwtProperties;
//
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //存储数据
//        String account = null;
//        //判断是否为跨域
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            log.info("当前请求为跨域，放行请求");
//            return true;
//        }
//        //获取前端传来的token值，判断是否可以正常操作
//        String tokenHeader = request.getHeader(jwtProperties.getOrTokenName());
//        if (!StringUtils.hasText(tokenHeader)) {
//            throw new BaseException("请先进行登录");
//        }
//        log.info("拦截器获取到token值为：{}", tokenHeader);
//        //解析token值
//        Claims claims = JwtUtils.praseJwt(jwtProperties.getOrSecretKey(), tokenHeader);
//        //存储token值里的数据
//        if (claims.containsKey(JwtClaimsConstant.userPhone)) {
//            account = (String) claims.get(JwtClaimsConstant.userPhone);
//        } else if (claims.containsKey(JwtClaimsConstant.userIdNumber)) {
//            account = (String) claims.get(JwtClaimsConstant.userIdNumber);
//        }
//        if (!StringUtils.hasText(account) || "null".equalsIgnoreCase(account)) {
//            throw new BaseException("拦截器获取的token值中无信息");
//        }
//        //存储用户信息到线程中，方便调用
//        BaseUtils.setCurrentAccount(account);
        return true;
    }

}
