package com.example.smartstudy.controller;

import com.example.smartstudy.common.Result;
import com.example.smartstudy.common.ResultByLogin;
import com.example.smartstudy.constant.JwtClaimsConstant;
import com.example.smartstudy.constant.RedisKeyPrefixConstant;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.model.dto.RegisterEditDto;
import com.example.smartstudy.model.dto.Security.Lgoin.LoginRequest;
import com.example.smartstudy.model.dto.Security.Register.RegisterEditRequest;
import com.example.smartstudy.model.dto.UserDto;
import com.example.smartstudy.model.dto.UserLoginDto;
import com.example.smartstudy.model.vo.LoginVo;
import com.example.smartstudy.properties.JwtProperties;
import com.example.smartstudy.service.UserService;
import com.example.smartstudy.utils.JwtUtils;
import com.example.smartstudy.utils.ThrowUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户信息
 */
@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/loginSecurity")
    public Result loginSecurityTest(@RequestBody @Valid LoginRequest loginRequest) {
        LoginVo loginVo = userService.loginSecurity(loginRequest);
        return Result.success(loginVo);
    }

    @PostMapping("/registerSecurity")
    public Result registerSecurity(@RequestBody RegisterEditRequest request) {
        boolean result = userService.registerSecurityUser(request);
        ThrowUtils.throwIf(!result, "注册失败");
        return Result.success("注册成功");
    }

    @PutMapping("/editSecurity")
    public Result editSecurity(@RequestBody RegisterEditRequest request) {
        boolean result = userService.editSecurityUser(request);
        ThrowUtils.throwIf(!result, "注册失败");
        return Result.success("注册成功");
    }

    @PostMapping("/sendSms")
    public Result sendSms(@RequestBody @Valid  RegisterEditRequest request) {
        String phone = request.getPhone();
        String purpose = request.getPurpose();
        ThrowUtils.throwIf(((!"register".equals(purpose)) && (!"edit".equals(purpose))), "请求目的错误");
        String smsCode = userService.sendSmsCode(phone, purpose);
        ThrowUtils.throwIf(smsCode == null, "验证码发送失败");
        return Result.success(smsCode);
    }











//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private JwtProperties jwtProperties;

//    @PostMapping("/sendSms")
//    public Result sendSms(@RequestBody RegisterEditDto registerEditDto) {
//        if (registerEditDto == null) {
//            throw new BaseException("请求参数错误");
//        }
//        String phone = registerEditDto.getPhone();
//        String purpose = registerEditDto.getPurpose();
//        ThrowUtils.throwIf(phone == null, "手机号码不为空");
//        ThrowUtils.throwIf((!"login".equals(purpose)) && (!"register".equals(purpose)) && (!"edit".equals(purpose)), "请求目的错误");
//        String smsCode = userService.sendSmsCode(phone, purpose);
//        ThrowUtils.throwIf(smsCode == null, "验证码发送失败");
//        return Result.success(smsCode);
//    }

//    @PostMapping("/testVal1")
//    public LoginRequest testVal1(@RequestBody @Valid LoginRequest loginRequest) {
//        return loginRequest;
//    }
//
//    @GetMapping("/test")
//    @PreAuthorize("hasAuthority('user1')")
//    public Result test1() {
//        return Result.success("权限");
//    }

//    /**
//     * 用户登录
//     *
//     * @param userLoginDto
//     * @return
//     */
//    @PostMapping("/login")
//    public ResultByLogin loginUser(@RequestBody UserLoginDto userLoginDto) {
//        if (userLoginDto == null) {
//            throw new BaseException("请求参数错误");
//        }
//        String account = userLoginDto.getAccount();
//        if (StringUtils.isBlank(account) && "".equals(account)) {
//            throw new BaseException("请求账号为空");
//        }
//        String password = userLoginDto.getPassword();
//        String code = userLoginDto.getCode();
//        if (password == null && code == null) {
//            throw new BaseException("请求参数错误");
//        }
//        UserDto userDto = userService.loginUser(account, password, code, userLoginDto.getPurpose());
//        String token = null;
//        Map<String, Object> claims = new HashMap<>();
//        //判断token是否存在
//        if (stringRedisTemplate.hasKey(RedisKeyPrefixConstant.Token + userDto.getAccount())) {
//            //token存在,返回token
//            token = stringRedisTemplate.opsForValue().get(RedisKeyPrefixConstant.Token + userDto.getAccount());
//            return ResultByLogin.success("登录成功", token);
//        } else if ("phone".equals(userDto.getAccountType())) {
//            //token不存在,生成token
//            claims.put(JwtClaimsConstant.userPhone, userDto.getPhone());
//        } else if ("idNumber".equals(userDto.getAccountType())) {
//            claims.put(JwtClaimsConstant.userIdNumber, userDto.getIdNumber());
//        } else {
//            return ResultByLogin.error("登录失败");
//        }
//        claims.put(JwtClaimsConstant.userPassword, password);
//        token = JwtUtils.createJwt(jwtProperties.getOrSecretKey(), jwtProperties.getOrTtl(), claims);
//        stringRedisTemplate.opsForValue().set(RedisKeyPrefixConstant.Token + userDto.getPhone(), token, 3, TimeUnit.DAYS);
//        return ResultByLogin.success("登录成功", token);
//    }
//
//    /**
//     * 注册用户
//     *
//     * @param registerEditDto
//     * @return
//     */
//    @PostMapping("/register")
//    public Result registerUser(@RequestBody RegisterEditDto registerEditDto) {
//        if (registerEditDto == null) {
//            throw new BaseException("请求参数错误");
//        }
//        boolean result = userService.registerUser(registerEditDto);
//        ThrowUtils.throwIf(!result, "注册失败");
//        return Result.success("注册成功");
//    }
//
//    /**
//     * 忘记密码
//     *
//     * @param registerEditDto
//     * @return
//     */
//    @PutMapping("/editPass")
//    public Result editPassUser(@RequestBody RegisterEditDto registerEditDto) {
//        if (registerEditDto == null) {
//            throw new BaseException("请求参数错误");
//        }
//        boolean result = userService.editPassUser(registerEditDto);
//        ThrowUtils.throwIf(!result, "修改密码失败");
//        return Result.success("修改成功");
//    }
//

    /**
     * 身份证认证
     * @param file
     * @return
     */
//    @PutMapping("/identify")
//    public Result identifyIdNumber(@RequestParam MultipartFile file){
//        if (file==null){
//            throw new BaseException("请求参数错误");
//        }
//        if (!file.getContentType().startsWith("image")){
//            throw new BaseException("请正确传输图片");
//        }
//        boolean result = userService.identifyIdNumber(file);
//        ThrowUtils.throwIf(!result,"认证失败");
//        return Result.success("认证成功");
//    }

}
