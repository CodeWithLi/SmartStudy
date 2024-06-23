package com.example.smartstudy.model.dto.Volunteer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerActivityBaseInfo implements Serializable {
    //活动id
    private String volunteerActivityId;
    //开始时间
    @JsonFormat(pattern = "yyyy-MM-dd hh-mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm")
    private Date activityTime;
    //地点
    private String activityPlace;
    //标题
    private String activityTitle;
    //活动概述
    private String activitySummary;

    //封面文件
    private String activityImageName;
    //活动封面
    private String activityImage;

    //已参加人数
    private Integer activityNumber;
    //需求量
    private Integer activityDemand;
}
