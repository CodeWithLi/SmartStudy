package com.example.smartstudy.service;

import com.example.smartstudy.model.Dialogue;
import com.example.smartstudy.model.XfStreamRequest;
import com.example.smartstudy.model.vo.XfDialogueAnswerVo;
import com.example.smartstudy.model.vo.XfDialogueName;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface SmartService {
    public XfDialogueAnswerVo sendQuestion(XfStreamRequest xfStreamRequest);

    //获取所有对话名称
    List<XfDialogueName> getDialoguesName();

    //获取某次对话的全部聊天记录
    List<Dialogue> getOneDialogues(String uid);

    //身份证认证
    String idcardAuthentication(MultipartFile file);
}
