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

### Admin APIs

* [x] Get All Issues
* [x] Create Department API

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

### Validation & Error Handling

* [ ] Add Bean Validation (@NotBlank, @Email, @Size)
* [ ] Add @Valid to controller endpoints
* [ ] Add Global Exception Handler (@RestControllerAdvice)
* [ ] Create custom exceptions
* [ ] Standardize API error responses

### Architecture Improvements

* [ ] Replace keyword-based department detection with department selection
* [ ] Introduce standard API response wrapper
* [ ] Implement soft delete for officials

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
* [ ] Admin Dashboard

### Analytics

* [ ] Issue Statistics
* [ ] Department Statistics
* [ ] Escalation Statistics
* [ ] SLA Statistics

### Administration

* [ ] Create Official API
* [ ] View Officials API
* [ ] Update Official API
* [ ] Delete Official API

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
* [ ] Official Dashboard Testing
* [ ] Department API Testing
* [ ] End-to-End Dashboard Testing

### Deployment

* [ ] Backend Deployment
* [ ] Database Deployment
* [ ] Frontend Deployment
* [ ] Environment Variable Configuration
