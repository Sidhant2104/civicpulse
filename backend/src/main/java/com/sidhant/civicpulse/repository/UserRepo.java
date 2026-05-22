package com.sidhant.civicpulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sidhant.civicpulse.model.Level;
import com.sidhant.civicpulse.model.Role;
import com.sidhant.civicpulse.model.User;

@Repository
public interface UserRepo extends JpaRepository<User,String> {
    User findTopByDepartmentIdAndRoleOrderByLevelAsc(String departmentId, Role role);
    boolean existsByDepartmentIdAndLevelAndRole(
    String departmentId, Level level, Role role
);
    User findByDepartmentIdAndLevelAndRole(String DepartmentId, Level level, Role role);
}
