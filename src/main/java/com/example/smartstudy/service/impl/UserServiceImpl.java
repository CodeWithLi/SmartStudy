package com.example.smartstudy.service.impl;

import cn.hutool.core.util.EnumUtil;
import com.apistd.uni.UniResponse;
import com.example.smartstudy.common.Result;
import com.example.smartstudy.constant.RedisKeyPrefixConstant;
import com.example.smartstudy.constant.UserRole;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.mapper.UserMapper;
import com.example.smartstudy.model.dto.RegisterEditDto;
import com.example.smartstudy.model.dto.Security.AuthenticateGetUserUtils;
import com.example.smartstudy.model.dto.Security.Lgoin.LoginRequest;
import com.example.smartstudy.model.dto.Security.Register.RegisterEditRequest;
import com.example.smartstudy.model.dto.Security.Register.VerificationSmsType;
import com.example.smartstudy.model.dto.Security.User;
import com.example.smartstudy.model.dto.UserDto;
import com.example.smartstudy.model.po.UserInfoPo;
import com.example.smartstudy.model.po.UserPo;
import com.example.smartstudy.model.vo.LoginVo;
import com.example.smartstudy.service.UserService;
import com.example.smartstudy.utils.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private PasswordUtils passwordUtils;

    @Autowired
    private ReduceCodeUtils reduceCodeUtils;

    @Autowired
    private XfUtil xfUtil;


    //用户登录
    @Override
    public UserDto loginUser(String account, String password, String code, String purpose) {
        if (!StringUtils.isBlank(password) && !password.equals("")) {
            //账号+密码登录
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(account, password);
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        }
        UserDto userDto = new UserDto();
        if (account.matches("^1[3-9]\\d{9}$") && !StringUtils.isBlank(code)) {
            //手机号+验证码登录
            //判断用户是否存在,根据手机号查询
            UserPo userPo = userMapper.selectUserByPhone(account);
            if (userPo == null) {
                throw new BaseException("用户未注册");
            }
            String purposeByApi = "login";
            reduceCodeUtils.UserRegisterEditReduce(account, purpose, code, purposeByApi);
            userDto = new UserDto();
            BeanUtils.copyProperties(userPo, userDto);
            userDto.setAccountType("phone");
        } else if (account.matches("^1[3-9]\\d{9}$") && !StringUtils.isBlank(password)) {
            //手机号+密码登录
            //判断用户是否存在
            UserPo userPo = userMapper.selectUserByPhone(account);
            if (userPo == null) {
                throw new BaseException("用户未注册");
            }
            passwordUtils.matchesPass(account, password);
            userDto = new UserDto();
            BeanUtils.copyProperties(userPo, userDto);
            userDto.setAccountType("phone");
        } else if (account.matches("^\\d{18}$") && !StringUtils.isBlank(password)) {
            //身份证+密码登录
            UserPo userPo = userMapper.selectUserByIdNumber(account);
            if (userPo == null) {
                throw new BaseException("用户未注册");
            }
            passwordUtils.matchesPass(account, password);
            userDto = new UserDto();
            BeanUtils.copyProperties(userPo, userDto);
            userDto.setAccountType("idNumber");
        } else {
            throw new BaseException("登录错误");
        }
        userDto.setAccount(account);
        return userDto;
    }

    //注册用户
    @Override
    public boolean registerUser(RegisterEditDto registerEditDto) {
        //确保只有一次注册
        synchronized (registerEditDto.getPhone()) {
            //判断用户是否已经注册
            UserPo userPo = userMapper.selectUserByPhone(registerEditDto.getPhone());
            if (userPo != null) {
                throw new BaseException("用户已注册");
            }

            //判断用户是否合规，调用讯飞文本合规进行判断
            String content = registerEditDto.getUsername();
            String resultByContent = xfUtil.SyncTextResult(content);
            if (!resultByContent.equals("pass") && !resultByContent.equals("block")) {
                throw new BaseException("用户名错误");
            } else if (resultByContent.equals("block")) {
                throw new BaseException("用户名违规");
            }

            String purposeByApi = "register";
            reduceCodeUtils.UserRegisterEditReduce
                    (registerEditDto.getPhone(), registerEditDto.getPurpose(), registerEditDto.getCode(), purposeByApi);
            //加密密码
            registerEditDto.setPassword(passwordUtils.encodedPass(registerEditDto.getPassword()));
            //向数据库中存储用户
            int result = userMapper.addUser(registerEditDto);
            if (result != 1) {
                throw new BaseException("注册失败");
            }
            return true;
        }
    }

    //修改密码
    @Override
    public boolean editPassUser(RegisterEditDto registerEditDto) {
        //判断用户是否已经注册
        UserPo userPo = userMapper.selectUserByPhone(registerEditDto.getPhone());
        if (userPo == null) {
            throw new BaseException("用户未注册");
        }
        String purposeByApi = "edit";
        reduceCodeUtils.UserRegisterEditReduce
                (registerEditDto.getPhone(), registerEditDto.getPurpose(), registerEditDto.getCode(), purposeByApi);
        //加密密码
        registerEditDto.setPassword(passwordUtils.encodedPass(registerEditDto.getPassword()));
        int result = userMapper.editPass(registerEditDto);
        if (result != 1) {
            throw new BaseException("修改密码失败");
        }
        return true;
    }

    //    //身份证识别并存入用户个人信息
    @Override
    public boolean identifyIdNumber(MultipartFile file) {
        UserInfoPo userInfo = reduceCodeUtils.getUserInfo();
        if (userInfo.getName() != null) {
            throw new BaseException("已认证");
        }
        //进行身份证识别
        UserDto userDto = xfUtil.IdNumberResult(file);
        Map<String, String> loginType = reduceCodeUtils.loginType();
        int result = 0;
        if (loginType.containsKey("phone")) {
            //存入数据库
            result = userMapper.updateUser(userDto, loginType.get("phone"));
        } else if (loginType.containsKey("IdNumber")) {
            throw new BaseException("登录错误");
        } else {
            throw new BaseException("登录错误");
        }

        if (result != 1) {
            throw new BaseException("认证失败");
        }
        return result == 1;
    }

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JwtUtils jwtUtils;

    //security登录
    @Override
    public LoginVo loginSecurity(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = null;
        //通过判断枚举类型，判断为哪种登录方式
        switch (loginRequest.getLoginType()) {
            case phone -> authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getPhone(), loginRequest.getPassword());
            case idNumber -> authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getIdNumber(), loginRequest.getPassword());
            case admin -> authenticationToken=
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword());
        }
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new BaseException("密码错误");
        }
        //authenticate通过验证已经将用户信息保存在线程SecurityContextHolder中
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        //获取用户信息
        User user = AuthenticateGetUserUtils.getUser();

        //由于已经在userDetailsService里判断user是否为空，故可直接取值

        //判断用户信息是否已经存在redis中，如存在则删除
        redisTemplate.delete("user:" + user.getUserId());

        //将用户信息存入redis中
        redisTemplate.opsForValue().set("user+" + user.getUserId(), user, 12, TimeUnit.HOURS);
        String token =null;

        //判断redis中是否存在token，有则取出，无则存入并生成
        if (Boolean.TRUE.equals(redisTemplate.hasKey("userToken+" + user.getUserId()))){
            token= (String) redisTemplate.opsForValue().get("userToken+"+user.getUserId());
        }else {
            token=jwtUtils.createUserIdJwt(user.getUserId(), String.valueOf(loginRequest.getLoginType()),user.getAuthorities());
            redisTemplate.opsForValue().set("userToken+"+user.getUserId(),token,12,TimeUnit.HOURS);
        }
        return new LoginVo(token, user.getBelongStatus(),user.getAuthorities());
    }

    @Resource
    private VerificationSmsUtils verificationSmsUtils;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private XfApiService xfApiService;

    //发送手机短信
    @Override
    public String sendSmsCode(String phone, String purpose) {
        //先判断手机号是否符合
        //判断缓存中是否存在验证码
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(RedisKeyPrefixConstant.KeyPrefix + purpose + phone))) {
            throw new BaseException("验证码未过期,请勿重复发送");
        }
        //生成随机4位数
        Random random = new Random();
        String randomCode = String.valueOf(random.nextInt(1000, 9999));
