package com.sidhant.civicpulse.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private String id;

    private String name;
    private String email;
    private String phoneNo;
    private String departmentId;
    private Role role;
    private Level level;
    private LocalDateTime createdAt;
    
}
