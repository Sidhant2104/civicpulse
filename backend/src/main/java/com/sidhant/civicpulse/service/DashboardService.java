package com.sidhant.civicpulse.service;

import com.sidhant.civicpulse.dto.*;
import com.sidhant.civicpulse.exception.ForbiddenException;
import com.sidhant.civicpulse.exception.NotFoundException;
import com.sidhant.civicpulse.model.*;
import com.sidhant.civicpulse.repository.DepartmentRepository;
import com.sidhant.civicpulse.repository.IssueRepository;
import com.sidhant.civicpulse.repository.IssueStatusHistoryRepository;
import com.sidhant.civicpulse.repository.UserRepo;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private UserRepo userRepo;
    private IssueRepository issueRepository;
    private IssueStatusHistoryRepository issueStatusHistoryRepository;
    private DepartmentRepository departmentRepository;

    DashboardService(UserRepo userRepo, IssueRepository issueRepository, IssueStatusHistoryRepository issueStatusHistoryRepository, DepartmentRepository departmentRepository){
        this.userRepo = userRepo;
        this.issueRepository = issueRepository;
        this.issueStatusHistoryRepository = issueStatusHistoryRepository;
        this.departmentRepository = departmentRepository;
    }

    // 1: Citizen Dashboard
    public CitizenDashboardResponseDto getCitizenDashboard(String email){
        User user = userRepo.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        if(user.getRole() != Role.CITIZEN){
            throw new ForbiddenException("Only citizens can access citizen dashboard");
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

    // 2: Official Dashboard
    public OfficialDashboardResponseDto getOfficialDashboard(String email){
        User user = userRepo.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        if(user.getRole() != Role.OFFICIAL){
            throw new ForbiddenException("Only officials can access Official Dashboard");
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

    //3: Admin Dashboard
    public AdminDashboardResponseDto getAdminDashboard(String email){
        User user = userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));

        List<Issue> issues = issueRepository.findAll();
        int totalIssues = issues.size();

        int openIssues = 0;
        int closedIssues = 0;
        int escalatedIssues = 0;
        int slaBreachedIssues = 0;
        int totalCitizens = 0;
        int totalOfficials = 0;
        int totalDepartments= 0;

        for(Issue issue : issues){

            IssueStatusHistory latestHistory =
                    issueStatusHistoryRepository
                            .findTopByIssueOrderByUpdatedAtDesc(issue);

            if(latestHistory == null){
                continue;
            }

            IssueStatus status = latestHistory.getStatus();

            switch(status){

                case CREATED:
                case IN_PROGRESS:
                    openIssues++;
                    break;

                case ESCALATED:
                    escalatedIssues++;
                    break;

                case CLOSED:
                    closedIssues++;
                    break;

                case SLA_BREACHED:
                    slaBreachedIssues++;
                    break;
            }
        }

        totalCitizens =
                (int) userRepo.findByRole(Role.CITIZEN).size();

        totalOfficials =
                (int) userRepo.findByRole(Role.OFFICIAL).size();

        totalDepartments =
                departmentRepository.findAll().size();

        AdminDashboardResponseDto response =
                new AdminDashboardResponseDto();

        response.setTotalIssues(totalIssues);
        response.setOpenIssues(openIssues);
        response.setClosedIssues(closedIssues);
        response.setEscalatedIssues(escalatedIssues);
        response.setSlaBreachedIssues(slaBreachedIssues);

        response.setTotalCitizens(totalCitizens);
        response.setTotalOfficials(totalOfficials);
        response.setTotalDepartments(totalDepartments);
        return response;
    }

//--------------------------------------------------------------------------------------
    //1: Issue Statistics
    public IssueStatisticsResponseDto getIssueStatistics(){
        List<Issue> issues = issueRepository.findAll();
        int totalIssues = issues.size();

        int openIssues = 0;
        int resolvedIssues = 0;
        int closedIssues = 0;
        int escalatedIssues = 0;
        int slaBreachedIssues = 0;

        for(Issue issue : issues){

            IssueStatusHistory latestHistory =
                    issueStatusHistoryRepository
                            .findTopByIssueOrderByUpdatedAtDesc(issue);

            if(latestHistory == null){
                continue;
            }

            IssueStatus status = latestHistory.getStatus();

            switch(status){

                case CREATED:
                case IN_PROGRESS:
                    openIssues++;
                    break;

                case RESOLVED:
                    resolvedIssues++;
                    break;

                case CLOSED:
                    closedIssues++;
                    break;

                case ESCALATED:
                    escalatedIssues++;
                    break;

                case SLA_BREACHED:
                    slaBreachedIssues++;
                    break;
            }
        }

        IssueStatisticsResponseDto response =
                new IssueStatisticsResponseDto();

        response.setTotalIssues(totalIssues);
        response.setOpenIssues(openIssues);
        response.setResolvedIssues(resolvedIssues);
        response.setClosedIssues(closedIssues);
        response.setEscalatedIssues(escalatedIssues);
        response.setSlaBreachedIssues(slaBreachedIssues);
        return response;
    }

    //2: Department Statistics
    public List<DepartmentStatisticsResponseDto> getDepartmentStatistics(){
        List<Department> departments= departmentRepository.findAll();

        List<DepartmentStatisticsResponseDto> response = new ArrayList<>();

        List<Issue> issues = issueRepository.findAll();

        for(Department department: departments){
            int totalIssues = 0;
            int openIssues = 0;
            int resolvedIssues = 0;
            int closedIssues = 0;
            int escalatedIssues = 0;
            int slaBreachedIssues = 0;


            for(Issue issue : issues){
                if(!issue.getDepartment().getId().equals(department.getId())){
                    continue;
                }
                totalIssues++;

                IssueStatusHistory latestHistory = issueStatusHistoryRepository.findTopByIssueOrderByUpdatedAtDesc(issue);
                if(latestHistory == null){
                    continue;
                }
                IssueStatus status = latestHistory.getStatus();

                switch(status){

                    case CREATED:
                    case IN_PROGRESS:
                        openIssues++;
                        break;

                    case RESOLVED:
                        resolvedIssues++;
                        break;

                    case CLOSED:
                        closedIssues++;
                        break;

                    case ESCALATED:
                        escalatedIssues++;
                        break;

                    case SLA_BREACHED:
                        slaBreachedIssues++;
                        break;
                }
            }

            DepartmentStatisticsResponseDto dto =
                    new DepartmentStatisticsResponseDto();

            dto.setDepartmentName(department.getName());

            dto.setTotalIssues(totalIssues);
            dto.setOpenIssues(openIssues);
            dto.setResolvedIssues(resolvedIssues);
            dto.setClosedIssues(closedIssues);
            dto.setEscalatedIssues(escalatedIssues);
            dto.setSlaBreachedIssues(slaBreachedIssues);

            double resolutionRate = 0;

            if(totalIssues > 0){
                resolutionRate = ((double)(resolvedIssues + closedIssues) / totalIssues) * 100;
            }

            double slaComplianceRate;

            if(totalIssues == 0){
                slaComplianceRate = 100.0;
            }
            else{
                slaComplianceRate = ((double)(totalIssues - slaBreachedIssues) / totalIssues) * 100;
            }

            dto.setResolutionRate(resolutionRate);
            dto.setSlaComplianceRate(slaComplianceRate);

            response.add(dto);
        }

        return response;
    }


    // 3: Escalation Statistics
    public EscalationStatisticsResponseDto getEscalationStatistics(){
        List<IssueStatusHistory> histories =
                issueStatusHistoryRepository.findAll();
        int totalEscalatedIssues = 0;

        int level1To2Escalations = 0;
        int level2To3Escalations = 0;
        int level3To4Escalations = 0;
        for(IssueStatusHistory history : histories){
            if(history.getStatus() != IssueStatus.ESCALATED){
                continue;
            }
            totalEscalatedIssues++;

            Level fromLevel = history.getFromLevel();
            Level toLevel = history.getToLevel();

            if(fromLevel == Level.LEVEL_1 &&
                    toLevel == Level.LEVEL_2){

                level1To2Escalations++;
            }

            else if(fromLevel == Level.LEVEL_2 &&
                    toLevel == Level.LEVEL_3){

                level2To3Escalations++;
            }

            else if(fromLevel == Level.LEVEL_3 &&
                    toLevel == Level.LEVEL_4){

                level3To4Escalations++;
            }
        }
        EscalationStatisticsResponseDto response =
                new EscalationStatisticsResponseDto();
        response.setTotalEscalatedIssues(totalEscalatedIssues);

        response.setLevel1To2Escalations(level1To2Escalations);
        response.setLevel2To3Escalations(level2To3Escalations);
        response.setLevel3To4Escalations(level3To4Escalations);
        return response;
    }
}
