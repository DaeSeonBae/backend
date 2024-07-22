package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.CommentRequestDTO;
import com.daeseonbae.DSBBackend.dto.CommentResponseDTO;
import com.daeseonbae.DSBBackend.jwt.JWTUtil;
import com.daeseonbae.DSBBackend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class CommentController {

    private final CommentService commentService;
    private final JWTUtil jwtUtil;

    @Autowired
    public CommentController(CommentService commentService, JWTUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    //댓글 작성
    @PostMapping("/api/board/comment/{boardId}")
    public String addComment(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                             @PathVariable Integer boardId,
                             @RequestBody CommentRequestDTO commentRequest) {
        Integer userId = getUserId(authorizationHeader);

        return commentService.processComment(boardId, userId, commentRequest);
    }

    //댓글 조회
    @GetMapping("/api/board/comment/{boardId}")
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable Integer boardId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByBoardId(boardId);
        return ResponseEntity.ok(comments);
    }


    //특정 댓글 수정
    @PutMapping("/api/board/comment")
    public ResponseEntity<String> commentModify(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                @RequestParam Integer commentId,
                                @RequestParam Integer boardId,
                                @RequestBody CommentRequestDTO commentRequestDTO){

        Integer userId = getUserId(token);

        boolean isModify = commentService.commentUpdate(commentId,boardId,commentRequestDTO,userId);
        if(isModify){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("요청 실패");
        }
    }

    //댓글 삭제
    @DeleteMapping("/api/board/comment")
    public ResponseEntity<String> commentDelete(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                @RequestParam Integer commentId,
                                                @RequestParam Integer boardId
                                                ){

        Integer userId = getUserId(token);

        boolean isDelete = commentService.commentDelete(commentId,boardId,userId);
        if(isDelete){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(404).body("존재하지 않는 댓글 입니다.");
        }
    }

    public Integer getUserId(String token){
        String newToken = token.substring(7);
        return jwtUtil.getId(newToken);
    }
}
