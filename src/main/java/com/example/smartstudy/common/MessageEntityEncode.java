package com.example.smartstudy.common;

import com.example.smartstudy.model.MessageEntity;
import com.google.gson.GsonBuilder;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

public class MessageEntityEncode implements Encoder.Text<MessageEntity> {

    @Override
    public String encode(MessageEntity messageEntity) {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
                .toJson(messageEntity);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}

}
