package com.sidhant.civicpulse.controller;

import com.sidhant.civicpulse.dto.*;
import com.sidhant.civicpulse.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DashboardController {

    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    //1:Citizen Dashboard Controller
    @GetMapping("/dashboard/citizen")
    public ApiResponse<CitizenDashboardResponseDto> getCitizenDashboard(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        return ApiResponse.success(
                "Citizen dashboard fetched successfully",
                dashboardService.getCitizenDashboard(email)
        );
    }

    //2: Official Dashboard Controller
    @GetMapping("/dashboard/official")
    public ApiResponse<OfficialDashboardResponseDto> getOfficialDashboard() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ApiResponse.success(
                "Official dashboard fetched successfully",
                dashboardService.getOfficialDashboard(email)
        );
    }

    //3: Admin Dashboard Controller
    @GetMapping("/admin")
    public ApiResponse<AdminDashboardResponseDto> getAdminDashboard(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ApiResponse.success(
                "Admin dashboard fetched successfully",
                dashboardService.getAdminDashboard(email)
        );
    }

    //------------------------------------------------------------------------------------//

    //1: Get issue statistics:
    @GetMapping("/admin/stats/issues")
    public ApiResponse<IssueStatisticsResponseDto> getIssueStatistics(){
        return ApiResponse.success(
                "Issue statistics fetched successfully",
                dashboardService.getIssueStatistics()
        );
    }

    //2: Department Statistics
    @GetMapping("/admin/stats/departments")
    public ApiResponse<List<DepartmentStatisticsResponseDto>> getDepartmentStatistics(){
        return ApiResponse.success(
                "Department statistics fetched successfully",
                dashboardService.getDepartmentStatistics()
        );
    }

    //3: Escalation Statistics
    @GetMapping("/admin/stats/escalations")
    public ApiResponse<EscalationStatisticsResponseDto> getEscalationStatistics(){
        return ApiResponse.success(
                "Escalation statistics fetched successfully",
                dashboardService.getEscalationStatistics()
        );
    }
}