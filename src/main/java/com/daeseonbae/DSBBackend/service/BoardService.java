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

import java.io.IOException;
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
    private final S3ImageService s3ImageService;
    private final JWTUtil jwtUtil;

    // 게시물 작성
    @Transactional
    public Integer createBoard(BoardRequestDTO boardRequestDTO) throws IOException {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setTitle(boardRequestDTO.getTitle());
        boardEntity.setContent(boardRequestDTO.getContent());
        boardEntity.setCommentCount(0L);
        boardEntity.setFavoriteCount(0L);
        boardEntity.setWriteDatetime(LocalDateTime.now());

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boardEntity.setWriterEmail(currentUserEmail);

        // 이미지 업로드 및 URL 저장
        if (boardRequestDTO.getImage() != null && !boardRequestDTO.getImage().isEmpty()) {
            String imageUrl = s3ImageService.upload(boardRequestDTO.getImage());
            boardEntity.setImageUrl(imageUrl);
        }

        BoardEntity savedEntity = boardRepository.save(boardEntity);
        return savedEntity.getBoardNumber();
    }

    // 게시물 리스트 조회
    public List<BoardResponseDTO> boardList() {
        List<BoardEntity> boardEntities = boardRepository.findAll();
        return boardEntities.stream()
                .map(BoardResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 특정 게시물 조회
    public Optional<BoardResponseDTO> boardView(Integer id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        return optionalBoardEntity.map(BoardResponseDTO::new);
    }

    // 특정 게시물 삭제
    public boolean boardDelete(Integer id, String token) {
        String email = jwtUtil.getUsername(token.substring(7));
        Optional<BoardEntity> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            BoardEntity boardEntity = optionalBoard.get();
            if (boardEntity.getWriterEmail().equals(email)) {
                if (boardEntity.getImageUrl() != null && !boardEntity.getImageUrl().isEmpty()) {
                    s3ImageService.deleteImageFromS3(boardEntity.getImageUrl());
                }
                boardRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

    // 특정 게시물 수정
    public boolean boardUpdate(Integer id, BoardRequestDTO boardRequestDTO, String token) throws IOException {
        String email = jwtUtil.getUsername(token.substring(7));
        Optional<BoardEntity> optionalBoardEntity = Optional.ofNullable(boardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("번호에 맞는 게시글을 찾지 못했습니다!")));

        BoardEntity boardEntity = optionalBoardEntity.get();
        if (!boardEntity.getWriterEmail().equals(email)) {
            throw new AccessDeniedException("게시글 작성자가 아닙니다!");
        }

        boardEntity.setTitle(boardRequestDTO.getTitle());
        boardEntity.setContent(boardRequestDTO.getContent());
        boardEntity.setWriteDatetime(LocalDateTime.now());

        if (boardRequestDTO.getImage() != null && !boardRequestDTO.getImage().isEmpty()) {
            String imageUrl = s3ImageService.upload(boardRequestDTO.getImage());
            boardEntity.setImageUrl(imageUrl);
        }

        boardRepository.save(boardEntity);
        return true;
    }

    //Hot 게시물 리스트 가져오기
    public List<BoardResponseDTO> boardHotList(){
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(14); //현재일로부터 14일전
        List<BoardEntity> boardEntities = boardRepository.findRecentHotBoards(sevenDaysAgo);
        return boardEntities.stream()
                .map(BoardResponseDTO::new)
                .collect(Collectors.toList());

    }
}
