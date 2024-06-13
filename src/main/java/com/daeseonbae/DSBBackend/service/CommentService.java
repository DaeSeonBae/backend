package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.CommentRequestDTO;
import com.daeseonbae.DSBBackend.entity.CommentEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.repository.CommentRepository;
import com.daeseonbae.DSBBackend.repository.UserRepository;
import com.daeseonbae.DSBBackend.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
        // Get the user and board from the repository
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        Optional<BoardEntity> optionalBoard = boardRepository.findById(boardId);
        if (!optionalBoard.isPresent()) {
            throw new RuntimeException("Board not found");
        }

        // Create and save the comment
        UserEntity user = optionalUser.get();
        BoardEntity board = optionalBoard.get();

        // Get the next comment number for the board
        Integer maxCommentNumber = commentRepository.findMaxCommentNumberByBoardId(boardId);
        int nextCommentNumber = maxCommentNumber + 1;

        CommentEntity comment = new CommentEntity();
        comment.setCommentNumber(nextCommentNumber);
        comment.setUser(user);
        comment.setBoard(board);
        comment.setContent(commentRequest.getContent());
        comment.setWriteDatetime(LocalDateTime.now());

        commentRepository.save(comment);

        return "Comment saved successfully";
    }
}
