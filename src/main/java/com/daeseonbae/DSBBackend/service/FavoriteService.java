package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.entity.FavoriteEntity;
import com.daeseonbae.DSBBackend.repository.BoardRepository;
import com.daeseonbae.DSBBackend.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, BoardRepository boardRepository) {
        this.favoriteRepository = favoriteRepository;
        this.boardRepository = boardRepository;
    }

    public FavoriteEntity addFavorite(int boardId, int userId) {
        FavoriteEntity existingFavorite = favoriteRepository.findByBoardNumberAndUserId(boardId, userId);
        if (existingFavorite == null) {
            // 사용자가 이 게시물을 좋아요하지 않은 경우
            FavoriteEntity newFavorite = new FavoriteEntity();
            newFavorite.setBoardNumber(boardId);
            newFavorite.setUserId(userId);
            favoriteRepository.save(newFavorite);
            updateFavoriteCount(boardId, 1); // 좋아요 수를 1 증가시킴
            return newFavorite;
        } else {
            // 사용자가 이미 이 게시물을 좋아요한 경우
            favoriteRepository.delete(existingFavorite);
            updateFavoriteCount(boardId, -1); // 좋아요 수를 1 감소시킴
            return null; // 좋아요가 존재하지 않음을 나타냄
        }
    }

    private void updateFavoriteCount(int boardId, int delta) {
        BoardEntity board = boardRepository.findById(boardId).orElse(null);
        if (board != null) {
            Long currentFavoriteCount = board.getFavoriteCount();
            if (currentFavoriteCount == null) {
                currentFavoriteCount = 0L;
            }
            board.setFavoriteCount(currentFavoriteCount + delta);
            boardRepository.save(board);
        }
    }
}
