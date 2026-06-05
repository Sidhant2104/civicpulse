package com.sidhant.civicpulse.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;

import com.sidhant.civicpulse.dto.CreateIssueRequestDto;
import com.sidhant.civicpulse.dto.IssueResponseDto;
import com.sidhant.civicpulse.dto.UpdateIssueStatusResponseDto;
import com.sidhant.civicpulse.model.Department;
import com.sidhant.civicpulse.model.Issue;
import com.sidhant.civicpulse.model.IssueStatus;
import com.sidhant.civicpulse.model.IssueStatusHistory;
import com.sidhant.civicpulse.model.Priority;
import com.sidhant.civicpulse.model.Role;
import com.sidhant.civicpulse.model.User;
import com.sidhant.civicpulse.repository.DepartmentRepository;
import com.sidhant.civicpulse.repository.IssueRepository;
import com.sidhant.civicpulse.repository.IssueStatusHistoryRepository;
import com.sidhant.civicpulse.repository.UserRepo;

@Service
public class IssueService {

    Map<IssueStatus, Set<IssueStatus>> transitions;

    private UserRepo userRepo;
    private DepartmentRepository departmentRepository;
    private IssueRepository issueRepository;
    private IssueStatusHistoryRepository issueStatusHistoryRepository;

    public IssueService(UserRepo userRepo, DepartmentRepository departmentRepository, IssueRepository issueRepository,
            IssueStatusHistoryRepository issueStatusHistoryRepository) {

        this.userRepo = userRepo;
        this.departmentRepository = departmentRepository;
        this.issueRepository = issueRepository;
        this.issueStatusHistoryRepository = issueStatusHistoryRepository;

        transitions = new HashMap<>();

        transitions.put(IssueStatus.CREATED, Set.of(IssueStatus.IN_PROGRESS, IssueStatus.ESCALATED));
        transitions.put(IssueStatus.IN_PROGRESS, Set.of(IssueStatus.RESOLVED, IssueStatus.ESCALATED));
        transitions.put(IssueStatus.ESCALATED, Set.of(IssueStatus.IN_PROGRESS, IssueStatus.RESOLVED));
        transitions.put(IssueStatus.RESOLVED, Set.of(IssueStatus.CLOSED));
    }

    private void validateTransition(IssueStatus current, IssueStatus next) {

        Set<IssueStatus> allowed = transitions.get(current);

        if (allowed == null || !allowed.contains(next)) {
            throw new RuntimeException("Invalid status transition");
        }
    }

    private void validateUser(Issue issue, User user, IssueStatus next) {

        if (user.getRole() == Role.ADMIN) {
            return;
        }

        if (user.getRole() == Role.OFFICIAL) {
            if (!issue.getAssignedTo().getId().equals(user.getId())) {
                throw new RuntimeException("Official not assigned to this issue");
            }
        }

        else if (user.getRole() == Role.CITIZEN) {
            if (!issue.getCreatedBy().getId().equals(user.getId())
                    || next != IssueStatus.CLOSED) {
                throw new RuntimeException("Citizen not allowed");
            }
        }
    }


    // Create Issue
    public IssueResponseDto createIssue(CreateIssueRequestDto dto, String email) {

        // VALIDATION
        User citizen = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (citizen.getRole() != Role.CITIZEN) {
            throw new RuntimeException("Only citizens can create issues");
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
        issue.setCreatedBy(citizen);

        if (dto.getPriority() == null) {
            issue.setPriority(Priority.MEDIUM);
        } else {
            issue.setPriority(Priority.valueOf(dto.getPriority()));
        }
        issue.setIssueId(UUID.randomUUID().toString());
        issueRepository.save(issue);
        IssueStatusHistory history = new IssueStatusHistory();

        history.setId(UUID.randomUUID().toString());
        history.setIssue(issue);
        history.setStatus(IssueStatus.CREATED);
        history.setUpdatedBy(citizen); // or creator
        history.setUpdatedAt(LocalDateTime.now());

        issueStatusHistoryRepository.save(history);

        IssueResponseDto response = new IssueResponseDto();
        response.setMessage("Issue created Successfully!");
        response.setIssueId(issue.getIssueId());
        return response;
    }

    // Issue Status Update System

    public UpdateIssueStatusResponseDto updateIssueStatus(String issueId, String email, IssueStatus next) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Issue not found"));
        IssueStatusHistory latest = issueStatusHistoryRepository.findTopByIssueOrderByUpdatedAtDesc(issue);
        IssueStatus currentStatus;
        if (latest == null) {
            currentStatus = IssueStatus.CREATED;
        } else {
            currentStatus = latest.getStatus();
        }

        validateTransition(currentStatus, next);
        validateUser(issue, user, next);

        IssueStatusHistory history = new IssueStatusHistory();

        history.setId(UUID.randomUUID().toString());
        history.setIssue(issue);
        history.setStatus(next);
        history.setUpdatedBy(user);
        history.setUpdatedAt(LocalDateTime.now());

        issueStatusHistoryRepository.save(history);

        UpdateIssueStatusResponseDto response = new UpdateIssueStatusResponseDto();

        response.setIssueId(issue.getIssueId());
        response.setMessage("Status updated successfully");
        response.setNewStatus(next.toString());
        response.setUpdatedAt(LocalDateTime.now());

        return response;

    }

    // Get Issue History by IssueID
    public List<IssueStatusHistory> getIssueHistory(String issueId){
        Issue issue = issueRepository.findById(issueId).orElseThrow(()->new RuntimeException("Issue Not Found"));
        List<IssueStatusHistory> issueHistory =
                issueStatusHistoryRepository.findByIssue(issue);
        return issueHistory;
    }

    // Get all issues created by currently logged-in citizen
    public List<Issue> getMyIssues(String email){
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate that only citizens can access this API
        if(user.getRole() != Role.CITIZEN){
            throw new RuntimeException(
                    "Only citizens can access their issues"
            );
        }

        // Fetch all issues created by this citizen
        List<Issue> issues = issueRepository.findByCreatedBy(user);

        // Return citizen's issues
        return issues;
    }

    // Get all issues assigned to the current official
    public List<Issue> getAssignedIssues(String email){

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getRole() != Role.OFFICIAL){
            throw new RuntimeException(
                    "Only officials can access issues assigned to them"
            );
        }

        List<Issue> issues = issueRepository.findByAssignedTo(user);

        return issues;
    }

    // Get all issues in the system for admin
    public List<Issue> getAllIssues(String email){

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getRole() != Role.ADMIN){
            throw new RuntimeException(
                    "Only admins can access all issues"
            );
        }

        return issueRepository.findAll();
    }

    // Get single issue details by issueId
    public Issue getIssueById(String issueId){

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(
                        () -> new RuntimeException("Issue not found")
                );

        return issue;
    }

    // Get all Issues with ESCALATED status
    public List<Issue> getIssueWithEscalatedStatus(){
        List<Issue> allIssues = issueRepository.findAll();
        List<Issue> escalatedIssues = new ArrayList<>();
        for(Issue  currIssue : allIssues){
            IssueStatusHistory latestHistory = issueStatusHistoryRepository
                    .findTopByIssueOrderByUpdatedAtDesc(currIssue);

            if(latestHistory != null &&
                    latestHistory.getStatus() == IssueStatus.ESCALATED)
            {
                escalatedIssues.add(currIssue);
            }
        }
        return escalatedIssues;
    }
}