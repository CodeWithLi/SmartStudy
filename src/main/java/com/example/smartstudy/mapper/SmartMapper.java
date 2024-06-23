package com.example.smartstudy.mapper;

import com.example.smartstudy.model.dto.UserDto;
import com.example.smartstudy.model.vo.XfDialogueName;

import java.util.List;

public interface SmartMapper {

    //查询用户有几条对话数据
    Integer queryCount(String userId);

    //存储对话名称
    int insertDialogueName(String dialogueId, String userId, String uid, String historyNameAnswer, int place);

    //获取对话名称
    List<XfDialogueName> getDialoguesName(String userId);

    //插入身份证信息
    int updateUserInfo(UserDto userDto);

    //修改用户role角色
    int updateRoleInfo(Integer roleId, String userId);
}
