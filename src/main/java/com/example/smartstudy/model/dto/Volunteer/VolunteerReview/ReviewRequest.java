package com.example.smartstudy.model.dto.Volunteer.VolunteerReview;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
//志愿活动审核
public class ReviewRequest implements Serializable {
    @NotBlank(message = "报名活动不为空")
    private String volunteerActivityId;
    private String userId;
    private String purpose;
}
