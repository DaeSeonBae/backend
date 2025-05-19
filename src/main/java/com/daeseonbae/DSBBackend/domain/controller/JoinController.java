package com.daeseonbae.DSBBackend.domain.controller;

import com.daeseonbae.DSBBackend.domain.dto.user.JoinDTO;
import com.daeseonbae.DSBBackend.domain.service.EmailService;
import com.daeseonbae.DSBBackend.domain.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signup")
@RequiredArgsConstructor
public class JoinController {
    private final JoinService joinService;
    private final EmailService emailService;

    @PostMapping
    public String joinProcess(JoinDTO joinDTO){

        boolean success = joinService.joinProcess(joinDTO);

        if(success){
            return "ok";
        }else{
            return "error";
        }
    }

}
