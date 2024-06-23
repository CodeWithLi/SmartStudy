package com.example.smartstudy.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class TaskPo {
    private String taskId;
    private String content;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd hh-mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd hh-mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm")
    private Date endTime;
    private Integer status;

}
