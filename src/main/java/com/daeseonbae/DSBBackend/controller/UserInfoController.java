package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.CustomUserDetails;
import com.daeseonbae.DSBBackend.dto.UserCommentDTO;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.service.UserCommentService;
import com.daeseonbae.DSBBackend.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserCommentService userCommentService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService, UserCommentService userCommentService) {
        this.userInfoService = userInfoService;
        this.userCommentService = userCommentService;
    }

    @GetMapping("/api/user-info")
    public UserEntity userInfo(Authentication authentication) {
        return userInfoService.getUserInfo(authentication);
    }

    @GetMapping("/comment")
    public List<UserCommentDTO> getUserComments(Authentication authentication) {
        return userCommentService.getCommentsByUser(authentication);
    }
}
