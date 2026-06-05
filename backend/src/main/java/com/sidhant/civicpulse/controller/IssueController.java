package com.sidhant.civicpulse.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.sidhant.civicpulse.model.Issue;
import com.sidhant.civicpulse.model.IssueStatusHistory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.sidhant.civicpulse.dto.CreateIssueRequestDto;
import com.sidhant.civicpulse.dto.IssueResponseDto;
import com.sidhant.civicpulse.dto.UpdateIssueStatusRequestDto;
import com.sidhant.civicpulse.dto.UpdateIssueStatusResponseDto;
import com.sidhant.civicpulse.model.IssueStatus;
import com.sidhant.civicpulse.model.User;
import com.sidhant.civicpulse.repository.UserRepo;
import com.sidhant.civicpulse.service.IssueService;

@RestController
public class IssueController {

    private IssueService issueService;
    private UserRepo userRepo;

    public IssueController(IssueService issueService, UserRepo userRepo) {
        this.issueService = issueService;
        this.userRepo = userRepo;
    }

    // 1: Create Issue
    @PostMapping("/issue")
    public IssueResponseDto createIssue(
            @RequestBody CreateIssueRequestDto dto) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return issueService.createIssue(dto, email);
    }

    // 2: Update Issue Status :
    @PatchMapping("/issue/{issueId}/status")
    public UpdateIssueStatusResponseDto updateIssueStatus(
            @PathVariable String issueId,
            @RequestBody UpdateIssueStatusRequestDto dto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        IssueStatus next = IssueStatus.valueOf(dto.getStatus());

        issueService.updateIssueStatus(issueId, email, next);

        UpdateIssueStatusResponseDto response = new UpdateIssueStatusResponseDto();

        response.setMessage("Status updated");
        response.setIssueId(issueId);
        response.setNewStatus(next.toString());
        response.setUpdatedAt(LocalDateTime.now());

        return response;
    }

    // 3. Get Issue History by issueId
    @GetMapping("/issue/{id}/history")
    public List<IssueStatusHistory> getIssueHistory(@PathVariable String id){
        List<IssueStatusHistory> history = issueService.getIssueHistory(id);
        return history;
    }

    // 4. Get all issues of currently logged-in citizen
    @GetMapping("/issue/my")
    public List<Issue> getMyIssues(){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return issueService.getMyIssues(email);
    }

    // 5. Get all issues assigned to the current official
    @GetMapping("/issue/assigned")
    public List<Issue> getAssignedIssues() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return issueService.getAssignedIssues(email);
    }

    // 6. Get all issues in the system
    @GetMapping("/issue/all")
    public List<Issue> getAllIssues() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return issueService.getAllIssues(email);
    }



    // 7. Get single issue details
    @GetMapping("/issue/{id}")
    public Issue getIssueById(@PathVariable String id){

        Issue issue = issueService.getIssueById(id);

        return issue;
    }

    // 8. Get all issues whose latest status is ESCALATED
    @GetMapping("/issue/escalated")
    public List<Issue> getIssueWithEscalatedStatus(){

        List<Issue> escalatedIssues =
                issueService.getIssueWithEscalatedStatus();

        return escalatedIssues;
    }
}
