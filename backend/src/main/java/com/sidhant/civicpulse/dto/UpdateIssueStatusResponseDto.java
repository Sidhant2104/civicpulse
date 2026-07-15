package com.sidhant.civicpulse.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class UpdateIssueStatusResponseDto {

    private String issueId;
    private String newStatus;
    private LocalDateTime updatedAt;


}