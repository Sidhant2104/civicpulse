package com.sidhant.civicpulse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EscalationStatisticsResponseDto {
    private long totalEscalatedIssues;
    private long level1To2Escalations;
    private long level2To3Escalations;
    private long level3To4Escalations;
}
