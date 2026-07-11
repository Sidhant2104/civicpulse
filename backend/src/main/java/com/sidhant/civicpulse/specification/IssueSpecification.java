package com.sidhant.civicpulse.specification;

import com.sidhant.civicpulse.model.Issue;
import com.sidhant.civicpulse.model.IssueStatus;
import com.sidhant.civicpulse.model.Priority;
import org.springframework.data.jpa.domain.Specification;

public class IssueSpecification {

    public static Specification<Issue> hasStatus(IssueStatus status){
        return (root, querry, criteriaBuilder)->{
            return criteriaBuilder.equal(root.get("status"),status);
        };
    }

    public static Specification<Issue> hasPriority(Priority priority){
        return(root, querry, criteriaBuilder)->{
            return criteriaBuilder.equal(root.get("priority"), priority);
        };
    }

    public static Specification<Issue> descriptionContains(String keyword) {

        return (root, query, criteriaBuilder) ->

                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + keyword.toLowerCase() + "%"
                );
    }

    public static Specification<Issue> hasDepartment(String department) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("department").get("name")),
                        department.toLowerCase()
                );
    }
}
