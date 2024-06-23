package com.example.smartstudy.controller;

import com.example.smartstudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RestController
//获取好友列表的控制器
public class GetFriendsController {

    private final UserService userService;

    @Autowired
    public GetFriendsController(UserService userService) {
        this.userService = userService;
    }
//
//    @PostMapping("/getFriends")
//    public List<User> getFriends(@RequestParam("id") Long uid) {
////        return userService.getFriends(uid);
//        return null;
//    }

}
