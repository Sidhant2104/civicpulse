package com.sidhant.civicpulse.dto;

import com.sidhant.civicpulse.model.Level;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetOfficialResponseDto {
    private String id;
    private String name;
    private String email;
    private String phoneNo;
    private String departmentId;
    private Level level;
    private LocalDateTime createdAt;
}
