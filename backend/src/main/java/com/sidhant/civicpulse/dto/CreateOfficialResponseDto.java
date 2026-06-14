package com.sidhant.civicpulse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOfficialResponseDto {
    private String id;
    private String name;
    private String email;
    private String message;
}
