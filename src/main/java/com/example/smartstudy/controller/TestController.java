package com.example.smartstudy.controller;

import cn.hutool.core.util.StrUtil;
import com.example.smartstudy.common.Result;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.listener.XfXhWebSocketListener;
import com.example.smartstudy.model.XfStreamRequest;
import com.example.smartstudy.model.dto.MsgDTO;
import com.example.smartstudy.model.dto.Security.AuthenticateGetUserUtils;
import com.example.smartstudy.model.dto.Security.User;
import com.example.smartstudy.model.dto.XfDialogue;
import com.example.smartstudy.model.dto.XfImageUnderstandDto;
import com.example.smartstudy.model.vo.XfDialogueAnswerVo;
import com.example.smartstudy.model.vo.XfDialogueName;
import com.example.smartstudy.properties.XfApiProperties;
import com.example.smartstudy.service.SmartService;
import com.example.smartstudy.service.impl.SmartServiceImpl;
import com.example.smartstudy.utils.XfApi.XfStreamAnswer;
import com.example.smartstudy.utils.XfApi.XfXhStreamUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 *
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {


    @Autowired
    private SmartService smartService;


    //todo 新建对话应返回对话名称

//    /**
//     * 发送问题
//     *
//     * @param question 问题
//     * @return 星火大模型的回答
//     */
//    @GetMapping("/sendQuestion")
//    public String sendQuestion(@RequestParam("question") String question) {
//        // 如果是无效字符串，则不对大模型进行请求
//        if (StrUtil.isBlank(question)) {
//            return "无效问题，请重新输入";
//        }
//        // 获取连接令牌
//        if (!xfXhStreamClient.operateToken(XfXhStreamUtils.GET_TOKEN_STATUS)) {
//            return "当前大模型连接数过多，请稍后再试";
//        }
//
//        // 创建消息对象
//        MsgDTO msgDTO = MsgDTO.createUserMsg(question);
//        // 创建监听器
//        XfXhWebSocketListener listener = new XfXhWebSocketListener();
//
//        //获取用户id
//        User user = AuthenticateGetUserUtils.getUser();
//        //传建对象存储用户提问以及模型回答的问题，存储到redis
//        XfDialogue xfDialogue=new XfDialogue();
//        xfDialogue.setUid(UUID.randomUUID().toString().substring(10));
//        xfDialogue.setUserId(user.getUserId());
//        xfDialogue.setQuestion(question);
//        // 发送问题给大模型，生成 websocket 连接
//        WebSocket webSocket = xfXhStreamClient.sendMsg(xfDialogue.getUid(), Collections.singletonList(msgDTO), listener);
//        if (webSocket == null) {
//            // 归还令牌
//            xfXhStreamClient.operateToken(XfXhStreamUtils.BACK_TOKEN_STATUS);
//            return "系统内部错误，请联系管理员";
//        }
//        try {
//            int count = 0;
//            // 为了避免死循环，设置循环次数来定义超时时长
//            int maxCount = xfXhConfig.getMaxResponseTime() * 5;
//            while (count <= maxCount) {
//                Thread.sleep(200);
//                if (listener.isWsCloseFlag()) {
//                    break;
//                }
//                count++;
//            }
//            if (count > maxCount) {
//                return "大模型响应超时，请联系管理员";
//            }
//            // 响应大模型的答案
//            //将大模型以及用户问的存到redis
//            xfDialogue.setAnswer(listener.getAnswer().toString());
//            xfDialogue.storeDialogue();
//
//            //存储最新历史对话
//            //先删除原先对话，再存入最新历史记录
//            if (Boolean.TRUE.equals(redisTemplate.hasKey("userDialogue:" + user.getUserId()))){
//                //取出历史记录
//                xfDialogue.setHistory((List<Map<String, String>>) redisTemplate.opsForValue().get("userDialogue:"+user.getUserId()));
//                //删除该历史记录
//                redisTemplate.delete("userDialogue:"+user.getUserId());
//            }else {
//                xfDialogue.setHistory(new ArrayList<>());
//            }
//            //存储新历史集合
//            xfDialogue.dialogueHistory();
//            redisTemplate.opsForValue().set("userDialogue:"+user.getUserId(),xfDialogue.getHistory());
//            return listener.getAnswer().toString();
//        } catch (InterruptedException e) {
//            log.error("错误：" + e.getMessage());
//            return "系统内部错误，请联系管理员";
//        } finally {
//            // 关闭 websocket 连接
//            webSocket.close(1000, "");
//            // 归还令牌
//            xfXhStreamClient.operateToken(XfXhStreamUtils.BACK_TOKEN_STATUS);
//        }
//        return null;
//    }

    //通过前端传送的数据判断是否是最新问题

    /**
     * 发送问题
     * @param xfStreamRequest
     * @return
     */
    @PostMapping("/sendQuestion")
    public Result sendQuestion(@RequestBody XfStreamRequest xfStreamRequest) {
        XfDialogueAnswerVo vo = smartService.sendQuestion(xfStreamRequest);
        return Result.success(vo);
    }

//    @GetMapping("/allDialogues")
//    public Result getDialoguesName(@RequestParam String userId){
//        if (StringUtils.isBlank(userId)){
//            throw new BaseException("请求参数错误");
//        }
//        List<XfDialogueName> vos= smartService.getDialoguesName(userId);
//        return Result.success(vos);
//    }
    /**
     * 全部对话的名称
     * @param userId
     * @return
     */
//


    /**
     * 某次对话聊天记录
     * @param uid
     * @return
     */
//    @GetMapping("/oneDialogue")
//    public Result getOneDialogues(@RequestParam String uid){
//        if (StringUtils.isBlank(uid)){
//            throw new BaseException("请求参数错误");
//        }
//        List<Map<String, String>> oneDialogue= smartService.getOneDialogues(uid);
//        return Result.success(oneDialogue);
//    }
}
