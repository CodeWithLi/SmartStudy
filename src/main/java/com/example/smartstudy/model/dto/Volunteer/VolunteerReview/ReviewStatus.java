package com.example.smartstudy.model.dto.Volunteer.VolunteerReview;

public enum ReviewStatus {
    review(0),
    success(1),
    fail(-1);

    private Integer code;

    private ReviewStatus(Integer code){
        this.code=code;
    }

    public Integer getCode(){
        return this.code;
    }
}
