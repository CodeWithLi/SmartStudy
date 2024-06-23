package com.example.smartstudy.service;

import com.example.smartstudy.model.dto.Volunteer.UserJoinActivity;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityBaseInfo;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityDetails;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityRequest;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewInfo;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CacheConfig(cacheNames = "Volunteer")
public interface VolunteerService {

    @Cacheable(key = "'activities'")
    List<VolunteerActivityBaseInfo> allActivity();

    //查询某活动的具体信息
    VolunteerActivityDetails getActivity(String activityId);

    //添加活动
    @CacheEvict(key = "'advertise'",allEntries = true)
    boolean addActivity(VolunteerActivityRequest activityRequest);

    //上传图片
    @CacheEvict(key = "'advertise'",allEntries = true)
    String uploadImage(MultipartFile file, String activityId);

    //修改活动信息
    @CacheEvict(key = "'advertise'",allEntries = true)
    boolean editActivity(VolunteerActivityRequest activityRequest);

    //删除某项活动
    @CacheEvict(key = "'advertise'",allEntries = true)
    boolean removeActivity(String activityId);

    //查询所有需审核的活动
    @Cacheable(key = "'reviews'")
    List<ReviewInfo> allReview();

    @CacheEvict(key = "'reviews'",allEntries = true)
    //审核结果（通过，未通过）
    boolean reviewStatus(ReviewRequest request);

    //用户报名
    @CacheEvict(key = "'reviews'",allEntries = true)
    boolean applicationActivity(ReviewRequest request);

    //获取用户已报名的全部活动
    List<UserJoinActivity> joinedActivity();

}
