package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.jwt.JWTUtil;
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
    private final JWTUtil jwtUtil;

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

    //특정 게시물 삭제
    public boolean boardDelete(Integer id,String token){
        //요청한 사용자의 email 추출
        String email = jwtUtil.getUsername(token.substring(7));
        //null값을 안전하게 처리하기 위해 Optional 사용
        Optional<BoardEntity> optionalBoard = boardRepository.findById(id);

        if(optionalBoard.isPresent()){ //값이 있으면 true
            BoardEntity boardEntity = optionalBoard.get();
            if(boardEntity.getWriter_email().equals(email)){
                boardRepository.deleteById(id);
                return true;
            }
        }

        return false;
    }

}
