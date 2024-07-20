package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.CustomUserDetails;
import com.daeseonbae.DSBBackend.dto.UserBoardDTO;
import com.daeseonbae.DSBBackend.dto.UserCommentDTO;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.service.BoardService;
//import com.daeseonbae.DSBBackend.service.UserBoardService;
import com.daeseonbae.DSBBackend.service.UserBoardService;
import com.daeseonbae.DSBBackend.service.UserCommentService;
import com.daeseonbae.DSBBackend.service.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserCommentService userCommentService;
    private final UserBoardService userboardService;

    public UserInfoController(UserInfoService userInfoService, UserCommentService userCommentService, UserBoardService userboardService) {
        this.userInfoService = userInfoService;
        this.userCommentService = userCommentService;
        this.userboardService = userboardService;
    }

    @GetMapping("/api/user-info")
    public UserEntity userInfo(Authentication authentication) {
        return userInfoService.getUserInfo(authentication);
    }

    @GetMapping("/user/comment")
    public List<UserCommentDTO> getUserComments(Authentication authentication) {
        return userCommentService.getCommentsByUser(authentication);
    }

//    내가 작성한 게시글 조회
    @GetMapping("/user/board/list")
    public List<UserBoardDTO> getUserBoards(Authentication authentication) {
        return userboardService.getBoardsByUser();
    }
}