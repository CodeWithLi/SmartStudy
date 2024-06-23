package com.example.smartstudy.model.dto.Volunteer.VolunteerReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewInfo implements Serializable {
    private String reviewId;
    private String volunteerActivityId;
    private String activityTitle;
    private String activityImageName;
    private String activityImage;
    private String userId;
    private String userName;
    private String userPhone;
    private String userIdNumber;
    private Integer reviewStatus;
}
