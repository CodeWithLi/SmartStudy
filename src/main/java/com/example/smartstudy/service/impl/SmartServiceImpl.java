package com.example.smartstudy.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.EnumUtil;
import com.example.smartstudy.constant.UserRole;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.mapper.SmartMapper;
import com.example.smartstudy.model.Dialogue;
import com.example.smartstudy.model.XfStreamRequest;
import com.example.smartstudy.model.dto.Security.AuthenticateGetUserUtils;
import com.example.smartstudy.model.dto.Security.User;
import com.example.smartstudy.model.dto.UserDto;
import com.example.smartstudy.model.dto.XfDialogue;
import com.example.smartstudy.model.vo.XfDialogueAnswerVo;
import com.example.smartstudy.model.vo.XfDialogueName;
import com.example.smartstudy.properties.XfApiProperties;
import com.example.smartstudy.service.SmartService;
import com.example.smartstudy.utils.MinioUtils;
import com.example.smartstudy.utils.XfApi.XfIdNumberUtil;
import com.example.smartstudy.utils.XfApi.XfStreamAnswer;
import com.example.smartstudy.utils.XfApi.XfXhStreamUtils;
import com.example.smartstudy.utils.XfUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class SmartServiceImpl implements SmartService {

    @Resource
    private XfXhStreamUtils xfXhStreamClient;

    @Resource
    private XfApiProperties xfXhConfig;

    @Resource
    private XfUtil xfUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private XfStreamAnswer xfStreamAnswer;

    @Resource
    private SmartMapper smartMapper;

    @Resource
    private MinioUtils minioUtils;

    //发送问题
    @Override
    public XfDialogueAnswerVo sendQuestion(XfStreamRequest xfStreamRequest) {
        System.out.println(xfStreamRequest);
        String question = xfStreamRequest.getQuestion();
        //获取用户id
        User user = AuthenticateGetUserUtils.getUser();
        //传建对象存储用户提问以及模型回答的问题，存储到redis
        XfDialogue xfDialogue = new XfDialogue();
        xfDialogue.setUserId(user.getUserId());
        xfDialogue.setQuestion(question);

        //根据uid判断用户是否是第一次提问,第一次询问返回uid
        if (xfStreamRequest.isNewRequest()) {
            if (!"".equals(xfStreamRequest.getUid())) {
                throw new BaseException("请求参数错误，请联系管理员");
            }
            //第一次提问，历史记录为空，存储最新历史对话
            xfDialogue.setHistory(new ArrayList<>());
            xfDialogue.setUid(UUID.randomUUID().toString().substring(10));
            //第一次提问，让讯飞大模型帮我们缩短用户第一次提问，作为对话名称
            String historyName = "帮我缩短为最多七个字："+question;
            // 响应大模型的答案
            String historyNameAnswer = xfStreamAnswer.answer(historyName, xfDialogue.getUid());
            xfDialogue.setOneDialogueName(historyNameAnswer);
            //存储名称到数据库
            //查询用户有几条对话在数据库
            Integer count=smartMapper.queryCount(user.getUserId());
            //存储对话
            int result=smartMapper.insertDialogueName(UUID.randomUUID().toString() ,user.getUserId(),xfDialogue.getUid(),historyNameAnswer,count+1);
            if (result!=1){
                throw new BaseException("请求错误");
            }
        } else {
            //不是第一次提问,先取出历史记录,删除原先对话
            //存储uid
            xfDialogue.setUid(xfStreamRequest.getUid());
            xfDialogue.setHistory((List<Dialogue>) redisTemplate.opsForValue().get("userDialogue:" + user.getUserId() + xfStreamRequest.getUid()));
            //删除该历史记录
            redisTemplate.delete("userDialogue:" + user.getUserId());
        }
        //将id，uid，对话名称存储到数据库
        // 响应大模型的答案
        String answer = xfStreamAnswer.answer(question, xfDialogue.getUid());
        //将大模型以及用户问的存到redis
        xfDialogue.setAnswer(answer);
        xfDialogue.storeDialogue();

        //存储新历史集合
        xfDialogue.dialogueHistory();
        redisTemplate.opsForValue().set("userDialogue:"+user.getUserId()+xfDialogue.getUid(),xfDialogue.getHistory());

        XfDialogueAnswerVo vo = new XfDialogueAnswerVo();
        vo.setAnswer(answer);
        vo.setUid(xfDialogue.getUid());
        vo.setDialogueName(xfDialogue.getOneDialogueName());
        return vo;
    }

    //获取所有对话名称
    @Override
    public List<XfDialogueName> getDialoguesName() {
        User user = AuthenticateGetUserUtils.getUser();
        List<XfDialogueName> xfDialogueNames=smartMapper.getDialoguesName(user.getUserId());
        return xfDialogueNames;
    }

    //获取某次对话的全部聊天记录
    @Override
    public List<Dialogue> getOneDialogues(String uid) {
        User user = AuthenticateGetUserUtils.getUser();
        List<Dialogue> history = new ArrayList<>();
        if (redisTemplate.hasKey("userDialogue:"+user.getUserId()+uid)){
            history= (List<Dialogue>) redisTemplate.opsForValue().get("userDialogue:"+user.getUserId()+uid);
        }
        return history;
    }

    //身份证认证
    @Override
    @PreAuthorize("hasAuthority('no_authentication')")
    public String idcardAuthentication(MultipartFile file) {
        //获取当前用户信息
        User user = AuthenticateGetUserUtils.getUser();

        UserDto userDto = xfUtil.IdNumberResult(file);
        userDto.setUserId(user.getUserId());
        userDto.setBelongStatus(EnumUtil.toString(UserRole.user));
        //插入认证信息
        int userResult=smartMapper.updateUserInfo(userDto);
        //修改用户role角色
        Integer roleId=UserRole.user.getCode();
        int roleResult= smartMapper.updateRoleInfo(roleId,user.getUserId());
        if (userResult!=1&&roleResult!=1){
            throw new BaseException("请求错误");
        }
        return "认证成功";

    }

}
