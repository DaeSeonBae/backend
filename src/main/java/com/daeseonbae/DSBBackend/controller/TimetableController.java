package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.TimetableRequestDTO;
import com.daeseonbae.DSBBackend.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    @PostMapping
    public ResponseEntity<String> addTimetable(
            @RequestBody TimetableRequestDTO timetableRequestDTO,
            Authentication authentication) {

        try {
            timetableService.saveTimetable(timetableRequestDTO, authentication);
            return ResponseEntity.ok("200 OK");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException 발생 시, 예외 메시지를 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // 다른 예외 발생 시, 일반적인 오류 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A server error occurred: " + e.getMessage());
        }
    }
}
