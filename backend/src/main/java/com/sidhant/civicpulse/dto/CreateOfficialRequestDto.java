package com.sidhant.civicpulse.dto;

import com.sidhant.civicpulse.model.Level;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOfficialRequestDto {
    private String name;
    private String email;
    private String phoneNo;
    private String password;
    private String departmentId;
    private Level level;
}
