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

### Workflow Engine

* [x] CREATED
* [x] IN_PROGRESS
* [x] ESCALATED
* [x] RESOLVED
* [x] CLOSED
* [x] SLA_BREACHED

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

### Official APIs

* [x] Get Assigned Issues
* [x] Update Issue Status

### Official Management

* [x] Create Official API
* [x] View Officials API
* [x] Official Response DTO

### Admin APIs

* [x] Get All Issues
* [x] Create Department API
* [x] Get All Departments API
* [x] Update Department API
* [x] Delete Department API

### JWT Refactor

* [x] createIssue() uses JWT user
* [x] updateIssueStatus() uses JWT user
* [x] getMyIssues() uses JWT user
* [x] getAssignedIssues() uses JWT user
* [x] getAllIssues() uses JWT user

---

## 🔧 Pending Fixes

* [x] Populate closedAt when issue closes
* [x] Populate closedBy when issue closes
* [x] Change CREATED history updatedBy from official → citizen
* [x] Secure getIssueById()
* [x] Null safety in getIssueWithEscalatedStatus()

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

### Architecture Improvements

* [ ] Replace keyword-based department detection with department selection
* [ ] Introduce standard API response wrapper
* [ ] Add isActive flag to User entity
* [ ] Implement Official Deactivation API
* [ ] Exclude inactive officials from assignment logic

---

## 🚀 Upcoming Features

### Department Management

* [x] Create Departments API
* [x] Get All Departments API
* [x] Update Department API
* [x] Delete Department API

### Dashboards

* [x] Citizen Dashboard
* [x] Official Dashboard
* [X] Admin Dashboard

### Analytics

* [x] Issue Statistics
* [x] Department Statistics
* [x] Escalation Statistics
* [ ] SLA Statistics

### Administration

* [x] Create Official API
* [x] View Officials API
* [x] Update Official API
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
* [ ] End-to-End Dashboard Testing

### Deployment

* [ ] Backend Deployment
* [ ] Database Deployment
* [ ] Frontend Deployment
* [ ] Environment Variable Configuration
