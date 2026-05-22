package com.sidhant.civicpulse.service;

import com.sidhant.civicpulse.model.*;
import com.sidhant.civicpulse.repository.IssueRepository;
import com.sidhant.civicpulse.repository.IssueStatusHistoryRepository;
import com.sidhant.civicpulse.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EscalationSchedulerService {

    private static final long SLA_MINUTES = 15;

    private static final Logger logger = LoggerFactory.getLogger(EscalationSchedulerService.class);

    IssueRepository issueRepository;
    IssueStatusHistoryRepository issueStatusHistoryRepository;
    UserRepo userRepo;

    EscalationSchedulerService(IssueRepository issueRepository, IssueStatusHistoryRepository issueStatusHistoryRepository, UserRepo userRepo) {
        this.issueRepository = issueRepository;
        this.issueStatusHistoryRepository = issueStatusHistoryRepository;
        this.userRepo = userRepo;
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void checkEscalations() {

        logger.info("Scheduler Running...");

        List<Issue> issues = issueRepository.findAll();

        for (Issue issue : issues) {

            IssueStatusHistory latestStatusHistory = issueStatusHistoryRepository.findTopByIssueOrderByUpdatedAtDesc(issue);

            // Skip resolved/closed/breached issues
            if (latestStatusHistory.getStatus() == IssueStatus.CLOSED || latestStatusHistory.getStatus() == IssueStatus.RESOLVED || latestStatusHistory.getStatus() == IssueStatus.SLA_BREACHED) {
                continue;
            }

            LocalDateTime currentTime = LocalDateTime.now();

            LocalDateTime updatedAt = latestStatusHistory.getUpdatedAt();

            long minutes = Duration.between(updatedAt, currentTime).toMinutes();

            if (minutes > SLA_MINUTES) {

                User assignedUser = issue.getAssignedTo();

                Level currentLevel = assignedUser.getLevel();

                int currentOrdinal = currentLevel.ordinal();

                int nextOrdinal = currentOrdinal + 1;

                Level[] levels = Level.values();

                if (nextOrdinal < levels.length) {

                    Level nextLevel = levels[nextOrdinal];

                    User nextOfficial = userRepo.findByDepartmentIdAndLevelAndRole(assignedUser.getDepartmentId(), nextLevel, Role.OFFICIAL);

                    if (nextOfficial != null) {

                        issue.setAssignedTo(nextOfficial);

                        issueRepository.save(issue);

                        createHistoryEntry(issue, IssueStatus.ESCALATED);

                        logger.info("Issue {} escalated from {} to {}", issue.getIssueId(), currentLevel, nextLevel);

                    } else {

                        logger.warn("No official found for department {} and level {}", assignedUser.getDepartmentId(), nextLevel);
                    }

                } else {

                    createHistoryEntry(issue, IssueStatus.SLA_BREACHED);

                    logger.warn("Issue {} reached SLA_BREACHED at level {}", issue.getIssueId(), currentLevel);
                }

            } else {

                logger.info("No escalation needed for issue {}", issue.getIssueId());
            }
        }
    }

    public void createHistoryEntry(Issue issue, IssueStatus issueStatus) {

        IssueStatusHistory history = new IssueStatusHistory();

        history.setIssue(issue);

        history.setStatus(issueStatus);

        history.setUpdatedAt(LocalDateTime.now());

        history.setUpdatedBy(null);

        history.setId(UUID.randomUUID().toString());

        issueStatusHistoryRepository.save(history);
    }
}