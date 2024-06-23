package com.example.smartstudy.controller;

import com.example.smartstudy.common.Result;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.model.Dialogue;
import com.example.smartstudy.model.XfStreamRequest;
import com.example.smartstudy.model.vo.XfDialogueAnswerVo;
import com.example.smartstudy.model.vo.XfDialogueName;
import com.example.smartstudy.service.SmartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/smart")
@Slf4j
/**
 * 智能功能
 */
public class SmartController {


    @Autowired
    private SmartService smartService;

    /**
     * 发送问题
     *
     * @param xfStreamRequest
     * @return
     */
    @PostMapping("/sendQuestion")
    public Result sendQuestion(@RequestBody XfStreamRequest xfStreamRequest) {
        XfDialogueAnswerVo vo = smartService.sendQuestion(xfStreamRequest);
        return Result.success(vo);
    }


    /**
     * 全部对话的名称
     *
     * @param
     * @return
     */
    @GetMapping("/allDialogues")
    public Result getDialoguesName() {
        List<XfDialogueName> vos = smartService.getDialoguesName();
        return Result.success(vos);
    }


    /**
     * 某次对话聊天记录
     *
     * @param uid
     * @return
     */
    @GetMapping("/oneDialogue")
    public Result getOneDialogues(@RequestParam String uid) {
        if (StringUtils.isBlank(uid)) {
            throw new BaseException("请求参数错误");
        }
        List<Dialogue> oneDialogue = smartService.getOneDialogues(uid);
        return Result.success(oneDialogue);
    }

    //身份证认证 文本合规工具类 图片合规工具类 图片理解工具类 大模型
    @PostMapping("/idcard/authentication")
    public Result idcardAuthentication(@RequestParam MultipartFile file) {
        if (file==null){
            throw new BaseException("请求参数错误");
        }
        String preview=smartService.idcardAuthentication(file);
        return Result.success(preview);
    }

    //文本合规
    @PostMapping("/text/sync")
    public Result textSync() {
        return null;
    }

    //图片合规
    @PostMapping("/image/sync")
    public Result imageSync() {
        return null;
    }

    //图片理解
    @PostMapping("/image/comprehend")
    public Result imageComprehend() {
        return null;
    }

}
