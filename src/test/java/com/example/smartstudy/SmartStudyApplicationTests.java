package com.example.smartstudy;

import cn.hutool.core.util.EnumUtil;
import com.example.smartstudy.controller.UserController;
import com.example.smartstudy.mapper.UserMapper;
import com.example.smartstudy.model.dto.Security.Lgoin.LoginRequest;
import com.example.smartstudy.model.dto.Security.Lgoin.LoginType;
import com.example.smartstudy.model.dto.Volunteer.VolunteerReview.ReviewStatus;
import com.example.smartstudy.utils.XfUtil;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.EnumUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.domain.geo.RadiusShape;

import java.util.UUID;

@SpringBootTest
class SmartStudyApplicationTests {

    @Autowired
    private XfUtil xfUtil;

    @Test
    void contextLoads() {
//        String textResult = xfUtil.SyncTextResult("傻逼");
//        System.out.println(textResult);
//        String textResult1 = xfUtil.SyncTextResult("");
//        System.out.println(textResult1);
//    }
//
//    public static final String hostUrl = "https://spark-api.cn-huabei-1.xf-yun.com/v2.1/image";
//    public static final String appid = "5a462c52";
//    public static final String apiSecret = "NTY1MDJkZDUyODBhNWIyOTQ3NmVkYmQw";
//    public static final String apiKey = "70e63cd93ff7cb9bf3910cd6f3b0ebba";
//
////    public static List<SmartStudyApplicationTests.RoleContent> historyList=new ArrayList<>(); // 对话历史存储集合
//
//    public static String totalAnswer = ""; // 大模型的答案汇总
//
//    // 环境治理的重要性  环保  人口老龄化  我爱我的祖国
//    public static String NewQuestion = "";
//    public static Boolean ImageAddFlag = false; // 判断是否添加了图片信息
//
//    public static final Gson gson = new Gson();
//
//    // 个性化参数
//    private String userId;
//    private Boolean wsCloseFlag;
//
//    private static Boolean totalFlag = true; // 控制提示用户是否输入
    // 构造函数
//    public BigModelNew(String userId, Boolean wsCloseFlag) {
//        this.userId = userId;
//        this.wsCloseFlag = wsCloseFlag;
    }

    @Autowired
    private UserController userController;

    @Test
    void test() {
//        ReviewStatus[] values = ReviewStatus.values();
//        for (ReviewStatus value : values) {
//            System.out.println(value);
//        }
//
//        System.out.println(ReviewStatus.review.getCode());
//        System.out.println(ReviewStatus.review);
//        System.out.println(EnumUtils.getEnumList(ReviewStatus.class));
//
//        if (EnumUtil.equals(ReviewStatus.success, "success")) {
//            System.out.println(ReviewStatus.success.getCode());
//        }
    }

    @Resource
    private UserMapper userMapper;

    @Test
    public void test1() {
//        String uuid = UUID.randomUUID().toString();
//        int role_id=1;
//        String name = "认证用户";
//        String role_key = "user";
//        int i=userMapper.insertRole(name,role_key);
//        System.out.println(i);
//        String name="用户获取已报名活动";
//        String prem_key="user_joined_activity";
//        int i =userMapper.insertmenu(name,prem_key);
//        System.out.println(i);
    }


}
