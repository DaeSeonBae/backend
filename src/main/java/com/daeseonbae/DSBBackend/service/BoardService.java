package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.board.BoardRequestDTO;
import com.daeseonbae.DSBBackend.dto.board.BoardResponseDTO;
import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.jwt.JWTUtil;
import com.daeseonbae.DSBBackend.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final JWTUtil jwtUtil;

    //게시물 작성
    @Transactional
    public Integer createBoard(BoardRequestDTO boardRequestDTO){
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setTitle(boardRequestDTO.getTitle());
        boardEntity.setContent(boardRequestDTO.getContent());
        boardEntity.setCommentCount(0L);
        boardEntity.setFavoriteCount(0L);
        boardEntity.setWriteDatetime(LocalDateTime.now());

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boardEntity.setWriterEmail(currentUserEmail);

        BoardEntity savedEntity = boardRepository.save(boardEntity);
        return savedEntity.getBoardNumber();
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
            if(boardEntity.getWriterEmail().equals(email)){
                boardRepository.deleteById(id);
                return true;
            }
        }

        return false;
    }


    //특정 게시물 수정
    public boolean boardUpdate(Integer id, BoardRequestDTO boardRequestDTO, String token) throws AccessDeniedException {
        String email = jwtUtil.getUsername(token.substring(7));
        //id값에 맞는 게시글 찾기
        Optional<BoardEntity> optionalBoardEntity = Optional.ofNullable(boardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("번호에 맞는 게시글을 찾지 못했습니다!")));
        //게시글 작성자의 이메일과 요청자의 이메일 비교
        BoardEntity boardEntity = optionalBoardEntity.get();
        if (!boardEntity.getWriterEmail().equals(email)) {
            throw new AccessDeniedException("게시글 작성자가 아닙니다!");
        }

        //게시글 수정
        boardEntity.setTitle(boardRequestDTO.getTitle());
        boardEntity.setContent(boardRequestDTO.getContent());
        boardEntity.setWriteDatetime(LocalDateTime.now());

        boardRepository.save(boardEntity);
        return true;
    }
}
