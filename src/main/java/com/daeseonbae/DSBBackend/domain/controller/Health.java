package com.daeseonbae.DSBBackend.domain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health {
    @GetMapping("/health")
    public String test(){
        return "Hello DSB";
    }
}
