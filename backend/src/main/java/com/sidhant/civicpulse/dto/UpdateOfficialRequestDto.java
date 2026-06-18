package com.sidhant.civicpulse.dto;

import com.sidhant.civicpulse.model.Level;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOfficialRequestDto {
    private String name;
    private String phoneNo;
    private String departmentId;
    private Level level;
}
