package com.example.smartstudy.filter;

import com.example.smartstudy.model.dto.Security.*;
import com.example.smartstudy.model.dto.Security.Lgoin.AdminLoginUserDetails;
import com.example.smartstudy.model.dto.Security.Lgoin.IdNumberLoginUserDetails;
import com.example.smartstudy.model.dto.Security.Lgoin.PhoneLoginUserDetails;
import com.example.smartstudy.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if (request.getRequestURI().equals("/user/loginSecurity")){
//            filterChain.doFilter(request,response);
//            return;
//        }
        if (request.getRequestURI().contains("/user")&&!request.getRequestURI().equals("user/logout")){
            filterChain.doFilter(request,response);
            return;
        }
        String token=request.getHeader("Authorization");
        if (!StringUtils.hasText(token)){
            filterChain.doFilter(request,response);
            return;
        }

        JwtClaims claims = jwtUtils.getUserIdJwt(token);

        //获取最新token值
        String redisToken = (String) redisTemplate.opsForValue().get("userToken+" + claims.getUserId());

        if (!token.equals(redisToken)){
            //token不为最新token，错误登录
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write("错误登录");
            return;
        }

        User user = (User) redisTemplate.opsForValue().get("user+" + claims.getUserId());
        if (user==null){
            log.error("用户未登录");
        }
        UsernamePasswordAuthenticationToken authenticationToken=null;


        //TODO 数据库获取权限信息封装到Authentication中
        List<GrantedAuthority> grantedAuthorities=
                user.getAuthorities().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //通过判断枚举类型，判断为哪种登录方式
        if (claims.getLoginType().equals("phone")){
            authenticationToken=
                    new UsernamePasswordAuthenticationToken(new PhoneLoginUserDetails(user,claims.getAuthorities()),null,grantedAuthorities);
        }else if (claims.getLoginType().equals("idNumber")){
            authenticationToken=
                    new UsernamePasswordAuthenticationToken(new IdNumberLoginUserDetails(user),null,grantedAuthorities);
        }else if (claims.getLoginType().equals("admin")){
            authenticationToken=
                    new UsernamePasswordAuthenticationToken(new AdminLoginUserDetails(user),null,grantedAuthorities);
        }

        //存入SecurityContextHolder
//        Authentication authenticate=authenticationToken;

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
