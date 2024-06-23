package com.example.smartstudy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XfDialogueAnswerVo {
    //返回对话答案
    private String answer;
    //返回该对话uid
    private String uid;
    //返回该对话名称
    private String dialogueName;
}
