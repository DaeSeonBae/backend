package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.board.BoardCreateDTO;
import com.daeseonbae.DSBBackend.dto.board.BoardDeleteDTO;
import com.daeseonbae.DSBBackend.dto.board.BoardInfo;
import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.jwt.JWTUtil;
import com.daeseonbae.DSBBackend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    //특정 게시물 정보 가져오기
    @GetMapping("/view/{id}")
    public Optional<BoardEntity> boardView(@PathVariable Integer id){
        return boardService.boardView(id);
    }

    //특정 게시물 삭제
    @DeleteMapping("/{id}") //게시물 id 값과 요청하는 사용자의 토큰을 받음
    public ResponseEntity<String> boardDelete(@RequestBody BoardDeleteDTO boardDeleteDTO){
        //boardService.boardDelete 에서 삭제를 성공시 true 실패시 false를 반환
        boolean isDelete = boardService.boardDelete(boardDeleteDTO.getId(),boardDeleteDTO.getToken());
        if(isDelete){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(404).body("존재하지 않는 게시물 입니다.");
        }
    }

}
