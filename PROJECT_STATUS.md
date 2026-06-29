# CivicPulse Feature Tracker

## ✅ Completed Features

### Authentication & Security

* [x] User Signup
* [x] User Login
* [x] BCrypt Password Encoding
* [x] JWT Token Generation
* [x] JWT Validation
* [x] JwtAuthenticationFilter
* [x] Spring Security Integration
* [x] UserDetails Integration
* [x] Role-Based Authorization
* [x] Secure Issue Access Control

### User Management

* [x] Citizen Registration
* [x] Department Hierarchy
* [x] Level Hierarchy (LEVEL_1 → LEVEL_4)

### Issue Management

* [x] Create Issue
* [x] Auto Department Detection
* [x] Auto Official Assignment
* [x] Priority Handling
* [x] Issue History Tracking
* [x] Issue Status Updates
* [x] Get Single Issue API
* [x] Get Escalated Issues API
* [x] Issue Resolution Tracking (resolvedAt, resolvedBy)
* [x] Issue Closure Tracking (closedAt, closedBy)

### Workflow Engine

* [x] CREATED
* [x] IN_PROGRESS
* [x] ESCALATED
* [x] RESOLVED
* [x] CLOSED
* [x] REOPENED
* [x] SLA_BREACHED
* [x] SLA_BREACHED → RESOLVED Recovery Flow
* [x] Citizen Resolution Verification Workflow
* [x] RESOLVED → REOPENED
* [x] REOPENED → IN_PROGRESS
* [x] RESOLVED → CLOSED

### Escalation System

* [x] Scheduled Escalation Service
* [x] SLA Monitoring
* [x] Multi-Level Escalation
* [x] Automatic Reassignment
* [x] SLA Breach Detection

### Citizen APIs

* [x] Create Issue
* [x] Get My Issues
* [x] Get Issue History
* [x] Review Issue API (Approve / Reject Resolution)

### Official APIs

* [x] Get Assigned Issues
* [x] Update Issue Status

### Official Management

* [x] Create Official API
* [x] View Officials API
* [x] Update Official API
* [x] Official Response DTO

### Admin APIs

* [x] Get All Issues
* [x] Create Department API
* [x] Get All Departments API
* [x] Update Department API
* [x] Delete Department API

### Dashboards

* [x] Citizen Dashboard
* [x] Official Dashboard
* [x] Admin Dashboard

### Analytics

* [x] Issue Statistics
* [x] Department Statistics
* [x] Escalation Statistics

### JWT Refactor

* [x] createIssue() uses JWT user
* [x] updateIssueStatus() uses JWT user
* [x] getMyIssues() uses JWT user
* [x] getAssignedIssues() uses JWT user
* [x] getAllIssues() uses JWT user

### Enum Migration

* [x] IssueStatus → EnumType.STRING
* [x] Priority → EnumType.STRING
* [x] Level → EnumType.STRING
* [x] Role → EnumType.STRING

---

## 🧹 Technical Debt / Cleanup Later

### Department System

* [ ] Replace manually inserted departments with API-created departments
* [ ] Standardize all Department IDs to UUIDs
* [ ] Recreate officials using UUID-based department IDs

### Official Data Cleanup

* [ ] Remove legacy officials with null email/phone fields
* [ ] Recreate officials through Create Official API

### Validation & Error Handling

* [ ] Add Bean Validation (@NotBlank, @Email, @Size)
* [ ] Add @Valid to controller endpoints
* [ ] Add Global Exception Handler (@RestControllerAdvice)
* [ ] Create custom exceptions
* [ ] Standardize API error responses

### Workflow Improvements

* [ ] Introduce configurable SLA durations based on Priority
* [ ] Replace hardcoded SLA_MINUTES with configuration
* [ ] Add resolution remarks/comments
* [ ] Add official resolution notes

### Architecture Improvements

* [ ] Replace keyword-based department detection with department selection
* [ ] Introduce standard API response wrapper
* [ ] Add isActive flag to User entity
* [ ] Implement Official Deactivation API
* [ ] Exclude inactive officials from assignment logic

---

## 🚀 Upcoming Features

### Analytics

* [ ] SLA Statistics

### Administration

* [ ] Soft Delete / Deactivate Official

### Search & Pagination

* [ ] Search Issues API
* [ ] Pagination for Issues

### File Uploads

* [ ] Upload Issue Evidence
* [ ] Attach Images to Issues

### Frontend

* [ ] React Setup
* [ ] Login Page
* [ ] Citizen Dashboard
* [ ] Issue Creation Page
* [ ] My Issues Page
* [ ] Official Dashboard
* [ ] Admin Dashboard
* [ ] Manage Officials
* [ ] Manage Departments

### Testing

* [x] Citizen Dashboard Testing
* [x] Official Dashboard Testing
* [x] Department API Testing
* [x] Citizen Review Workflow Testing
* [ ] End-to-End Dashboard Testing

### Deployment

* [ ] Backend Deployment
* [ ] Database Deployment
* [ ] Frontend Deployment
* [ ] Environment Variable Configuration
* [ ] Dockerize Backend
* [ ] Production MySQL Setup
* [ ] CI/CD Pipeline

---

## 📊 Current Project Status

### Backend Progress

Authentication & Security: **100%**

User Management: **100%**

Issue Management: **100%**

Escalation System: **100%**

Department Management: **100%**

Official Management: **95%**

Dashboards: **100%**

Analytics: **90%**

Testing: **90%**

Frontend: **15%**

Deployment: **0%**

### Major Remaining Backend Features

* Bean Validation
* Global Exception Handling
* Custom Exceptions
* Search API
* Pagination
* File Uploads
* Official Deactivation
