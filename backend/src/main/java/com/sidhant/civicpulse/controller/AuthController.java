package com.sidhant.civicpulse.controller;

import com.sidhant.civicpulse.dto.*;
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
    public ApiResponse<SignupResponseDto> registerUser(@Valid @RequestBody SignupRequestDto request){
        return ApiResponse.success(
                "Signup Successful",
                authService.registerUser(request)
        );
    }


//    //2: LOGIN
    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> LoginUser(@Valid @RequestBody LoginRequestDto request){
        return ApiResponse.success(
                "Login Successful",
                authService.LoginUser(request)
        );
    }


}
