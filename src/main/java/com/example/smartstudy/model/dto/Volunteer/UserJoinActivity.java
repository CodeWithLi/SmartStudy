package com.example.smartstudy.model.dto.Volunteer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.framework.qual.NoQualifierParameter;

import java.io.Serializable;

//用户已经参加了的活动
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinActivity implements Serializable {
    private String userId;
    private String name;
    private String phone;
    private String idNumber;
    private String volunteerActivityId;
    private VolunteerActivityBaseInfo baseInfo ;
}
