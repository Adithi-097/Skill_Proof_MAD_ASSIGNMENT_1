SkillProof – Skill Verification & Career Readiness App

Don't just claim a skill. Prove it.

📌 About

SkillProof is an Android application designed to help students build, assess, verify, and prove their technical skills.

Instead of simply listing skills on a resume, SkillProof connects skills with assessments, projects, certificates, GitHub repositories, evidence, and verification to provide a measurable Proof Score.

🚀 Overview

The application follows a simple workflow:

Claim Skill
    ↓
Select Current Skill
    ↓
Take Assessment
    ↓
Add Evidence
    ↓
Verify Evidence
    ↓
Proof Score
    ↓
Skill Gap
    ↓
Career Readiness

The goal is to provide students with a structured view of what they know, what they have built, and what they need to improve.

✨ Features

🔐 User Authentication – Login and registration

🧠 Skill Management – Add and manage technical skills

⭐ Current Skill – Select the skill currently being focused on

📝 Skill Assessments – Skill-specific MCQ assessments

📊 Assessment Results – Score and performance analysis

📚 Assessment History – Track previous attempts

📎 Evidence Management – Add projects, certificates, and links

🔗 GitHub Verification – Connect GitHub projects with skills

🏆 Proof Score – Calculate an overall skill-proof score

📉 Skill Gap Analyzer – Identify areas that need improvement

💼 Career Readiness – View career-related insights

👤 Profile Management – Manage user information

🏆 Proof Score

SkillProof calculates a score based on four major factors:

Component

Weight

Skill Level

30%

Assessment

30%

Evidence

25%

Verification

15%

Proof Score =
(Skill Level × 30%)
+ (Assessment × 30%)
+ (Evidence × 25%)
+ (Verification × 15%)

The final score is calculated out of 100.

⭐ Current Skill

Users can select one skill as their Current Skill.

For example:

Current Skill → Python

The selected skill is automatically used throughout:

Assessment
    ↓
Proof Score
    ↓
Skill Gap
    ↓
Career

This keeps the application focused on the skill the student is currently developing.

📝 Assessment

Skill-specific assessments are currently available for:

Python

Java

Kotlin

SQL

Android

Each assessment contains multiple-choice questions and automatically calculates the user's score and percentage.

📎 Evidence & Verification

Students can submit evidence related to their skills, including:

Projects

Certificates

GitHub repositories

Portfolio links

File/image attachments

GitHub projects can also be associated with a particular skill for verification.

📉 Skill Gap Analyzer

The Skill Gap module analyzes the selected skill and identifies areas that require improvement.

It considers:

Skill Level
Assessment
Evidence
Verification
Proof Score

Based on these factors, the application provides an improvement action plan.

📸 Screenshots

The following screenshots showcase the main modules of the SkillProof application.

Splash Screen

<img width="333" height="697" alt="image" src="https://github.com/user-attachments/assets/5cbc8e4c-f47d-480d-afb1-3d86cf805adf" />

Login



Registration



Dashboard

<img width="335" height="692" alt="image" src="https://github.com/user-attachments/assets/a2037756-a4c2-4676-b02d-82fbe4f2876e" />

Skills

<img width="335" height="690" alt="image" src="https://github.com/user-attachments/assets/2f44538f-bd32-4d90-b862-7650a41bc8fc" />

Evidence

<img width="336" height="702" alt="image" src="https://github.com/user-attachments/assets/c052f075-2705-4519-8d59-b6fd5a1757e8" />

Assessment

<img width="337" height="696" alt="image" src="https://github.com/user-attachments/assets/f44b2d65-8b39-4428-a16f-91268649d9b7" />

Assessment History

<img width="337" height="696" alt="image" src="https://github.com/user-attachments/assets/390ce1e9-40eb-4fb6-8821-ab70bdf818b7" />

GitHub Verification

<img width="337" height="697" alt="image" src="https://github.com/user-attachments/assets/b83ccc3a-ea67-4271-b0af-f3eecab59afc" />

Proof Score

