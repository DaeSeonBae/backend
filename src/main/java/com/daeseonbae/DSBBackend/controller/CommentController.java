package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.CommentRequestDTO;
import com.daeseonbae.DSBBackend.dto.CommentResponseDTO;
import com.daeseonbae.DSBBackend.jwt.JWTUtil;
import com.daeseonbae.DSBBackend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;
    private final JWTUtil jwtUtil;

    @Autowired
    public CommentController(CommentService commentService, JWTUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/board/comment/{boardId}")
    public String addComment(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                             @PathVariable Integer boardId,
                             @RequestBody CommentRequestDTO commentRequest) {
        String token = authorizationHeader.substring(7);
        Integer userId = jwtUtil.getId(token);

        return commentService.processComment(boardId, userId, commentRequest);
    }

    @GetMapping("/api/board/comment/{boardId}")
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable Integer boardId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByBoardId(boardId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/api/board/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                @PathVariable Integer commentId) {
        String token = authorizationHeader.substring(7);
        Integer userId = jwtUtil.getId(token);

        String response = commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(response);
    }
}
