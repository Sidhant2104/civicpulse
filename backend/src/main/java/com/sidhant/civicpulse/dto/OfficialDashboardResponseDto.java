package com.sidhant.civicpulse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfficialDashboardResponseDto {
    private int assignedIssues;
    private int inProgressIssues;
    private int resolvedIssues;
    private int escalatedIssues;
    private int closedIssues;

}
