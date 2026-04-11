package com.sidhant.civicpulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sidhant.civicpulse.model.Department;
@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
     Department findByName(String name);
}
