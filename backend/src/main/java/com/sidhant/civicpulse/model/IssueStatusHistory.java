package com.sidhant.civicpulse.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class IssueStatusHistory {

    @Id
    private String id;

    @ManyToOne
    private Issue issue;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @ManyToOne
    private User updatedBy;

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Level fromLevel;

    @Enumerated(EnumType.STRING)
    private Level toLevel;

}
