package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.JoinDTO;
import com.daeseonbae.DSBBackend.repository.UserRepository;
import com.daeseonbae.DSBBackend.service.JoinService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public String joinProcess(JoinDTO joinDTO){

        boolean success = joinService.joinProcess(joinDTO);

        if(success){
            return "ok";
        }else{
            return "error";
        }
    }
}
