package com.example.smartstudy.model.dto;

import com.example.smartstudy.model.Dialogue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XfDialogue implements Serializable {
    //对话中用户的身份id
    private String uid;
    //用户id
    private String userId;
    //用户提问
    private String question;
    //大模型回答
    private String answer;
    //判断是否是第一次询问
    private boolean isOneDialogue=true;
    //一次对话
    public final Dialogue oneDialogue = new Dialogue();
    //一次对话名称
    private String oneDialogueName;
    //存储所有记录
    public List<Dialogue> history;

    public void storeDialogue(){
        this.oneDialogue.setQuestion(this.getQuestion());
        this.oneDialogue.setAnswer(this.getAnswer());
    }

    public void dialogueHistory(){
        history.add(this.oneDialogue);
    }
}
