package com.sidhant.civicpulse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashboardResponseDto {
    private long totalIssues;
    private long openIssues;
    private long closedIssues;
    private long escalatedIssues;
    private long slaBreachedIssues;
    private long totalCitizens;
    private long totalOfficials;
    private long totalDepartments;
}
