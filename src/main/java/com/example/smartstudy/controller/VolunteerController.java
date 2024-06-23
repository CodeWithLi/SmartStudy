package com.example.smartstudy.controller;

import com.example.smartstudy.common.Result;
import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.model.dto.Volunteer.UserJoinActivity;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityBaseInfo;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityDetails;
import com.example.smartstudy.model.dto.Volunteer.VolunteerActivityRequest;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewInfo;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewRequest;
import com.example.smartstudy.service.VolunteerService;
import com.example.smartstudy.utils.ThrowUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/volunteer")
@Validated
@Slf4j
/**
 * 志愿活动
 */
public class VolunteerController {

    @Autowired
    private VolunteerService volunteerService;

    //todo 后续更改管理员删除活动后，其他有关数据均加密或删除

    /**
     * 查询所有的志愿活动
     * @return
     */
    @GetMapping("/all")
    public Result allActivity(){
        List<VolunteerActivityBaseInfo> vo=volunteerService.allActivity();
        return Result.success(vo);
    }

    /**
     * 查询某项活动的具体信息
     * @param activityId
     * @return
     */
    @GetMapping("/")
    public Result getActivity(@RequestParam String activityId){
        if (StringUtils.isBlank(activityId)){
            throw new BaseException("请求参数错误");
        }
        VolunteerActivityDetails activityDetails=volunteerService.getActivity(activityId);
        return Result.success(activityDetails);
    }

    /**
     * 管理员添加活动
     * @param activityRequest
     * @return
     */
    @PostMapping("/")
//    @PreAuthorize("hasAuthority('admin')")
    public Result addActivity(@RequestBody @Valid VolunteerActivityRequest activityRequest){
        boolean result=volunteerService.addActivity(activityRequest);
        return resultReduce(result,"添加活动成功","添加活动失败");
    }

    //添加图片
    @PutMapping("/upload")
    public Result uploadImage(@RequestParam MultipartFile file,@RequestParam String activityId){
        if (file==null|| !Objects.requireNonNull(file.getContentType()).contains("image")){
            throw new BaseException("请求参数错误");
        }
        String review=volunteerService.uploadImage(file,activityId);
        return Result.success(review);
    }

    /**
     * 管理员修改活动
     * @param activityRequest
     * @return
     */
    @PutMapping("/")
    public Result editActivity(@RequestBody VolunteerActivityRequest activityRequest){
        boolean result=volunteerService.editActivity(activityRequest);
        return resultReduce(result,"修改活动成功","修改活动失败");
    }

    /**
     * 管理员删除活动
     * @param activityId
     * @return
     */
    @DeleteMapping("/")
//    @PreAuthorize("hasAuthority('admin')")
    public Result removeActivity(@RequestParam String activityId){
        if (StringUtils.isBlank(activityId)){
            throw new BaseException("请求参数错误");
        }
        boolean result=volunteerService.removeActivity(activityId);
        return resultReduce(result,"删除活动成功","删除活动失败");
    }

    /**
     * 待审核的活动
     * @return
     */
    @GetMapping("/allReview")
    @PreAuthorize("hasAuthority('volunteer_review')")
    public Result allReview(){
        List<ReviewInfo> info = volunteerService.allReview();
        return Result.success(info);
    }

    /**
     * 审核结果
     * @param request
     * @return
     */
    @PutMapping("/success")
    public Result successReview(@RequestBody @Valid ReviewRequest request) {
        boolean result= volunteerService.reviewStatus(request);
        return resultReduce(result,"审核成功","审核错误");
    }

    /**
     * 用户报名
     * @param request
     * @return
     */
    @PostMapping("/application")
    public Result applicationActivity(@RequestBody @Valid ReviewRequest request){
        boolean result= volunteerService.applicationActivity(request);
        return resultReduce(result,"报名成功","报名失败");
    }

    /**
     * 获取用户已报名的活动
     * @return
     */
    @GetMapping("/activitySuccess")
    public Result joinedActivity(){
        List<UserJoinActivity> activities=volunteerService.joinedActivity();
        return Result.success(activities);
    }

    private Result<String> resultReduce(boolean result, String successMsg, String errorMsg){
        ThrowUtils.throwIf(!result,errorMsg);
        return Result.success(successMsg);
    }
}
