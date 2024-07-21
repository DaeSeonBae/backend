package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.CommentRequestDTO;
import com.daeseonbae.DSBBackend.dto.CommentResponseDTO;
import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.entity.CommentEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.jwt.JWTUtil;
import com.daeseonbae.DSBBackend.repository.BoardRepository;
import com.daeseonbae.DSBBackend.repository.CommentRepository;
import com.daeseonbae.DSBBackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional
    public String processComment(Integer boardId, Integer userId, CommentRequestDTO commentRequest) {
        // 사용자 조회
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }

        // 게시판 조회
        Optional<BoardEntity> optionalBoard = boardRepository.findById(boardId);
        if (!optionalBoard.isPresent()) {
            throw new RuntimeException("Board not found");
        }

        UserEntity user = optionalUser.get();
        BoardEntity board = optionalBoard.get();

        // 새로운 댓글 생성 및 저장
        CommentEntity comment = new CommentEntity();
        comment.setUser(user);
        comment.setBoard(board);
        comment.setContent(commentRequest.getContent());
        comment.setWriteDatetime(LocalDateTime.now());

        commentRepository.save(comment);

        // 게시판의 댓글 수 증가
        updateCommentCount(boardId,true);

        return "Comment saved successfully";
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getCommentsByBoardId(Integer boardId) {
        // 게시판에 해당하는 모든 댓글 조회
        List<CommentEntity> comments = commentRepository.findByBoardBoardNumber(boardId);
        return comments.stream()
                .map(comment -> new CommentResponseDTO(comment.getCommentNumber(), comment.getContent(), comment.getWriteDatetime(), comment.getUser().getId()))
                .collect(Collectors.toList());
    }

    //댓글 수정
    public boolean commentUpdate(Integer commentId, Integer boardId, CommentRequestDTO commentRequestDTO, Integer userId) {
        try {
            Optional<BoardEntity> optionalBoardEntity = Optional.ofNullable(boardRepository.findById(boardId)
                    .orElseThrow(() -> new NoSuchElementException("번호에 맞는 게시글을 찾지 못했습니다!")));

            Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("번호에 맞는 유저를 찾지 못했습니다!")));

            Optional<CommentEntity> optionalCommentEntity = Optional.ofNullable(commentRepository.findById(commentId)
                    .orElseThrow(() -> new NoSuchElementException("번호에 맞는 댓글을 찾지 못했습니다!")));

            CommentEntity comment = optionalCommentEntity.get();
            if (!comment.getUser().getId().equals(userId)) {
                throw new AccessDeniedException("댓글 작성자가 아닙니다!");
            }

            // 댓글 내용 수정
            comment.setContent(commentRequestDTO.getContent());
            comment.setWriteDatetime(LocalDateTime.now());

            commentRepository.save(comment);

            return true;
        } catch (NoSuchElementException | AccessDeniedException e) {
            logger.error("에러 메세지:", e);
            // 예외 발생 시 false 반환
            return false;
        }
    }

    //댓글 삭제
    @Transactional
    public boolean commentDelete(Integer commentId, Integer boardId, CommentRequestDTO commentRequestDTO, Integer userId) {
        try{
            Optional<BoardEntity> optionalBoardEntity = Optional.ofNullable(boardRepository.findById(boardId)
                    .orElseThrow(() -> new NoSuchElementException("번호에 맞는 게시글을 찾지 못했습니다!")));

            Optional<CommentEntity> optionalCommentEntity = Optional.ofNullable(commentRepository.findById(commentId)
                    .orElseThrow(() -> new NoSuchElementException("번호에 맞는 댓글을 찾지 못했습니다!")));

            Optional<UserEntity> optionalUserEntity = Optional.ofNullable(userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("번호에 맞는 유저를 찾지 못했습니다!")));

            CommentEntity comment = optionalCommentEntity.get();

            if(!comment.getUser().getId().equals(userId)){
                throw new AccessDeniedException("댓글 작성자가 아닙니다!");
            }

            //댓글 삭제
            commentRepository.delete(comment);

            updateCommentCount(boardId,false);

            return true;
            //게시판 댓글 수 감소
        } catch (NoSuchElementException | AccessDeniedException e) {
            logger.error("에러 메세지:", e);
            // 예외 발생 시 false 반환
            return false;
        }
    }

    //댓글 수 업데이트
    private void updateCommentCount(Integer boardId, boolean increase) {
        BoardEntity board = boardRepository.findById(boardId).orElse(null);
        if (board != null) {
            Long currentCommentCount = board.getCommentCount();
            if (currentCommentCount == null) {
                currentCommentCount = 0L;
            }
            if (increase) {
                board.setCommentCount(currentCommentCount + 1);
            } else {
                board.setCommentCount(Math.max(0, currentCommentCount - 1));
            }
            boardRepository.save(board);
        }
    }

}
