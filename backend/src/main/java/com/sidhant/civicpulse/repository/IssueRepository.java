package com.sidhant.civicpulse.repository;

import com.sidhant.civicpulse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sidhant.civicpulse.model.Issue;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, String> {
    List<Issue> findByCreatedBy(User user);
    List<Issue> findByAssignedTo(User user);
}