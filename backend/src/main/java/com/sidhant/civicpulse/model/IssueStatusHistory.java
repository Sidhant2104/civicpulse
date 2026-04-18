package com.sidhant.civicpulse.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class IssueStatusHistory {

    @Id
    private String id;

    @ManyToOne
    private Issue issue;

    private IssueStatus status;

    @ManyToOne
    private User updatedBy;

    private LocalDateTime updatedAt;

    public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

public Issue getIssue() {
    return issue;
}

public void setIssue(Issue issue) {
    this.issue = issue;
}

public IssueStatus getStatus() {
    return status;
}

public void setStatus(IssueStatus status) {
    this.status = status;
}

public User getUpdatedBy() {
    return updatedBy;
}

public void setUpdatedBy(User updatedBy) {
    this.updatedBy = updatedBy;
}

public LocalDateTime getUpdatedAt() {
    return updatedAt;
}

public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
}
}
