package com.example.smartstudy.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskVo {
    private String taskId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd hh-mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm")
    private Date endTime;
    private Integer status;

}
