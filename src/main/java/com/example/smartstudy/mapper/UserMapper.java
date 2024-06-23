package com.example.smartstudy.mapper;

import com.example.smartstudy.model.dto.RegisterEditDto;
import com.example.smartstudy.model.dto.Security.Register.RegisterEditRequest;
import com.example.smartstudy.model.dto.Security.User;
import com.example.smartstudy.model.dto.UserDto;
import com.example.smartstudy.model.po.UserPo;

import java.util.List;

public interface UserMapper {

    //判断用户是否存在
    UserPo selectUserById(String phone);

    //添加用户
    int addUser(RegisterEditDto registerEditDto);

    //根据用户手机号查询用户
    UserPo selectUserByPhone(String phone);

    //根据用户身份证查询密码
    UserPo selectUserByIdNumber(String idNumber);

    //修改密码
    int editPass(RegisterEditDto registerEditDto);

    //存入个人信息
    int updateUser(UserDto userDto, String phone);

    //security根据手机号查询个人信息
    User findUserByPhone(String phone);

    //security根据手机号查询个人信息
    User findUserByIdNumber(String idNumber);

    //管理员登录
    User findUserByUsername(String username);

    //添加用户
    void insertUser(User user);

    //插入role未认证用户
    int insertNormorlRole(RegisterEditRequest request);

    //修改密码
    void editUser(User user);

    //向role表插入权限对应的用户
    int insertRole(String name, String role_key);

    //向menu表插入对应权限
    int insertmenu(String name, String prem_key);

    //查询用户对应权限
    List<String> selectUserPermission(String userId);
}
