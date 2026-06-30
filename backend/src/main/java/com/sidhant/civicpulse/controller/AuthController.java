package com.sidhant.civicpulse.controller;

import com.sidhant.civicpulse.dto.LoginRequestDto;
import com.sidhant.civicpulse.dto.LoginResponseDto;
import com.sidhant.civicpulse.dto.SignupRequestDto;
import com.sidhant.civicpulse.dto.SignupResponseDto;
import com.sidhant.civicpulse.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    //1: SIGNUP
    @PostMapping("/signup")
    public SignupResponseDto registerUser(@Valid @RequestBody SignupRequestDto request){
        return authService.registerUser(request);
    }

//    //2: LOGIN
    @PostMapping("/login")
    public LoginResponseDto LoginUser(@Valid @RequestBody LoginRequestDto request){
        return authService.LoginUser(request);
    }


}
