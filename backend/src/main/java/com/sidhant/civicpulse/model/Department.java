package com.sidhant.civicpulse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Department {
    @Id
    private String id;
    private String name;
    
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
