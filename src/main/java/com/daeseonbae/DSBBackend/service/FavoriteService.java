package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.entity.FavoriteEntity;
import com.daeseonbae.DSBBackend.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteEntity addFavorite(int boardId, int userId) {
        FavoriteEntity favorite = new FavoriteEntity();
        favorite.setBoardNumber(boardId);
        favorite.setUserId(userId);
        return favoriteRepository.save(favorite);
    }
}
