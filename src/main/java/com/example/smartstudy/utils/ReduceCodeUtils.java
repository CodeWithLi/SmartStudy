package com.example.smartstudy.utils;

import com.example.smartstudy.constant.RedisKeyPrefixConstant;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.mapper.UserInfoMapper;
import com.example.smartstudy.model.dto.RegisterEditDto;
import com.example.smartstudy.model.dto.Security.AuthenticateGetUserUtils;
import com.example.smartstudy.model.dto.Security.User;
import com.example.smartstudy.model.po.UserInfoPo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 简化代码重复使用
 */
@Component
public class ReduceCodeUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserInfoMapper userInfoMapper;
    //判断验证码
    public void UserRegisterEditReduce(String account,String purpose,String code , String purposeByApi){
        if (!account.matches("^1[3-9]\\d{9}$")){
            throw new BaseException("手机号格式错误");
        }
        if (!purposeByApi.equals(purpose)){
            throw new BaseException("请求参数错误");
        }
        if (code==null){
            throw new BaseException("验证码未空");
        }
        if (!Boolean.TRUE.equals(stringRedisTemplate.hasKey(RedisKeyPrefixConstant.KeyPrefix + purpose + account))){
            throw new BaseException("请先发送短信");
        }
        //从redis中获取验证码
        String redisCode = stringRedisTemplate.opsForValue().get(RedisKeyPrefixConstant.KeyPrefix + purpose + account);
        //判断验证码是否一致
        if (!Objects.equals(code, redisCode)){
            throw new BaseException("验证码错误");
        }
    }

    //获取登录用户信息
    public UserInfoPo getUserInfo(){
        //根据拦截器获取
        User user = AuthenticateGetUserUtils.getUser();
//        String account = BaseUtils.getCurrentAccount();

        UserInfoPo infoPo = userInfoMapper.selectInfoByUserId(user.getUserId());
        //判断是根据哪种方式登录
//        if (user.getIdNumber().matches("^\\d{18}$")) {
//            //根据身份证获取个人信息
//            infoPo = userInfoMapper.selectInfoByIdNumber(user.getIdNumber());
//        } else if (user.getPhone().matches("^1[3-9]\\d{9}$")) {
//            //手机号登录，判断数据库是否存在信息
//            infoPo = userInfoMapper.selectInfoByPhone(user.getPhone());
//        } else {
//            throw new BaseException("登录出错");
//        }
        return infoPo;
    }

    //判断为手机号登录还是身份证登录
    public Map<String,String> loginType(){
        Map<String,String> type=new HashMap<>();
        //根据拦截器获取
        String account = BaseUtils.getCurrentAccount();
        //判断是根据哪种方式登录
        if (account.matches("^\\d{18}$")) {
            //根据身份证获取个人信息
            type.put("IdNumber",account);
             return type;
        } else if (account.matches("^1[3-9]\\d{9}$")) {
            //手机号登录，判断数据库是否存在信息
            type.put("phone",account);
            return type;
        } else {
            throw new BaseException("登录出错");
        }
    }
}
