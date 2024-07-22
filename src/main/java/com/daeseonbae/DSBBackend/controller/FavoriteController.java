package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.jwt.JWTUtil;
import com.daeseonbae.DSBBackend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final JWTUtil jwtUtil;

    @Autowired
    public FavoriteController(FavoriteService favoriteService, JWTUtil jwtUtil) {
        this.favoriteService = favoriteService;
        this.jwtUtil = jwtUtil;
    }

    //    게시글에 좋아요
    @PostMapping("/board/{boardId}")
    public ResponseEntity<String> addBoardFavorite(@PathVariable Integer boardId, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        Integer userId = jwtUtil.getId(token);
        String result = favoriteService.addFavorite(boardId, userId);
        return ResponseEntity.ok(result);
    }

    //    댓글에 좋아요
    @PostMapping("/comment/{commentId}")
    public ResponseEntity<String> addCommentFavorite(@PathVariable Integer commentId, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        Integer userId = jwtUtil.getId(token);
        String result = favoriteService.addCommentFavorite(commentId, userId);
        return ResponseEntity.ok(result);
    }
}
