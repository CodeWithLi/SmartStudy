package com.example.smartstudy.utils.XfApi;

import cn.hutool.core.util.StrUtil;
import com.example.smartstudy.listener.XfXhWebSocketListener;
import com.example.smartstudy.model.dto.MsgDTO;
import com.example.smartstudy.model.dto.Security.AuthenticateGetUserUtils;
import com.example.smartstudy.model.dto.Security.User;
import com.example.smartstudy.model.dto.XfDialogue;
import com.example.smartstudy.properties.XfApiProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@Slf4j
public class XfStreamAnswer {

    @Resource
    private XfXhStreamUtils xfXhStreamClient;

    @Resource
    private XfApiProperties xfXhConfig;

    public String answer(String question,String uid){
        // 如果是无效字符串，则不对大模型进行请求
        if (StrUtil.isBlank(question)) {
            return "无效问题，请重新输入";
        }
        // 获取连接令牌
        if (!xfXhStreamClient.operateToken(XfXhStreamUtils.GET_TOKEN_STATUS)) {
            return "当前大模型连接数过多，请稍后再试";
        }

        // 创建消息对象
        MsgDTO msgDTO = MsgDTO.createUserMsg(question);
        // 创建监听器
        XfXhWebSocketListener listener = new XfXhWebSocketListener();

        // 发送问题给大模型，生成 websocket 连接
        WebSocket webSocket = xfXhStreamClient.sendMsg(uid, Collections.singletonList(msgDTO), listener);
        if (webSocket == null) {
            // 归还令牌
            xfXhStreamClient.operateToken(XfXhStreamUtils.BACK_TOKEN_STATUS);
            return "系统内部错误，请联系管理员";
        }
        try {
            int count = 0;
            // 为了避免死循环，设置循环次数来定义超时时长
            int maxCount = xfXhConfig.getMaxResponseTime() * 5;
            while (count <= maxCount) {
                Thread.sleep(200);
                if (listener.isWsCloseFlag()) {
                    break;
                }
                count++;
            }
            if (count > maxCount) {
                return "大模型响应超时，请联系管理员";
            }
            return listener.getAnswer().toString();
        } catch (InterruptedException e) {
            log.error("错误：" + e.getMessage());
            return "系统内部错误，请联系管理员";
        } finally {
            // 关闭 websocket 连接
            webSocket.close(1000, "");
            // 归还令牌
            xfXhStreamClient.operateToken(XfXhStreamUtils.BACK_TOKEN_STATUS);
        }
    }
}
