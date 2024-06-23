package com.example.smartstudy.controller;

import com.example.smartstudy.common.Result;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.model.dto.EditPassDto;
import com.example.smartstudy.model.vo.UserInfoVo;
import com.example.smartstudy.service.UserInfoService;
import com.example.smartstudy.service.UserService;
import com.example.smartstudy.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户个人信息
 */
@RestController
@RequestMapping("/info")
@Slf4j
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 查询用户信息
     * @return
     */
    @GetMapping("/")
    public Result userInfo(){
        UserInfoVo infoVo=userInfoService.getUserInfo();
        return Result.success(infoVo);
    }

    /**
     * 上传头像
     * @param file
     * @return
     */
    @PutMapping("/image")
    public Result uploadImage(@RequestParam MultipartFile file){
        if (file==null){
            throw new BaseException("请求参数错误");
        }
        if (!file.getContentType().startsWith("image")){
            throw new BaseException("请正确传输图片");
        }
        String preview=userInfoService.uploadImage(file);
        return Result.success(preview);
    }

    //更改密码，旧密码+新密码
    @PutMapping("/editPass")
    public Result editPass(@RequestBody EditPassDto editPassDto){
        //由于页面实现了忘记密码，所以这个接口只需要输入新旧密码
        if (editPassDto==null){
            throw new BaseException("请求参数错误");
        }
        boolean result=userInfoService.editPass(editPassDto);
        ThrowUtils.throwIf(!result,"修改密码失败");
        return Result.success("修改密码成功");
    }

    /**
     * 身份证认证
     * @param file
     * @return
     */
    @PutMapping("/identify")
    public Result identifyIdNumber(@RequestParam MultipartFile file){
        if (file==null){
            throw new BaseException("请求参数错误");
        }
        if (!file.getContentType().startsWith("image")){
            throw new BaseException("请正确传输图片");
        }
        boolean result = userInfoService.identifyIdNumber(file);
        ThrowUtils.throwIf(!result,"认证失败");
        return Result.success("认证成功");
    }

    //获取个人权限
    @GetMapping("authorities")
    public Result getAuthorities(){
        List<String> authorities= userInfoService.getAuthorities();
        return Result.success(authorities);
    }

    @Autowired
    private UserService userService;
    @DeleteMapping("/logoutSecurity")
    public Result logout() {
        return userService.logout();
    }

}
