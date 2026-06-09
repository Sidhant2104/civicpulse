package com.sidhant.civicpulse.controller;

import com.sidhant.civicpulse.dto.CitizenDashboardResponseDto;
import com.sidhant.civicpulse.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard/citizen")
    public CitizenDashboardResponseDto getCitizenDashboard(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        return dashboardService.getCitizenDashboard(email);
    }
}