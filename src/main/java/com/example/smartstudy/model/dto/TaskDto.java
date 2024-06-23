package com.example.smartstudy.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
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
