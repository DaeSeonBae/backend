package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.board.BoardRequestDTO;
import com.daeseonbae.DSBBackend.dto.board.BoardResponseDTO;
import com.daeseonbae.DSBBackend.service.BoardService;
import com.daeseonbae.DSBBackend.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시물 작성 (이미지 포함)
    @PostMapping
    public ResponseEntity<Integer> createBoard(@ModelAttribute BoardRequestDTO boardRequestDTO) throws IOException {
        Integer boardId = boardService.createBoard(boardRequestDTO);
        return ResponseEntity.ok(boardId);
    }

    //게시물 리스트 정보 가져오기
    @GetMapping("/list")
    public List<BoardResponseDTO> boardList() {
        return boardService.boardList();
    }

    //특정 게시물 정보 가져오기
    @GetMapping("/view/{id}")
    public Optional<BoardResponseDTO> boardView(@PathVariable Integer id) {
        return boardService.boardView(id);
    }

    //특정 게시물 삭제
    @DeleteMapping("/{id}") //게시물 id 값과 요청하는 사용자의 토큰을 받음
    public ResponseEntity<String> boardDelete(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        //boardService.boardDelete 에서 삭제를 성공시 true 실패시 false를 반환
        boolean isDelete = boardService.boardDelete(id, token);
        if (isDelete) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).body("존재하지 않는 게시물 입니다.");
        }
    }

    //특정 게시물 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> boardModify(@PathVariable Integer id, @RequestBody BoardRequestDTO boardRequestDTO, @RequestHeader("Authorization") String token) throws AccessDeniedException {
        try{
            boolean isModify = boardService.boardUpdate(id, boardRequestDTO, token);
            if(isModify){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("요청 실패");
            }
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물을 찾을 수가 없습니다.");
        }catch(AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
