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

    
}