//        UniResponse uniResponse = smsUtils.sendSms(phone, randomCode);
//        if (uniResponse.status!=200){
//            throw new BaseException("验证码发送失败");
//        }
        stringRedisTemplate.opsForValue().set(RedisKeyPrefixConstant.KeyPrefix + purpose + phone, randomCode, 3, TimeUnit.MINUTES);
        return randomCode;
    }


    //security注册
    @Override
    public boolean registerSecurityUser(RegisterEditRequest request) {
            //已经校验了purpose是否符合，故下面添加用户时不需要判断
            isExitUser(request.getPurpose(),request.getPhone());
            //校验验证码
            verificationSmsUtils.VerificationSms(request.getPurpose(), request.getPhone(), request.getCode());

            //判断用户名是否合规
//            xfApiService.SyncTextResult(request.getUsername());

            //未认证用户
            request.setRole(EnumUtil.toString(UserRole.normalUser));
            request.setRoleId(UserRole.normalUser.getCode());
            //添加用户
            operationUser(request);

            //向role表添加该未认证用户
            insertRole(request);

            return true;
    }

    //security修改
    @Override
    public boolean editSecurityUser(RegisterEditRequest request) {
        //已经校验了purpose是否符合，故下面添加用户时不需要判断
        isExitUser(request.getPurpose(),request.getPhone());

        //校验验证码
        verificationSmsUtils.VerificationSms(request.getPurpose(), request.getPhone(), request.getCode());

        //判断用户名是否合规
        xfApiService.SyncTextResult(request.getUsername());

        //修改用户
        operationUser(request);

        return true;
    }

    //退出登录
    @Override
    public Result logout() {
        //获取认证
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user= (User) authentication.getPrincipal();
        User user= AuthenticateGetUserUtils.getUser();
        //根据用户id删除redis中的数据
        redisTemplate.delete("userToken+"+user.getUserId());
        redisTemplate.delete("user+" + user.getUserId());
        return Result.success("退出成功");
    }

    public void isExitUser(String purpose,String phone){
        if (!EnumUtil.contains(VerificationSmsType.class,purpose)){
            throw new BaseException("请求参数错误");
        }

        if (EnumUtil.equals(VerificationSmsType.register,purpose)){
            return;
        } else if (EnumUtil.equals(VerificationSmsType.edit, purpose)) {
            return;
        }
        throw new BaseException("用户账号异常");
    }

    public void operationUser(RegisterEditRequest request){
        User exitUser=userMapper.findUserByPhone(request.getPhone());
        if (exitUser!=null){
            throw new BaseException("用户已存在");
        }
        User user=new User();

        BeanUtils.copyProperties(request,user);

        String encode=bCryptPasswordEncoder.encode(request.getPassword());
        user.setPassword(encode);

        try {
            if (EnumUtil.equals(VerificationSmsType.register,request.getPurpose())){
               userMapper.insertUser(user);
//               insertRole(request);
            }else if (EnumUtil.equals(VerificationSmsType.edit,request.getPurpose())){
                userMapper.editUser(user);
            }
        }catch (Exception e){
         throw new BaseException("请求错误");
        }

    }

    public void insertRole(RegisterEditRequest request){
        if (userMapper.insertNormorlRole(request)!=1){
            throw new BaseException("注册失败");
        }
        return;
    }

}
