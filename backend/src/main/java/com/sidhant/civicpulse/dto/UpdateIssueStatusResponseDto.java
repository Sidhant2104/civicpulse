package com.sidhant.civicpulse.dto;

import java.time.LocalDateTime;

public class UpdateIssueStatusResponseDto {

    private String message;
    private String issueId;
    private String newStatus;
    private LocalDateTime updatedAt;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UpdateIssueStatusResponseDto() {
    }
}