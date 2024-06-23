package com.example.smartstudy.service.impl;

import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.mapper.UserMapper;
import com.example.smartstudy.model.dto.Security.Lgoin.AdminLoginUserDetails;
import com.example.smartstudy.model.dto.Security.Lgoin.IdNumberLoginUserDetails;
import com.example.smartstudy.model.dto.Security.Lgoin.PhoneLoginUserDetails;
import com.example.smartstudy.model.dto.Security.User;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadUserByUsernameServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<String> permissions=null;

        if (username.matches("^1[3-9]\\d{9}$")){
            //手机号登录
            User user = userMapper.findUserByPhone(username);

            isNullUser(user);

            //获取用户权限
            permissions = getAuthorities(user.getUserId());

            user.setAuthorities(permissions);

            return new PhoneLoginUserDetails(user,permissions);
        } else if (username.matches("^\\d{18}$")) {
            //身份证登录
            User user = userMapper.findUserByIdNumber(username);

            isNullUser(user);

            //获取用户权限
            permissions = getAuthorities(user.getUserId());

            user.setAuthorities(permissions);

            return new IdNumberLoginUserDetails(user,permissions);
        }else if (username.equalsIgnoreCase("admin")){
            //身份证登录
            User user = userMapper.findUserByUsername(username);
            System.out.println(user);
            isNullUser(user);

            //获取用户权限
            permissions = getAuthorities(user.getUserId());

            user.setAuthorities(permissions);

            return new AdminLoginUserDetails(user,permissions);
        }
            throw new BaseException("用户名非手机号或身份证号");
    }


    public static void isNullUser(User user){
        if (user==null){
            throw new BaseException("用户不存在");
        }else if (StringUtils.isBlank(user.getUserId())){
            throw new BaseException("用户信息错误");
        }
    }

    public List<String> getAuthorities(String userId){
       return userMapper.selectUserPermission(userId);
    }
}
