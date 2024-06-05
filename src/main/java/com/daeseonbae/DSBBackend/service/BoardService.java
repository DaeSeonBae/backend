package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.board.BoardCreateDTO;
import com.daeseonbae.DSBBackend.dto.board.BoardResponseDTO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final JWTUtil jwtUtil;

    //게시물 작성
    @Transactional
    public Integer createBoard(BoardCreateDTO boardCreateDTO){
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setTitle(boardCreateDTO.getTitle());
        boardEntity.setContent(boardCreateDTO.getContent());
        boardEntity.setComment_count(0L);
        boardEntity.setFavorite_count(0L);
        boardEntity.setWrite_datetime(LocalDateTime.now());

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boardEntity.setWriter_email(currentUserEmail);

        BoardEntity savedEntity = boardRepository.save(boardEntity);
        return savedEntity.getBoard_number();
    }

    //게시물 리스트 조회
    public List<BoardResponseDTO> boardList(){
        //모든 객체 메서드를 가져옴
        List<BoardEntity> boardEntities = boardRepository.findAll();

        //BoardEntity리스트를 BoardResponseDTO 리스트로 변환
        return boardEntities.stream()
                .map(BoardResponseDTO::new)
                .collect(Collectors.toList());
    }

    //특정 게시물 조회
    public Optional<BoardResponseDTO> boardView(Integer id){
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        return optionalBoardEntity.map(BoardResponseDTO::new);
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
