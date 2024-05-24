package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    //게시물 작성
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

    //게시물 리스트 조회
    public List<BoardEntity> boardList(){
        return boardRepository.findAll();
    }

    //특정 게시물 조회
    public Optional<BoardEntity> boardView(Integer id){
        return boardRepository.findById(id);
    }

}
