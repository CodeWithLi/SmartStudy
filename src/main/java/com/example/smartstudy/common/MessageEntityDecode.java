package com.example.smartstudy.common;

import com.example.smartstudy.model.MessageEntity;
import com.google.gson.GsonBuilder;
import jakarta.websocket.Decoder;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import org.springframework.stereotype.Component;

@Component
public class MessageEntityDecode implements Decoder.Text<MessageEntity> {

    @Override
    public MessageEntity decode(String s) {
        // 利用 gson 处理消息实体，并格式化日期格式
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
                .fromJson(s, MessageEntity.class);
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}

}



