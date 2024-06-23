package com.example.smartstudy.service.impl;

import cn.hutool.core.util.EnumUtil;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.mapper.VolunteerMapper;
import com.example.smartstudy.model.dto.Security.AuthenticateGetUserUtils;
import com.example.smartstudy.model.dto.Security.User;
import com.example.smartstudy.model.dto.Volunteer.UserJoinActivity;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityBaseInfo;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityDetails;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityRequest;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewApplicationRequest;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewInfo;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewRequest;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewStatus;
import com.example.smartstudy.service.VolunteerService;
import com.example.smartstudy.utils.MinioUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service

public class VolunteerServiceImpl implements VolunteerService {

    @Resource
    private VolunteerMapper volunteerMapper;

    @Resource
    private MinioUtils minioUtils;

    @Resource
    private XfApiService xfApiService;

    @Override
    public List<VolunteerActivityBaseInfo> allActivity() {
        //查询出所有任务的基本信息
        List<VolunteerActivityBaseInfo> list = volunteerMapper.allActivityBaseInfo();

        return list.stream().peek(baseInfo -> {
            if (!StringUtils.isBlank(baseInfo.getActivityImageName())) {
                String preview = minioUtils.preview(baseInfo.getActivityImageName());
                baseInfo.setActivityImage(preview);
            }
        }).collect(Collectors.toList());
    }

    //查询某活动的具体信息
    @Override
    public VolunteerActivityDetails getActivity(String activityId) {
        VolunteerActivityDetails activityDetails = volunteerMapper.getActivity(activityId);
        return activityDetails;

    }

    //管理员添加活动
    @Override
    public boolean addActivity(VolunteerActivityRequest activityRequest) {
//       初始化VolunteerActivityBaseInfo对象
        VolunteerActivityBaseInfo baseInfo = new VolunteerActivityBaseInfo();
        BeanUtils.copyProperties(activityRequest, baseInfo);

        VolunteerActivityDetails activityDetails = new VolunteerActivityDetails();
        BeanUtils.copyProperties(activityRequest, activityDetails);
        // 设置 baseInfo 到 activityDetails
        activityDetails.setActivityBaseInfo(baseInfo);

        //获取信息
        User user = AuthenticateGetUserUtils.getUser();
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //设置管理员信息
        activityDetails.setActivityAdminConnection(user.getPhone());
        //设置uuid
//        activityDetails.getActivityBaseInfo().setVolunteerActivityId(UUID.randomUUID().toString());
        //插入活动
        int result = volunteerMapper.insertActivity(activityDetails);
        return result == 1;
    }

    @Override
    public String uploadImage(MultipartFile file, String activityId) {
        VolunteerActivityDetails activity = volunteerMapper.getActivity(activityId);
        if (activity==null){
            throw new BaseException("该活动不存在");
        }
        String upload = minioUtils.upload(file);
//        xfApiService.SyncImageResult(upload);
        //向数据库存储
        int result= volunteerMapper.uploadImage(upload,activityId);
        String preview = minioUtils.preview(upload);
        if (result!=1&&StringUtils.isBlank(preview)){
            throw new BaseException("上传图片失败");
        }
        return preview;
    }

    @Override
    public boolean editActivity(VolunteerActivityRequest activityRequest) {
        int result = volunteerMapper.updateActivity(activityRequest);
        return result == 1;
    }

    //删除任务
    @Override
    public boolean removeActivity(String activityId) {
        int result = volunteerMapper.deleteActivity(activityId);
        return result == 1;
    }

    //查询所有需审核的活动
    @Override

    public List<ReviewInfo> allReview() {
        return volunteerMapper.allReview().stream().peek(reviewInfo -> {
            if (!StringUtils.isBlank(reviewInfo.getActivityImageName())){
                reviewInfo.setActivityImage(minioUtils.preview(reviewInfo.getActivityImageName()));
            }
        }).collect(Collectors.toList());
    }

    //审核结果
    @Override
    public boolean reviewStatus(ReviewRequest request) {
        //获取审核结果
        reviewStatusResult(request);
        if (StringUtils.equals(request.getPurpose(), "fail")){
            return true;
        }
        //审核成功，创建对象，向注册表填入数据
        ReviewApplicationRequest applicationRequest=new ReviewApplicationRequest();
        applicationRequest.setVolunteerSignupId(UUID.randomUUID().toString());
        applicationRequest.setVolunteerSignupTime(new Date());
        BeanUtils.copyProperties(request,applicationRequest);
        //审核结果存入数据库
        int result= volunteerMapper.successSignup(applicationRequest);
        return result==1;
    }

    //判断为审核成功还是失败
    private void reviewStatusResult(ReviewRequest request){
        int result=0;
        Integer code=0;
        //同时向数据库更改
        if (StringUtils.equals(request.getPurpose(),"success")){
            code = ReviewStatus.success.getCode();
            result = volunteerMapper.resultReview(code, request.getVolunteerActivityId(), request.getUserId());
            addActivityNumber(request.getVolunteerActivityId());
        } else if (StringUtils.equals(request.getPurpose(), "fail")) {
            code=ReviewStatus.fail.getCode();
            result = volunteerMapper.resultReview(code, request.getVolunteerActivityId(), request.getUserId());
        }else {
            throw new BaseException("请求参数错误");
        }

        if (result==1){
            return;
        }
        throw new BaseException("审核错误");
    }

    //审核成功后人数+1
    private void addActivityNumber(String activityId){
        //活动人数+1
        int result= volunteerMapper.addActivityNumber(activityId);
        if (result!=1){
            throw new BaseException("活动人数增加失败");
        }
    }

    //用户报名
    @Override
    public boolean applicationActivity(ReviewRequest request) {
        //根据认证后的用户获取其信息
        User user= AuthenticateGetUserUtils.getUser();
        //先判断用户是否以及报名过
        int result= volunteerMapper.hasApplicationActivity(request.getVolunteerActivityId(),user.getUserId());
        if (result==1){
            throw new BaseException("请勿重复参加活动");
        }
        //封装请求信息
        ReviewInfo info=ReviewInfo.builder()
                .reviewId(UUID.randomUUID().toString())
                .volunteerActivityId(request.getVolunteerActivityId())
                .userId(user.getUserId())
                .userName(user.getName())
                .userPhone(user.getPhone())
                .userIdNumber(user.getIdNumber())
                .reviewStatus(ReviewStatus.review.getCode()).build();
        //将报名存储数据库
        int resultApplication=volunteerMapper.insertApplicationActivity(info);
        return resultApplication==1;
    }

    //获取用户已报名的全部活动
    @Override
    public List<UserJoinActivity> joinedActivity() {

        User user = AuthenticateGetUserUtils.getUser();
        //获取用户已报名的全部活动
        List<UserJoinActivity> baseInfos = volunteerMapper.joinedActivity(user.getUserId());
        return baseInfos.stream().peek(userJoinActivity -> {
            if (!StringUtils.isBlank(userJoinActivity.getBaseInfo().getActivityImageName())) {
                String preview = minioUtils.preview(userJoinActivity.getBaseInfo().getActivityImageName());
                userJoinActivity.getBaseInfo().setActivityImage(preview);
            }
        }).collect(Collectors.toList());
    }

}
