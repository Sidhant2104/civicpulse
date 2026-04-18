package com.sidhant.civicpulse.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
    public IssueResponseDto createIssue(@RequestBody CreateIssueRequestDto dto,
            @RequestHeader("X-USER-ROLE") String role) {
        return issueService.createIssue(dto, role);
    }

    // 2: Update Issue Status :
    @PatchMapping("/issue/{issueId}/status")
    public UpdateIssueStatusResponseDto updateIssueStatus(
            @PathVariable String issueId,
            @RequestBody UpdateIssueStatusRequestDto dto,
            @RequestHeader("X-USER-ID") String userId) {

        IssueStatus next = IssueStatus.valueOf(dto.getStatus());

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        issueService.updateIssueStatus(issueId, user, next);

        UpdateIssueStatusResponseDto response = new UpdateIssueStatusResponseDto();

        response.setMessage("Status updated");
        response.setIssueId(issueId);
        response.setNewStatus(next.toString());
        response.setUpdatedAt(LocalDateTime.now());

        return response;
    }

}
