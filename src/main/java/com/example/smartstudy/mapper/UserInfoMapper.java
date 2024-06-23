package com.example.smartstudy.mapper;

import com.example.smartstudy.model.dto.UserDto;
import com.example.smartstudy.model.po.UserInfoPo;
import com.example.smartstudy.model.po.UserPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserInfoMapper {

    //根据手机号查询个人信息
    UserInfoPo selectInfoByPhone(String account);

    //存储用户身份证信息
    int updateUserInfo( UserDto userDto);

    //根据身份证查询个人信息
    UserInfoPo selectInfoByIdNumber(String account);

    //上传头像
    int uploadImage(String imageName, String userId);

    //根据手机号更改密码
    int editPassByPhone(String encodedPass, String phone);

    //根据身份证更改密码
    int updatePassByIdNumber(String encodedPass, String idNumber);

    //查询个人信息
    UserInfoPo selectInfoByUserId(String userId);

    //获取权限
    List<String> getAuthorities(String userId);
}
