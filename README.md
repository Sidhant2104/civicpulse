# CivicPulse

## 🚀 Overview

CivicPulse is a workflow-driven backend system for citizen issue tracking and escalation.
It is designed to ensure accountability by managing issue lifecycles, enforcing SLA rules, and enabling automatic escalation through hierarchical roles.

---

## 🎯 Core Idea

Citizens can raise issues which are:

* Automatically assigned to officials
* Tracked through different lifecycle states
* Escalated if not resolved within defined SLA

---

## ⚙️ Tech Stack

* Java
* Spring Boot
* MySQL (or Firestore - configurable)
* REST APIs

---

## 🧠 Key Concepts

* Event-based status tracking (no direct status field in Issue)
* Role-Based Access Control (CITIZEN, OFFICIAL, ADMIN)
* Hierarchical escalation system
* SLA-driven automation

---

## 🏗️ Architecture

* Controller Layer → Handles API requests
* Service Layer → Business logic
* Model Layer → Data structures
* Repository Layer → Data access
* Utility Layer → Authentication helpers
* Exception Layer → Global error handling

---

## 📌 Project Status

🚧 In Development — Core backend system being built

---

## 👨‍💻 Author

Sidhant Singh
