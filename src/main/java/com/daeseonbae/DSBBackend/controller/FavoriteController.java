package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.entity.FavoriteEntity;
import com.daeseonbae.DSBBackend.jwt.JWTUtil;
import com.daeseonbae.DSBBackend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final JWTUtil jwtUtil;

    @Autowired
    public FavoriteController(FavoriteService favoriteService, JWTUtil jwtUtil) {
        this.favoriteService = favoriteService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/{boardId}")
    public FavoriteEntity addFavorite(@PathVariable Integer boardId, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);

        Integer userId = jwtUtil.getId(token);

        return favoriteService.addFavorite(boardId, userId);
    }
}
