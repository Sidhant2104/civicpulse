package com.sidhant.civicpulse.service;

import com.sidhant.civicpulse.dto.CitizenDashboardResponseDto;
import com.sidhant.civicpulse.dto.OfficialDashboardResponseDto;
import com.sidhant.civicpulse.model.*;
import com.sidhant.civicpulse.repository.IssueRepository;
import com.sidhant.civicpulse.repository.IssueStatusHistoryRepository;
import com.sidhant.civicpulse.repository.UserRepo;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private UserRepo userRepo;
    private IssueRepository issueRepository;
    private IssueStatusHistoryRepository issueStatusHistoryRepository;

    DashboardService(UserRepo userRepo, IssueRepository issueRepository, IssueStatusHistoryRepository issueStatusHistoryRepository){
        this.userRepo = userRepo;
        this.issueRepository = issueRepository;
        this.issueStatusHistoryRepository = issueStatusHistoryRepository;
    }

    public CitizenDashboardResponseDto getCitizenDashboard(String email){
        User user = userRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

        if(user.getRole() != Role.CITIZEN){
            throw new RuntimeException("Only citizens can access citizen dashboard");
        }
        List<Issue> issues = issueRepository.findByCreatedBy(user);

        int totalIssues = issues.size();

        int openIssues = 0;
        int resolvedIssues = 0;
        int closedIssues = 0;
        int escalatedIssues = 0;
        int slaBreachedIssues = 0;

        for(Issue issue : issues){
            IssueStatusHistory latestHistory = issueStatusHistoryRepository.findTopByIssueOrderByUpdatedAtDesc(issue);

            if (latestHistory == null) {
                continue;
            }

            IssueStatus status = latestHistory.getStatus();
            switch (status) {

                case CREATED:
                    openIssues++;
                    break;

                case IN_PROGRESS:
                    openIssues++;
                    break;

                case ESCALATED:
                    openIssues++;
                    escalatedIssues++;
                    break;

                case RESOLVED:
                    resolvedIssues++;
                    break;

                case CLOSED:
                    closedIssues++;
                    break;

                case SLA_BREACHED:
                    slaBreachedIssues++;
                    break;
            }
        }

        CitizenDashboardResponseDto response =
                new CitizenDashboardResponseDto();

        response.setTotalIssues(totalIssues);
        response.setOpenIssues(openIssues);
        response.setResolvedIssues(resolvedIssues);
        response.setClosedIssues(closedIssues);
        response.setEscalatedIssues(escalatedIssues);
        response.setSlaBreachedIssues(slaBreachedIssues);

        return response;
    }


    public OfficialDashboardResponseDto getOfficialDashboard(String email){
        User user = userRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

        if(user.getRole() != Role.OFFICIAL){
            throw new RuntimeException("Only officials can access Official Dashboard");
        }

        List<Issue> issues = issueRepository.findByAssignedTo(user);

        int assignedIssues = issues.size();

        int inProgressIssues = 0;
        int resolvedIssues = 0;
        int escalatedIssues = 0;
        int closedIssues = 0;

        for(Issue issue : issues){
            IssueStatusHistory latestHistory = issueStatusHistoryRepository.findTopByIssueOrderByUpdatedAtDesc(issue);
            if(latestHistory == null){
                continue;
            }
             IssueStatus  status = latestHistory.getStatus();
            switch(status){
                case CREATED:
                case IN_PROGRESS:
                    inProgressIssues++;
                    break;

                case ESCALATED:
                    escalatedIssues++;
                    break;

                case RESOLVED:
                    resolvedIssues++;
                    break;

                case CLOSED:
                    closedIssues++;
                    break;
            }
        }

        OfficialDashboardResponseDto response = new OfficialDashboardResponseDto();
        response.setAssignedIssues(assignedIssues);
        response.setInProgressIssues(inProgressIssues);
        response.setResolvedIssues(resolvedIssues);
        response.setEscalatedIssues(escalatedIssues);
        response.setClosedIssues(closedIssues);

        return response;
    }
}
