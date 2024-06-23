package com.example.smartstudy.model.dto.Volunteer.VolunteerReview;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewApplicationRequest implements Serializable {
    private String volunteerSignupId;
    private String userId;
    private String volunteerActivityId;
    private Date volunteerSignupTime;
//    private Integer status;
}
