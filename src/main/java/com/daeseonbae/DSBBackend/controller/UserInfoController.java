package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.*;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.service.UserBoardService;
import com.daeseonbae.DSBBackend.service.UserCommentService;
import com.daeseonbae.DSBBackend.service.UserFavoriteService;
import com.daeseonbae.DSBBackend.service.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserCommentService userCommentService;
    private final UserBoardService userboardService;
    private final UserFavoriteService userfavoriteService;

    public UserInfoController(UserInfoService userInfoService, UserCommentService userCommentService, UserBoardService userboardService, UserFavoriteService userfavoriteService) {
        this.userInfoService = userInfoService;
        this.userCommentService = userCommentService;
        this.userboardService = userboardService;
        this.userfavoriteService = userfavoriteService;
    }

    @GetMapping("/api/user-info")
    public UserEntity userInfo(Authentication authentication) {
        return userInfoService.getUserInfo(authentication);
    }

    @GetMapping("/user/comment")
    public List<UserCommentDTO> getUserComments(Authentication authentication) {
        return userCommentService.getCommentsByUser(authentication);
    }

    // 내가 작성한 게시글 조회
    @GetMapping("/user/board/list")
    public List<UserBoardDTO> getUserBoards(Authentication authentication) {
        return userboardService.getBoardsByUser();
    }

    // 내가 좋아요 누른 게시글 조회
    @GetMapping("/user/like-board")
    public List<UserFavoriteDTO> getUserFavoriteBoards(Authentication authentication) {
        return userfavoriteService.getFavoriteBoardsByUser(authentication);
    }

    // 내가 좋아요 누른 댓글 조회
    @GetMapping("/user/like-comment")
    public List<UserFavoriteCommentDTO> getUserFavoriteComments(Authentication authentication) {
        return userfavoriteService.getFavoriteCommentsByUser(authentication);
    }

    @PutMapping("/user/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetDTO passwordResetDTO) {
        userInfoService.resetPassword(passwordResetDTO);
        return ResponseEntity.ok("Password reset successfully");
    }

    @Transactional
    @DeleteMapping("/users/sign-out")
    public ResponseEntity<String> deleteUser(Authentication authentication) {
        userInfoService.deleteUser(authentication);
        return ResponseEntity.ok("User deleted successfully");
    }
}
