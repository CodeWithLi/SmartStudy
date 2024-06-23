package com.example.smartstudy.model.dto.Volunteer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// 志愿活动详细信息
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerActivityDetails implements Serializable {
    //活动内容
    private String activityContent;
    //参加方式
    private String activityMethod;
    //管理员联系方式
    private String activityAdminConnection;
    //基本信息
    private VolunteerActivityBaseInfo activityBaseInfo;
}
