package com.example.smartstudy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//历史对话名称
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XfDialogueName {

    private String userId;
    private String uid;
    private String dialogueName;
    private String dialoguePlace;
}
