package com.daeseonbae.DSBBackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hello {
    @GetMapping("/test")
    public String test(){
        return "Hello JAVA";
    }
}
