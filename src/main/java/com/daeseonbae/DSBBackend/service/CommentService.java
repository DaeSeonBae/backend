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

    public String processComment(Integer boardId, Integer userId, CommentRequestDTO commentRequest) {
//        userId가 없으면 예외 발생
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
//        boardId 없으면 에외 발생
        Optional<BoardEntity> optionalBoard = boardRepository.findById(boardId);
        if (!optionalBoard.isPresent()) {
            throw new RuntimeException("Board not found");
        }

        UserEntity user = optionalUser.get();
        BoardEntity board = optionalBoard.get();

        CommentEntity comment = new CommentEntity();
        comment.setUser(user);
        comment.setBoard(board);
        comment.setContent(commentRequest.getContent());
        comment.setWriteDatetime(LocalDateTime.now());

        commentRepository.save(comment);

        return "Comment saved successfully";
    }

    public List<CommentResponseDTO> getCommentsByBoardId(Integer boardId) {
        List<CommentEntity> comments = commentRepository.findByBoardBoardNumber(boardId);
        return comments.stream()
//                각 CommentEntity를 기반으로 CommentResponseDTO 객체를 생성
                .map(comment -> new CommentResponseDTO(comment.getCommentNumber(), comment.getContent(), comment.getWriteDatetime(), comment.getUser().getId()))
//                스트림의 각 요소를 리스트로 모아서 List<CommentResponseDTO> 형태로 반환
                .collect(Collectors.toList());
    }
}
