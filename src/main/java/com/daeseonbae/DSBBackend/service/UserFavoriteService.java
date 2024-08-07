package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.CustomUserDetails;
import com.daeseonbae.DSBBackend.dto.UserFavoriteCommentDTO;
import com.daeseonbae.DSBBackend.dto.UserFavoriteDTO;
import com.daeseonbae.DSBBackend.entity.FavoriteEntity;
import com.daeseonbae.DSBBackend.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    // 현재 로그인된 사용자가 좋아요한 게시글 목록 조회
    public List<UserFavoriteDTO> getFavoriteBoardsByUser(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = customUserDetails.getId();
        List<FavoriteEntity> favoriteEntities = favoriteRepository.findByUserId(userId);
        return favoriteEntities.stream()
                .filter(favoriteEntity -> favoriteEntity.getBoardNumber() != null)
                .map(this::convertToBoardDTO)
                .collect(Collectors.toList());
    }

    // 현재 로그인된 사용자가 좋아요한 댓글 목록 조회
    public List<UserFavoriteCommentDTO> getFavoriteCommentsByUser(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = customUserDetails.getId();
        List<FavoriteEntity> favoriteEntities = favoriteRepository.findByUserId(userId);
        return favoriteEntities.stream()
                .filter(favoriteEntity -> favoriteEntity.getCommentNumber() != null)
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList());
    }

    // FavoriteEntity를 UserFavoriteDTO로 변환
    private UserFavoriteDTO convertToBoardDTO(FavoriteEntity favoriteEntity) {
        UserFavoriteDTO favoriteDTO = new UserFavoriteDTO();
        favoriteDTO.setBoardNumber(favoriteEntity.getBoardNumber());
        favoriteDTO.setId(favoriteEntity.getId());
        return favoriteDTO;
    }

    // FavoriteEntity를 UserFavoriteCommentDTO로 변환
    private UserFavoriteCommentDTO convertToCommentDTO(FavoriteEntity favoriteEntity) {
        UserFavoriteCommentDTO favoriteDTO = new UserFavoriteCommentDTO();
        favoriteDTO.setCommentNumber(favoriteEntity.getCommentNumber());
        favoriteDTO.setId(favoriteEntity.getId());
        return favoriteDTO;
    }
}
