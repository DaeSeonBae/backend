package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.CustomUserDetails;
import com.daeseonbae.DSBBackend.dto.UserBoardDTO;
import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserBoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 현재 로그인한 사용자의 게시판 목록
    public List<UserBoardDTO> getBoardsByUser() {
        // SecurityContextHolder를 사용하여 현재 인증된 사용자의 세부 정보를 가져옴
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 사용자 이메일을 가져옴
        String userEmail = customUserDetails.getEmail();
        // 사용자 이메일로 게시판 엔티티 목록을 조회
        List<BoardEntity> boardEntities = boardRepository.findByWriterEmail(userEmail);
        // 게시판 엔티티 목록을 DTO 목록으로 변환하여 반환
        return boardEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // BoardEntity를 UserBoardDTO로 변환
    private UserBoardDTO convertToDTO(BoardEntity boardEntity) {
        UserBoardDTO boardDTO = new UserBoardDTO();
        boardDTO.setTitle(boardEntity.getTitle());
        boardDTO.setFavoriteCount(boardEntity.getFavoriteCount());
        boardDTO.setCommentCount(boardEntity.getCommentCount());
        return boardDTO;
    }
}
