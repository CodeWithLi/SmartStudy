package com.example.smartstudy.model.dto.Volunteer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//用户报名请求
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerApplicationRequest implements Serializable {
    private String reviewId;
    @NotBlank(message = "广告id不为空")
    private String volunteerActivityId;
    @NotBlank(message = "报名者姓名不为空")
    private String userName;
    @NotBlank(message = "报名者手机号不为空")
    private String userPhone;
    @NotBlank(message = "报名者身份证号不为空")
    private String userIdNumber;

}