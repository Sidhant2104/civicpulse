package com.sidhant.civicpulse.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.sidhant.civicpulse.dto.*;
import com.sidhant.civicpulse.model.Issue;
import com.sidhant.civicpulse.model.IssueStatusHistory;
import com.sidhant.civicpulse.model.Priority;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.sidhant.civicpulse.model.IssueStatus;
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
    @PostMapping(value = "/issue", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<IssueResponseDto> createIssue(
            @Valid @ModelAttribute CreateIssueRequestDto dto) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        IssueResponseDto response =
                issueService.createIssue(dto, email);

        return ApiResponse.success(
                "Issue created successfully",
                response
        );
    }

    // 2: Update Issue Status :
    @PatchMapping("/issue/{issueId}/status")
    public ApiResponse<UpdateIssueStatusResponseDto> updateIssueStatus(
            @PathVariable String issueId,
            @RequestBody UpdateIssueStatusRequestDto dto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        IssueStatus next = IssueStatus.valueOf(dto.getStatus());

        issueService.updateIssueStatus(issueId, email, next);

        UpdateIssueStatusResponseDto response = new UpdateIssueStatusResponseDto();
        response.setIssueId(issueId);
        response.setNewStatus(next.toString());
        response.setUpdatedAt(LocalDateTime.now());

        return ApiResponse.success(
                "Issue status updated successfully",
                response
        );
    }

    // 3. Get Issue History by issueId
    @GetMapping("/issue/{id}/history")
    public ApiResponse<List<IssueStatusHistory>> getIssueHistory(@PathVariable String id){
        List<IssueStatusHistory> history = issueService.getIssueHistory(id);
        return ApiResponse.success("Issue history fetched successfully", history);
    }

    // 4. Get all issues of currently logged-in citizen
    @GetMapping("/issue/my")
    public ApiResponse<Page<Issue>> getMyIssues(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC
    )Pageable pageable){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ApiResponse.success("Issues fetched successfully",issueService.getMyIssues(email,pageable));
    }

    // 5. Get all issues assigned to the current official
    @GetMapping("/issue/assigned")
    public ApiResponse<Page<Issue>> getAssignedIssues(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return ApiResponse.success("Assigned issues fetched successfully",issueService.getAssignedIssues(email, pageable));
    }

    // 6. Get all issues in the system
    @GetMapping("/issue/all")
    public ApiResponse<Page<Issue>> getAllIssues(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) IssueStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) String department,
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return ApiResponse.success(
                "All issues fetched successfully",
                issueService.getAllIssues(email, search, status, priority, department, pageable)
        );
    }



    // 7. Get single issue details
    @GetMapping("/issue/{id}")
    public ApiResponse<Issue> getIssueById(@PathVariable String id){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return ApiResponse.success(
                "Issue fetched successfully",
                issueService.getIssueById(id, email)
        );
    }

    // 8. Get all issues whose latest status is ESCALATED
    @GetMapping("/issue/escalated")
    public ApiResponse<List<Issue>> getIssueWithEscalatedStatus(){

        List<Issue> escalatedIssues =
                issueService.getIssueWithEscalatedStatus();

        return ApiResponse.success(
                "Escalated issues fetched successfully",
                escalatedIssues
        );
    }


    //Review Issues
    @PatchMapping("/issue/{issueId}/review")
    public ApiResponse<UpdateIssueStatusResponseDto> reviewIssue(
            @PathVariable String issueId,
            @Valid @RequestBody ReviewIssueRequestDto dto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return ApiResponse.success(
                "Issue reviewed successfully",
                issueService.reviewIssue(issueId, email, dto.isApproved())
        );
    }
}
