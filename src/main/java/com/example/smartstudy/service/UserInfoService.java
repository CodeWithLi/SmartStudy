package com.example.smartstudy.service;

import com.example.smartstudy.model.dto.EditPassDto;
import com.example.smartstudy.model.vo.UserInfoVo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CacheConfig(cacheNames = "UserInfo")
public interface UserInfoService {

    //查询用户个人信息
//    @Cacheable(key = "'userInfo'")
    UserInfoVo getUserInfo();

    //上传个人头像
//    @CacheEvict(key = "'userInfo'",allEntries = true)
    String uploadImage(MultipartFile file);

    //修改密码
//    @CacheEvict(key = "'userInfo'",allEntries = true)
    boolean editPass(EditPassDto editPassDto);

    boolean identifyIdNumber(MultipartFile file);

    //获取权限
    List<String> getAuthorities();

}
