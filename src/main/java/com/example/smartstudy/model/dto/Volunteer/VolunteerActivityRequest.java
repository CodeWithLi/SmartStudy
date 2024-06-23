package com.example.smartstudy.model.dto.Volunteer;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerActivityRequest implements Serializable {
    @NotNull(message = "参数不为空")
    private String volunteerActivityId;
    //开始时间
    @NotNull(message = "参数不为空")
    @JsonFormat(pattern = "yyyy-MM-dd hh-mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm")
    private Date activityTime;
    //地点
    @NotBlank(message = "参数不为空")
    private String activityPlace;
    //标题
    @NotBlank(message = "参数不为空")
    @Size(max = 20,message = "标题最多为20个字符")
    private String activityTitle;
    //活动概述
    @Size(max = 100,message = "活动概述最多为100个字符")
    private String activitySummary;
    //需求量
    @NotNull(message = "参数不为空")
    private Integer activityDemand;
    //活动内容
    @NotBlank(message = "参数不为空")
    private String activityContent;
    //参加方式
    @NotBlank(message = "参数不为空")
    private String activityMethod;
}
