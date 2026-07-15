package com.sidhant.civicpulse.service;

import java.time.LocalDateTime;
import java.util.*;

import com.sidhant.civicpulse.exception.BadRequestException;
import com.sidhant.civicpulse.exception.ForbiddenException;
import com.sidhant.civicpulse.exception.NotFoundException;
import com.sidhant.civicpulse.exception.ResourceUnavailableException;
import com.sidhant.civicpulse.specification.IssueSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final CloudinaryService cloudinaryService;

    public IssueService(UserRepo userRepo, DepartmentRepository departmentRepository, IssueRepository issueRepository,
            IssueStatusHistoryRepository issueStatusHistoryRepository, CloudinaryService cloudinaryService) {

        this.userRepo = userRepo;
        this.departmentRepository = departmentRepository;
        this.issueRepository = issueRepository;
        this.issueStatusHistoryRepository = issueStatusHistoryRepository;
        this.cloudinaryService=cloudinaryService;

        transitions = new HashMap<>();

        transitions.put(IssueStatus.CREATED, Set.of(IssueStatus.IN_PROGRESS, IssueStatus.ESCALATED));
        transitions.put(IssueStatus.IN_PROGRESS, Set.of(IssueStatus.RESOLVED, IssueStatus.ESCALATED));
        transitions.put(IssueStatus.ESCALATED, Set.of(IssueStatus.IN_PROGRESS, IssueStatus.RESOLVED));
        transitions.put(IssueStatus.RESOLVED, Set.of(IssueStatus.CLOSED, IssueStatus.REOPENED));
        transitions.put(IssueStatus.SLA_BREACHED, Set.of(IssueStatus.RESOLVED));
        transitions.put(IssueStatus.REOPENED, Set.of(IssueStatus.IN_PROGRESS));
    }



    private void validateTransition(IssueStatus current, IssueStatus next) {

        Set<IssueStatus> allowed = transitions.get(current);

        if (allowed == null || !allowed.contains(next)) {
            throw new BadRequestException("Invalid status transition");
        }
    }

    private void validateUser(Issue issue, User user, IssueStatus next) {

        if (user.getRole() == Role.ADMIN) {
            return;
        }

        if (user.getRole() == Role.OFFICIAL) {
            if (!issue.getAssignedTo().getId().equals(user.getId())) {
                throw new ForbiddenException("Official not assigned to this issue");
            }
            return;
        }

        if (user.getRole() == Role.CITIZEN) {
            if (!issue.getCreatedBy().getId().equals(user.getId())
                    || (next != IssueStatus.CLOSED && next != IssueStatus.REOPENED)) {
                throw new ForbiddenException("Citizen not allowed");
            }
            return;
        }
    }


    // Create Issue
    public IssueResponseDto createIssue(CreateIssueRequestDto dto, String email){

        // VALIDATION
        User citizen = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));


        if (citizen.getRole() != Role.CITIZEN) {
            throw new ForbiddenException("Only citizens can create issues");
        }

        // Upload image first
        String imageUrl = null;

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(dto.getImage());
        }

        String description = dto.getDescription();
        String desc = description.toLowerCase();

        String department;

        if (desc.contains("water")
                || desc.contains("pipeline")
                || desc.contains("leakage")) {

            department = "WATER";

        } else if (desc.contains("light")
                || desc.contains("electric")
                || desc.contains("pole")
                || desc.contains("power")) {

            department = "ELECTRICITY";

        } else if (desc.contains("road")
                || desc.contains("pothole")
                || desc.contains("street")
                || desc.contains("highway")
                || desc.contains("crack")) {

            department = "ROADS";

        } else if (desc.contains("garbage")
                || desc.contains("waste")
                || desc.contains("dustbin")
                || desc.contains("cleaning")) {

            department = "SANITATION";

        } else {

            department = "GENERAL";
        }

        Department departmentObj = departmentRepository.findByName(department);
        if (departmentObj == null) {
            throw new NotFoundException("Department not found");
        }

        User official = userRepo.findTopByDepartmentIdAndRoleOrderByLevelAsc(departmentObj.getId(), Role.OFFICIAL);
        if (official == null) {
            throw new ResourceUnavailableException("No official available for this  department");
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

        issue.setImageUrl(imageUrl);

        issueStatusHistoryRepository.save(history);

        IssueResponseDto response = new IssueResponseDto();
        response.setIssueId(issue.getIssueId());
        response.setImageUrl(issue.getImageUrl());
        return response;
    }

    // Issue Status Update System
    public UpdateIssueStatusResponseDto updateIssueStatus(String issueId, String email, IssueStatus next) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("Issue not found"));
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

        if (next == IssueStatus.RESOLVED) {

            issue.setResolvedAt(LocalDateTime.now());
            issue.setResolvedBy(user);
            issueRepository.save(issue);
        }

        if (next == IssueStatus.REOPENED) {

            issue.setResolvedAt(null);
            issue.setResolvedBy(null);

            issueRepository.save(issue);
        }

        if (next == IssueStatus.CLOSED) {
            issue.setClosedAt(LocalDateTime.now());
            issue.setClosedBy(user.getId());

            issueRepository.save(issue);
        }
        issueStatusHistoryRepository.save(history);

        UpdateIssueStatusResponseDto response = new UpdateIssueStatusResponseDto();

        response.setIssueId(issue.getIssueId());
        response.setNewStatus(next.toString());
        response.setUpdatedAt(LocalDateTime.now());

        return response;

    }

    // Get Issue History by IssueID
    public List<IssueStatusHistory> getIssueHistory(String issueId){
        Issue issue = issueRepository.findById(issueId).orElseThrow(()->new NotFoundException("Issue not found"));
        List<IssueStatusHistory> issueHistory =
                issueStatusHistoryRepository.findByIssue(issue);
        return issueHistory;
    }

    // Get all issues created by currently logged-in citizen
    public Page<Issue> getMyIssues(String email, Pageable pageable){
        validatePageable(pageable);
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        // Validate that only citizens can access this API
        if(user.getRole() != Role.CITIZEN){
            throw new ForbiddenException(
                    "Only citizens can access their issues"
            );
        }
        // Fetch all issues created by this citizen
        Page<Issue> issues = issueRepository.findByCreatedBy(user, pageable);
        // Return citizen's issues
        return issues;
    }

    // Get all issues assigned to the current official
    public Page<Issue> getAssignedIssues(String email, Pageable pageable){
        validatePageable(pageable);
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if(user.getRole() != Role.OFFICIAL){
            throw new ForbiddenException("Only officials can access issues assigned to them");
        }
        Page<Issue> issues = issueRepository.findByAssignedTo(user,pageable);

        return issues;
    }

    // Get all issues in the system for admin
    public Page<Issue> getAllIssues(String email, String search, IssueStatus status, Priority priority,
            String department,
            Pageable pageable) {

        validatePageable(pageable);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new ForbiddenException(
                    "Only admins can access all issues"
            );
        }

        Specification<Issue> spec = Specification.allOf();
