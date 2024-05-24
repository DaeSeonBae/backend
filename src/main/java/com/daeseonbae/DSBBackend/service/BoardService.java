package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.board.BoardInfo;
import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.repository.BoardRepository;
import com.daeseonbae.DSBBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Integer createBoard(BoardEntity boardEntity){
        boardEntity.setComment_count(0L);
        boardEntity.setFavorite_count(0L);
        boardEntity.setWrite_datetime(LocalDateTime.now());

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boardEntity.setWriter_email(currentUserEmail);

        BoardEntity savedEntity = boardRepository.save(boardEntity);
        return savedEntity.getBoard_number();
    }

    public List<BoardEntity> boardList(){
        return boardRepository.findAll();
    }
}
