package com.sidhant.civicpulse.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sidhant.civicpulse.dto.CreateIssueRequestDto;
import com.sidhant.civicpulse.dto.IssueResponseDto;
import com.sidhant.civicpulse.model.Department;
import com.sidhant.civicpulse.model.Issue;
import com.sidhant.civicpulse.model.Priority;
import com.sidhant.civicpulse.model.Role;
import com.sidhant.civicpulse.model.User;
import com.sidhant.civicpulse.repository.DepartmentRepository;
import com.sidhant.civicpulse.repository.IssueRepository;
import com.sidhant.civicpulse.repository.UserRepo;

@Service
public class IssueService {

    private UserRepo userRepo;
    private DepartmentRepository departmentRepository;
    private IssueRepository issueRepository;

    public IssueService(UserRepo userRepo, DepartmentRepository departmentRepository, IssueRepository issueRepository) {
        this.userRepo = userRepo;
        this.departmentRepository = departmentRepository;
        this.issueRepository = issueRepository;
    }

    public IssueResponseDto createIssue(CreateIssueRequestDto dto, String role) {

        // VALIDATION
        if (!"CITIZEN".equalsIgnoreCase(role.trim())) {
            throw new RuntimeException("Only Citizen can create issue.");
        }

        String description = dto.getDescription(); // getting description
        String desc = description.toLowerCase();// converting to lowercase
        String department;

        if (desc.contains("water")) {
            department = "WATER";
        } else if (desc.contains("light") || desc.contains("electric")) {
            department = "ELECTRICITY";
        } else if (desc.contains("garbage") || desc.contains("waste")) {
            department = "SANITATION";
        } else {
            department = "GENERAL";
        }

        Department departmentObj = departmentRepository.findByName(department);
        if (departmentObj == null) {
            throw new RuntimeException("Department not found");
        }

    User official = userRepo.findTopByDepartmentIdAndRoleOrderByLevelAsc(departmentObj.getId(), Role.OFFICIAL);
        if (official == null) {
            throw new RuntimeException("No official available for this  department");
        }

        Issue issue = new Issue();

        issue.setDescription(description);
        issue.setDepartment(departmentObj);
        issue.setAssignedTo(official);
        issue.setCreatedAt(LocalDateTime.now());

        if(dto.getPriority() == null){
            issue.setPriority(Priority.MEDIUM);
        }else{
            issue.setPriority(Priority.valueOf(dto.getPriority()));
        }
        issue.setIssueId(UUID.randomUUID().toString());
        issueRepository.save(issue);

        IssueResponseDto response = new IssueResponseDto();
        response.setMessage("Issue created Successfully!");
        response.setIssueId(issue.getIssueId());
        return response;
    }
}