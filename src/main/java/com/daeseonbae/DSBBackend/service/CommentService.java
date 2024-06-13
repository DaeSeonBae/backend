package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.CommentRequestDTO;
import com.daeseonbae.DSBBackend.dto.CommentResponseDTO;
import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.entity.CommentEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.repository.BoardRepository;
import com.daeseonbae.DSBBackend.repository.CommentRepository;
import com.daeseonbae.DSBBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

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
        increaseCommentCount(boardId);

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

    private void increaseCommentCount(Integer boardId) {
        // 게시판 조회 및 댓글 수 증가
        BoardEntity board = boardRepository.findById(boardId).orElse(null);
        if (board != null) {
            Long currentCommentCount = board.getCommentCount();
            if (currentCommentCount == null) {
                currentCommentCount = 0L;
            }
            board.setCommentCount(currentCommentCount + 1);
            boardRepository.save(board);
        }
    }
}
