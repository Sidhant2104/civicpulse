# CivicPulse

## 🚀 Overview

CivicPulse is a workflow-driven backend application for managing and tracking civic issues. It enables citizens to report public grievances, automatically routes issues to the appropriate government department, and manages the complete resolution lifecycle through role-based workflows and SLA-driven escalation.

The project focuses on transparency, accountability, and efficient issue resolution by combining automated assignment, hierarchical escalation, and detailed status tracking.

---

## 🎯 Core Features

* Citizen issue registration
* Automatic department detection based on issue description
* Automatic official assignment
* Role-Based Access Control (Citizen, Official, Admin)
* JWT Authentication & Spring Security
* Complete issue lifecycle management
* Multi-level escalation workflow
* SLA monitoring using scheduled tasks
* Issue history tracking
* Resolution and closure tracking
* Dashboard statistics for different user roles
* Department and official management

---

## 🔄 Issue Workflow

```
CREATED
    ↓
IN_PROGRESS
    ↓
ESCALATED
    ↓
SLA_BREACHED
    ↓
RESOLVED
    ↓
CLOSED
```

Every status change is stored in the Issue Status History, providing a complete audit trail of the issue lifecycle.

---

## ⚙️ Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* MySQL
* Spring Data JPA (Hibernate)
* Maven
* REST APIs

---

## 🧠 Key Concepts

* Event-based issue status tracking
* Role-Based Access Control (RBAC)
* Hierarchical official escalation
* SLA-based automated escalation
* Department-wise issue routing
* Resolution and closure tracking
* Scheduler-driven background processing

---

## 🏗️ Project Architecture

* Controller Layer – REST API endpoints
* Service Layer – Business logic
* Repository Layer – Database operations
* Model Layer – Entity definitions
* Security Layer – JWT Authentication & Authorization
* Scheduler Layer – Automatic SLA monitoring and escalation

---

## 📌 Current Status

🚧 **Backend Under Active Development**

### Completed

* Authentication & Authorization
* Citizen, Official & Admin modules
* Department Management
* Official Management
* Issue Management
* Automated Escalation Engine
* Dashboard APIs
* Issue Resolution & Closure Tracking

### Planned

* File upload support
* Search & Pagination
* Frontend (React)
* Backend deployment
* Production-ready validation and exception handling

---

## 👨‍💻 Author

**Sidhant Singh**

