package com.sidhant.civicpulse.dto;

public class CreateIssueRequestDto {

    private String description;
    private String priority;

    public String getPriority(){
        return priority;
    }

    public String getDescription() {
        return description;

    }
}