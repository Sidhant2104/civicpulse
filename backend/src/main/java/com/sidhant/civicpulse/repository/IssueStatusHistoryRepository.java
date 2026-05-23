package com.sidhant.civicpulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sidhant.civicpulse.model.Issue;
import com.sidhant.civicpulse.model.IssueStatusHistory;

import java.util.List;

@Repository
public interface IssueStatusHistoryRepository extends JpaRepository<IssueStatusHistory, String> {

    IssueStatusHistory findTopByIssueOrderByUpdatedAtDesc(Issue issue);
    List<IssueStatusHistory> findByIssue(Issue issue);
}