<img width="332" height="687" alt="image" src="https://github.com/user-attachments/assets/1fc95069-6a78-4a84-b408-6178d1f05bc1" />

Skill Gap

<img width="341" height="701" alt="image" src="https://github.com/user-attachments/assets/93537fbe-f7f0-4355-bc95-4d778ef79c96" />

Career

<img width="335" height="697" alt="image" src="https://github.com/user-attachments/assets/125b5040-f341-49af-9f13-fa96bac0dba1" />

Profile

<img width="332" height="702" alt="image" src="https://github.com/user-attachments/assets/d8394fef-4f41-4114-be2e-833ca198469a" />


🏗️ Application Architecture

                 SkillProof Android App
                          |
        +-----------------+-----------------+
        |                 |                 |
        ↓                 ↓                 ↓
       UI              Application        Data
    Activities          Logic            Layer
        |                 |                 |
        ↓                 ↓                 ↓
      XML            Calculations        SQLite
   Material UI       Proof Score      SharedPrefs
   RecyclerView      Skill Gap
        |                 |
        +-----------------+
                |
                ↓
        Career Readiness

📂 Project Structure

SkillProof/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com.example.skill_proof_mad_assignment_1/
│           │       ├── Activities
│           │       ├── Adapters
│           │       ├── Models
│           │       └── DatabaseHelper.kt
│           │
│           └── res/
│               ├── layout/
│               ├── drawable/
│               ├── anim/
│               └── values/
│
├── screenshots/
│   ├── splash.png
│   ├── login.png
│   ├── registration.png
│   ├── dashboard.png
│   ├── skills.png
│   ├── add_skill.png
│   ├── evidence.png
│   ├── assessment.png
│   ├── assessment_result.png
│   ├── assessment_history.png
│   ├── github_verification.png
│   ├── proof_score.png
│   ├── skill_gap.png
│   ├── career.png
│   └── profile.png
│
├── build.gradle.kts
├── settings.gradle.kts
└── README.md

🛠️ Tech Stack

Android

Kotlin

Android Studio

XML

Material Components

ConstraintLayout

RecyclerView

Data

SQLite

SQLiteOpenHelper

SharedPreferences

Development

Git

GitHub

💾 Data Storage

SQLite

Used for storing:

Skills

Evidence

Assessments

Verification information

SharedPreferences

Used for:

Login status

User information

Current Skill

🔄 Application Flow

Splash
  ↓
Login / Register
  ↓
Dashboard
  ↓
Skills
  ↓
Select Current Skill
  ↓
Assessment
  ↓
Assessment Result
  ↓
Assessment History
  ↓
Evidence
  ↓
Verification
  ↓
Proof Score
  ↓
Skill Gap
  ↓
Career

🚀 Getting Started

Requirements

Android Studio

Android SDK

Android Emulator or Android Device

Git

Installation

Clone the repository:

git clone <YOUR_REPOSITORY_URL>

Open the project in Android Studio, allow Gradle to sync, connect an Android device or start an emulator, and run the application.

📱 Main Modules

Module

Description

Splash

Application startup

Login/Register

Authentication

Dashboard

Main navigation

Skills

Skill management

Assessment

Skill evaluation

Assessment History

Previous assessment attempts

Evidence

Proof submission

GitHub Verification

Project verification

Proof Score

Skill proof calculation

Skill Gap

Improvement analysis

Career

Career readiness

Profile

User information

🔮 Future Enhancements

Future versions of SkillProof can include:

🤖 AI-based skill analysis

🧠 AI-generated assessments

📄 NLP-based resume analysis

🔍 Advanced GitHub repository analysis

☁️ Cloud database and synchronization

💼 Job-role matching

📑 Automated resume generation

🗺️ Personalized learning roadmaps

💬 LLM-based career assistant

🎓 Academic Information

Project: SkillProof – Skill Verification & Career Readiness App
Course: Mobile Application Development
Course Code: 2CEIT5PE18
Semester: V
Academic Year: 2026–27
University: Ganpat University
Faculty: Faculty of Engineering & Technology
