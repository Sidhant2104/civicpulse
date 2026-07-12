package com.sidhant.civicpulse.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Issue {
    @Id
    private String issueId;
    private String description;
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    private Department department;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    private User assignedTo;

    @ManyToOne
    private User createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    private String closedBy; // userId od official who closed the issue

    private LocalDateTime resolvedAt;
    @ManyToOne
    private User resolvedBy;

}
