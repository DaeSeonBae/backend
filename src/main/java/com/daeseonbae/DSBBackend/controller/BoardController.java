package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.board.BoardCreateDTO;
import com.daeseonbae.DSBBackend.dto.board.BoardInfo;
import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //게시물 작성
    @PostMapping
    public Integer createBoard(@RequestBody BoardEntity boardEntity){
        return boardService.createBoard(boardEntity);
    }

    //게시물 리스트 정보 가져오기
    @GetMapping("/list")
    public List<BoardEntity> boardList(){
        return boardService.boardList();
    }
}
