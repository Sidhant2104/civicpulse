package com.sidhant.civicpulse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentStatisticsResponseDto {
    private String departmentName;

    private long totalIssues;
    private long openIssues;
    private long resolvedIssues;
    private long closedIssues;
    private long escalatedIssues;
    private long slaBreachedIssues;

    private double resolutionRate;
    private double slaComplianceRate;
}
