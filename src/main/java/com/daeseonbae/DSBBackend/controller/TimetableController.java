package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.TimetableRequestDTO;
import com.daeseonbae.DSBBackend.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
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

        // 타임테이블 서비스에서 유저의 수업 정보를 저장
        timetableService.saveTimetable(timetableRequestDTO, authentication);

        return ResponseEntity.ok("200 OK");
    }
}