//        if (status != null) {
//            spec = spec.and(IssueSpecification.hasStatus(status));
//        }

        if (priority != null) {
            spec = spec.and(IssueSpecification.hasPriority(priority));
        }

        if (search != null && !search.isBlank()) {
            spec = spec.and(IssueSpecification.descriptionContains(search));
        }

        if (department != null && !department.isBlank()) {
            spec = spec.and(IssueSpecification.hasDepartment(department));
        }

        return issueRepository.findAll(spec, pageable);
    }

    // Get single issue details by issueId
    public Issue getIssueById(String issueId, String email){

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Issue issue = issueRepository.findById(issueId).orElseThrow(() ->
                        new NotFoundException("Issue not found"));
        if (user.getRole() == Role.ADMIN) {
            return issue;
        }

        if (user.getRole() == Role.OFFICIAL) {
            if (!issue.getAssignedTo().getId().equals(user.getId())) {
                throw new ForbiddenException("Access denied");
            }
            return issue;
        }

        if (user.getRole() == Role.CITIZEN) {
            if (!issue.getCreatedBy().getId().equals(user.getId())) {
                throw new ForbiddenException("Access denied");
            }
            return issue;
        }
        throw new ForbiddenException("Access denied");

    }

    // Get all Issues with ESCALATED status
    public List<Issue> getIssueWithEscalatedStatus(){
        List<Issue> allIssues = issueRepository.findAll();
        List<Issue> escalatedIssues = new ArrayList<>();
        for(Issue  currIssue : allIssues){
            IssueStatusHistory latestHistory = issueStatusHistoryRepository
                    .findTopByIssueOrderByUpdatedAtDesc(currIssue);

            if(latestHistory != null && latestHistory.getStatus() == IssueStatus.ESCALATED)
            {
                escalatedIssues.add(currIssue);
            }
        }
        return escalatedIssues;
    }

    // Review Issue
    public UpdateIssueStatusResponseDto reviewIssue(
            String issueId,
            String email,
            boolean approved) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRole() != Role.CITIZEN) {
            throw new ForbiddenException("Only citizens can review resolved issues");
        }

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("Issue not found"));

        if (!issue.getCreatedBy().getId().equals(user.getId())) {
            throw new ForbiddenException("You can only review your own issues");
        }

        IssueStatusHistory latest =
                issueStatusHistoryRepository.findTopByIssueOrderByUpdatedAtDesc(issue);

        if (latest == null || latest.getStatus() != IssueStatus.RESOLVED) {
            throw new ForbiddenException("Only resolved issues can be reviewed");
        }

        IssueStatus nextStatus = approved
                ? IssueStatus.CLOSED
                : IssueStatus.REOPENED;

        return updateIssueStatus(issueId, email, nextStatus);
    }


    private void validatePageable(Pageable pageable) {

        if (pageable.getPageSize() > 50) {
            throw new BadRequestException("Page size cannot exceed 50.");
        }

    }
}