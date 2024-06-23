package com.example.smartstudy.utils;

import cn.hutool.core.util.EnumUtil;
import com.example.smartstudy.constant.RedisKeyPrefixConstant;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.mapper.UserMapper;
import com.example.smartstudy.model.dto.Security.Register.VerificationSmsType;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class VerificationSmsUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    public void VerificationSms(String purpose,String phone,String verificationSms){
        boolean result=false;
        try{
            result=stringRedisTemplate.opsForValue().get(RedisKeyPrefixConstant.KeyPrefix+purpose+phone).equals(verificationSms);
        }catch (Exception e){
            throw new BaseException("该手机号验证码不存在");
        }
        if (result){
            //验证通过，删除验证码
            stringRedisTemplate.delete((RedisKeyPrefixConstant.KeyPrefix+purpose+phone));
            return;
        }
        throw new BaseException("请求错误");
    }
}
