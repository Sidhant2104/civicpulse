package com.sidhant.civicpulse.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private String id;

    private String name;
    private String email;
    private String phoneNo;
    private String departmentId;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Level level;
    private LocalDateTime createdAt;
    public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getPhoneNo() {
    return phoneNo;
}

public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
}

public String getDepartmentId() {
    return departmentId;
}

public void setDepartmentId(String departmentId) {
    this.departmentId = departmentId;
}

public Role getRole() {
    return role;
}

public void setRole(Role role) {
    this.role = role;
}

public Level getLevel() {
    return level;
}

public void setLevel(Level level) {
    this.level = level;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}
}
