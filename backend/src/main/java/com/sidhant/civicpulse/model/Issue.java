package com.sidhant.civicpulse.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class Issue {
    @Id
    private String issueId;
    private String description;

    @ManyToOne
    private Department department;

    private Priority priority;

    @ManyToOne
    private User assignedTo;

    @ManyToOne
    private User createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    private String closedBy; // userId od official who closed the issue
}
