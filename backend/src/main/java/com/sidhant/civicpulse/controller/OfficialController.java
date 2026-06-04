package com.sidhant.civicpulse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/official")
public class OfficialController {

    @GetMapping("/hello")
    public String helloOfficial() {
        return "Hello Official";
    }
}