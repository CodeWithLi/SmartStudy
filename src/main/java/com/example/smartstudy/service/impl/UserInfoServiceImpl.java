package com.example.smartstudy.service.impl;

import com.example.smartstudy.common.Result;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.mapper.UserInfoMapper;
import com.example.smartstudy.model.dto.EditPassDto;
import com.example.smartstudy.model.dto.Security.AuthenticateGetUserUtils;
import com.example.smartstudy.model.dto.Security.User;
import com.example.smartstudy.model.dto.UserDto;
import com.example.smartstudy.model.po.UserInfoPo;
import com.example.smartstudy.model.po.UserPo;
import com.example.smartstudy.model.vo.UserInfoVo;
import com.example.smartstudy.service.UserInfoService;
import com.example.smartstudy.service.UserService;
import com.example.smartstudy.utils.*;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private XfUtil xfUtil;

    @Resource
    private MinioUtils minioUtils;

    @Resource
    private ReduceCodeUtils reduceCodeUtils;

    @Resource
    private PasswordUtils passwordUtils;

    @Resource
    private XfApiService xfApiService;

    //查询用户个人信息
    @Override
    public UserInfoVo getUserInfo() {
        //获取登录用户信息
        UserInfoPo infoPo = reduceCodeUtils.getUserInfo();
        if (infoPo == null) {
            throw new BaseException("该用户未认证");
        }
        UserInfoVo infoVo = new UserInfoVo();
        BeanUtils.copyProperties(infoPo, infoVo);
        if (!StringUtils.isBlank(infoPo.getImageName())&&!infoPo.getImageName().equals("")){
            infoVo.setImage(minioUtils.preview(infoPo.getImageName()));
        }
        return infoVo;
    }

    //上传头像
    @Override
    public String uploadImage(MultipartFile file) {
        //获取登录用户信息
        UserInfoPo infoPo = reduceCodeUtils.getUserInfo();
        if (infoPo == null) {
            throw new BaseException("该用户未认证");
        }
        //上传头像
        String imageName = minioUtils.upload(file);
        //判断图片是否合规
//        xfApiService.SyncImageResult(imageName);
        //判断是否存在图片
        if (StringUtils.isBlank(infoPo.getImageName())||infoPo.getImageName().equals("")){
            //存在头像，删除minio中多余图片
            minioUtils.remove(infoPo.getImageName());
        }
        int result=userInfoMapper.uploadImage(imageName,infoPo.getUserId());
        String preview=minioUtils.preview(imageName);
        if (result!=1&&StringUtils.isBlank(preview)){
            throw new BaseException("上传失败");
        }
        return preview;
    }

    //用户修改密码
    @Override
    public boolean editPass(EditPassDto editPassDto) {
        String oldPass = editPassDto.getOldPass();
        String account = BaseUtils.getCurrentAccount();
        //判断密码是否正确
        passwordUtils.matchesPass(account,oldPass);
        //存储新密码
        String newPass = editPassDto.getNewPass();
        String encodedPass = passwordUtils.encodedPass(newPass);
        int result=0;
        Map<String, String> loginType = reduceCodeUtils.loginType();
        if (loginType.containsKey("phone")){
            result=userInfoMapper.editPassByPhone(encodedPass,loginType.get("phone"));
        }else if (loginType.containsKey("idNumber")){
            result=userInfoMapper.updatePassByIdNumber(encodedPass,loginType.get("idNumber"));
        }else {
            throw new BaseException("服务端错误");
        }
        if (result==0){
            throw new BaseException("更新密码失败");
        }
        return result==1;
    }

    //身份证识别并存入用户个人信息
    @Override
    public boolean identifyIdNumber(MultipartFile file) {
        UserInfoPo userInfo = reduceCodeUtils.getUserInfo();
        if (userInfo.getName()!=null){
            throw new BaseException("已认证");
        }
        //进行身份证识别
        UserDto userDto = xfUtil.IdNumberResult(file);
        Map<String, String> loginType = reduceCodeUtils.loginType();
        int result=0;
        if (loginType.containsKey("phone")){
            System.out.println(userDto);
            userDto.setAccount(loginType.get("phone"));
            //存入数据库
            result = userInfoMapper.updateUserInfo(userDto);
        }else if (loginType.containsKey("IdNumber")){
            throw new BaseException("登录错误");
        }else{
            throw new BaseException("登录错误");
        }

        if (result != 1) {
            throw new BaseException("认证失败");
        }
        return result == 1;
    }

    //获取用户个人信息
    @Override
    public List<String> getAuthorities() {
        User user = AuthenticateGetUserUtils.getUser();
        return userInfoMapper.getAuthorities(user.getUserId());
    }


}
