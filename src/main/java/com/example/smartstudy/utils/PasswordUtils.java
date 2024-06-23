package com.example.smartstudy.utils;

import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {
    
    @Resource
    private UserMapper userMapper;        

    //创建security对象
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //加密密码
    public String encodedPass(String password){
        //加密密码
        return passwordEncoder.encode(password);
    }

    //校对数据库密码
    public void matchesPass(String account,String rowPass){
        String sqlPass=null;
        //进行简单的判断
        if (account.matches("^1[3-9]\\d{9}$")){
            //根据手机号查询出用户的密码
            sqlPass=userMapper.selectUserByPhone(account).getPassword();
        } else if (account.matches("^\\d{18}$")) {
            //根据身份证查询出用户的密码
            sqlPass=userMapper.selectUserByIdNumber(account).getPassword();
        }else {
            throw new BaseException("登录错误");
        }
        //进行判断
        boolean matches = passwordEncoder.matches(rowPass, sqlPass);
        if (!matches){
            throw new BaseException("密码错误");
        }
    }
}
