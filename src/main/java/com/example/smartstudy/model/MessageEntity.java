package com.example.smartstudy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {

    // 发送者的 id
    private Long from;
    // 接受者的 id
    private Long to;
    // 具体信息
    private String message;
    // 发送时间
    private Date time;

}
