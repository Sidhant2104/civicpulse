package com.sidhant.civicpulse.repository;

import com.sidhant.civicpulse.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sidhant.civicpulse.model.Issue;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, String> {
    Page<Issue> findByCreatedBy(User user, Pageable pageable);
    List<Issue> findByCreatedBy(User user);
    List<Issue> findByAssignedTo(User user);
    Page<Issue> findByAssignedTo(User user, Pageable pageable);
}