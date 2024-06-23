package com.example.smartstudy.config;

import com.example.smartstudy.filter.JwtAuthenticationTokenFilter;
import com.example.smartstudy.handler.AccessDeniedHandlerImpl;
import com.example.smartstudy.handler.AuthenticationEntryPointImpl;
import com.example.smartstudy.service.UserService;
import com.example.smartstudy.service.impl.LoadUserByUsernameServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Resource
    private LoadUserByUsernameServiceImpl userService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //过滤链
    @Resource
    JwtAuthenticationTokenFilter jwtFilter;//后面jwt验证需要用到的过滤器
//
//    //自定义失败处理器
    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Resource
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(AbstractHttpConfigurer::disable)//取消默认登录页面的使用
                .logout(AbstractHttpConfigurer::disable)//取消默认登出页面的使用
                .authenticationProvider(authenticationProvider())//将自己配置的PasswordEncoder放入SecurityFilterChain中
                .csrf(AbstractHttpConfigurer::disable)//禁用csrf保护，前后端分离不需要
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//禁用session，因为我们已经使用了JWT
                .httpBasic(AbstractHttpConfigurer::disable)//禁用httpBasic，因为我们传输数据用的是post，而且请求体是JSON
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/user/loginSecurity","/user/sendSms","/user/registerSecurity","/user/editSecurity").permitAll() //登录放行
                        .anyRequest().authenticated())//其余均要身份认证
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)//将用户授权时用到的JWT校验过滤器添加进SecurityFilterChain中，并放在UsernamePasswordAuthenticationFilter的前面
                .exceptionHandling(ExceptionHandling -> ExceptionHandling.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler));
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
