package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.CommentRequestDTO;
import com.daeseonbae.DSBBackend.service.CommentService;
import com.daeseonbae.DSBBackend.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

    private final CommentService commentService;
    private final JWTUtil jwtUtil;

    @Autowired
    public CommentController(CommentService commentService, JWTUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/board/view/{boardId}")
    public String addComment(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                             @PathVariable Integer boardId,
                             @RequestBody CommentRequestDTO commentRequest) {
        // Extract token from Authorization header
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        // Get userId from token
        Integer userId = jwtUtil.getId(token);

        // Process comment with the userId from the token
        return commentService.processComment(boardId, userId, commentRequest);
    }
}
