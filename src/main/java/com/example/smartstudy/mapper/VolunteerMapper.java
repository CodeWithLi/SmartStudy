package com.example.smartstudy.mapper;

import com.example.smartstudy.model.dto.Volunteer.UserJoinActivity;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityBaseInfo;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityDetails;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityRequest;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewApplicationRequest;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewInfo;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewRequest;

import java.util.List;

public interface VolunteerMapper {

    //查询活动基本信息
    List<VolunteerActivityBaseInfo> allActivityBaseInfo();

    //查询某活动的具体信息
    VolunteerActivityDetails getActivity(String activityId);

    //管理员添加活动
    int insertActivity(VolunteerActivityDetails activityDetails);

    //存储图片
    int uploadImage(String upload, String activityId);

    //修改活动信息
    int updateActivity(VolunteerActivityRequest activityRequest);

    //删除活动
    int deleteActivity(String activityId);

    //判断用户是否存在志愿报名表中
    int hasApplicationActivity(String volunteerActivityId, String userId);

    //将报名数据存储进数据库
    int insertApplicationActivity(ReviewInfo info);

    //查询所有待审核活动
    List<ReviewInfo> allReview();

//    //审核成功
//    int successReview(ReviewRequest request);
//
//    //审核失败
//    int failReview(ReviewRequest request);

    //审核结果
    int resultReview(Integer code, String volunteerActivityId, String userId);

    //审核成功后，报名人数+1
    int addActivityNumber(String activityId);

    //审核通过后，注册到数据库表中
    int successSignup(ReviewApplicationRequest applicationRequest);

    //获取用户已报名的全部活动
    List<UserJoinActivity> joinedActivity(String userId);

}
