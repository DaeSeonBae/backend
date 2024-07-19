package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.CustomUserDetails;
import com.daeseonbae.DSBBackend.dto.UserCommentDTO;
import com.daeseonbae.DSBBackend.entity.CommentEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.repository.UserCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCommentService {

    @Autowired
    private UserCommentRepository usercommentRepository;

    public List<UserCommentDTO> getCommentsByUser(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity user = new UserEntity();
        user.setId(customUserDetails.getId());
        List<CommentEntity> commentEntities = usercommentRepository.findByUser(user);
        return commentEntities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private UserCommentDTO convertToDTO(CommentEntity commentEntity) {
        UserCommentDTO commentDTO = new UserCommentDTO();
        commentDTO.setBoardNumber(commentEntity.getBoard().getBoardNumber());
        commentDTO.setContent(commentEntity.getContent());
        commentDTO.setWriteDatetime(commentEntity.getWriteDatetime());
        return commentDTO;
    }
}
