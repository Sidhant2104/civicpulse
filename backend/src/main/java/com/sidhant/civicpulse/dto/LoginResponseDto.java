package com.sidhant.civicpulse.dto;

import com.sidhant.civicpulse.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String token;
    private String name;
    private String email;
    private String phoneNo;
    private Role role;
    private String message;
}
