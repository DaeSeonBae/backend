package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.entity.CommentEntity;
import com.daeseonbae.DSBBackend.entity.FavoriteEntity;
import com.daeseonbae.DSBBackend.repository.BoardRepository;
import com.daeseonbae.DSBBackend.repository.CommentRepository;
import com.daeseonbae.DSBBackend.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, BoardRepository boardRepository, CommentRepository commentRepository) {
        this.favoriteRepository = favoriteRepository;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }

    public String addFavorite(int boardId, int userId) {
        FavoriteEntity existingFavorite = favoriteRepository.findByBoardNumberAndUserId(boardId, userId);
        if (existingFavorite == null) {
            // 사용자가 이 게시물을 좋아요하지 않은 경우
            FavoriteEntity newFavorite = new FavoriteEntity();
            newFavorite.setBoardNumber(boardId);
            newFavorite.setUserId(userId);
            favoriteRepository.save(newFavorite);
            updateBoardFavoriteCount(boardId, 1); // 좋아요 수를 1 증가시킴
            return "favorite OK";
        } else {
            // 사용자가 이미 이 게시물을 좋아요한 경우
            favoriteRepository.delete(existingFavorite);
            updateBoardFavoriteCount(boardId, -1); // 좋아요 수를 1 감소시킴
            return "favorite Cancel";
        }
    }

    public String addCommentFavorite(int commentId, int userId) {
        FavoriteEntity existingFavorite = favoriteRepository.findByCommentNumberAndUserId(commentId, userId);
        if (existingFavorite == null) {
            // 사용자가 이 댓글을 좋아요하지 않은 경우
            FavoriteEntity newFavorite = new FavoriteEntity();
            newFavorite.setCommentNumber(commentId);
            newFavorite.setUserId(userId);
            favoriteRepository.save(newFavorite);
            updateCommentFavoriteCount(commentId, 1); // 좋아요 수를 1 증가시킴
            return "favorite OK";
        } else {
            // 사용자가 이미 이 댓글을 좋아요한 경우
            favoriteRepository.delete(existingFavorite);
            updateCommentFavoriteCount(commentId, -1); // 좋아요 수를 1 감소시킴
            return "favorite Cancel";
        }
    }

    private void updateBoardFavoriteCount(int boardId, int delta) {
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

    private void updateCommentFavoriteCount(int commentId, int delta) {
        CommentEntity comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            Integer currentFavoriteCount = comment.getFavoriteCount();
            if (currentFavoriteCount == null) {
                currentFavoriteCount = 0;
            }
            comment.setFavoriteCount(currentFavoriteCount + delta);
            commentRepository.save(comment);
        }
    }
}
