package com.example.smartstudy.service;

import com.example.smartstudy.common.Result;
import com.example.smartstudy.model.dto.RegisterEditDto;
import com.example.smartstudy.model.dto.Security.Lgoin.LoginRequest;
import com.example.smartstudy.model.dto.Security.Register.RegisterEditRequest;
import com.example.smartstudy.model.dto.UserDto;
import com.example.smartstudy.model.vo.LoginVo;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    //用于登录
    UserDto loginUser(String account, String password, String code,String purpose);

    //发送验证码
    String sendSmsCode(String phone,String purpose);

    //注册用户
    boolean registerUser(RegisterEditDto registerEditDto);

    //修改密码
    boolean editPassUser(RegisterEditDto registerEditDto);


    //身份证识别
    boolean identifyIdNumber(MultipartFile file);

    //security登录
    LoginVo loginSecurity(LoginRequest loginRequest);

    //security注册
    boolean registerSecurityUser(RegisterEditRequest request);

    //security修改
    boolean editSecurityUser(RegisterEditRequest request);

    //退出
    Result logout();

}
