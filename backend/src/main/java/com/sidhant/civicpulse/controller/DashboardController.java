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
    public CitizenDashboardResponseDto getCitizenDashboard(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        return dashboardService.getCitizenDashboard(email);
    }

    //2: Official Dashboard Controller
    @GetMapping("/dashboard/official")
    public OfficialDashboardResponseDto getOfficialDashboard() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return dashboardService.getOfficialDashboard(email);
    }

    //3: Admin Dashboard Controller
    @GetMapping("/admin")
    public AdminDashboardResponseDto getAdminDashboard(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return dashboardService.getAdminDashboard(email);
    }

    //------------------------------------------------------------------------------------//

    //1: Get issue statistics:
    @GetMapping("/admin/stats/issues")
    public IssueStatisticsResponseDto getIssueStatistics(){
        return dashboardService.getIssueStatistics();
    }

    //2: Department Statistics
    @GetMapping("/admin/stats/departments")
    public List<DepartmentStatisticsResponseDto> getDepartmentStatistics(){
        return dashboardService.getDepartmentStatistics();
    }
}