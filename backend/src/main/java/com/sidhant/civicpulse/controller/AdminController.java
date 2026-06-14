package com.sidhant.civicpulse.controller;

import com.sidhant.civicpulse.dto.CreateOfficialRequestDto;
import com.sidhant.civicpulse.dto.CreateOfficialResponseDto;
import com.sidhant.civicpulse.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/officials")
    public CreateOfficialResponseDto createOfficial(
            @RequestBody CreateOfficialRequestDto request){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String adminEmail = authentication.getName();

        return authService.createOfficial(
                request,
                adminEmail
        );
    }

}