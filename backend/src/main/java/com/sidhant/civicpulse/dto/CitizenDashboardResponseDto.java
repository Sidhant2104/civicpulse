package com.sidhant.civicpulse.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CitizenDashboardResponseDto {

    private int totalIssues;
    private int openIssues;
    private int resolvedIssues;
    private int closedIssues;
    private int escalatedIssues;
    private int slaBreachedIssues;

}