SkillProof -- Skill Verification & Career Readiness App

Don't just claim a skill. Prove it.

SkillProof is an Android application developed for the Mobile
Application Development course. It helps students track technical
skills, complete skill-specific assessments, submit
projects/certificates/GitHub evidence, verify evidence, calculate an
explainable Proof Score, analyze skill gaps, and understand career
readiness.

🎯 Objectives

Track technical skills and proficiency.

Select one Current Skill for analysis.

Conduct skill-specific assessments.

Store assessment results and history.

Add and manage evidence.

Support evidence verification.

Calculate a measurable Proof Score.

Analyze skill gaps against a target.

Provide career-readiness recommendations.

✨ Main Features

Authentication

Splash screen with login-state check.

Registration and login.

Local session management using SharedPreferences.

Dashboard

Central navigation for: - Skills - Evidence - Assessment - Assessment
History - Proof Score - Skill Gap Analyzer - Career - Profile

Skill Management

Users can add skills, set proficiency/progress, view proof status, and
select a Current Skill.

The selected skill is stored as:

SkillProofPrefs → current_skill

This keeps Dashboard, Assessment, Proof Score, Skill Gap, and Career
aligned.

Skill-Specific Assessment

Supported assessment banks: - Kotlin - Java - Python - SQL - Android

Each assessment contains 5 multiple-choice questions, validates answers,
calculates the percentage, stores the result in SQLite, and displays an
Assessment Result screen.

Assessment History

Previous attempts are stored locally and display: - Skill - Score -
Percentage - Performance

Evidence

Users can submit: - Projects - Certificates - GitHub repositories -
Other supporting evidence

Evidence includes title, skill, type, link, and optional attachment.

Verification

Evidence can be verified through the application's verification
workflow, including GitHub verification where applicable.

Proof Score

The Proof Score measures how strongly a selected skill is supported.

Proof Score =
    Skill Level       × 30%
  + Assessment        × 30%
  + Evidence          × 25%
  + Verification      × 15%

All components are normalized from 0--100.

Evidence scoring:

Evidence   Score

       0       0
       1      45
       2      65
       3      80
       4      90
      5+     100

Skill Gap Analyzer

Shows: - Current score - Target score - Points needed - Skill level -
Assessment strength - Evidence strength - Verification strength -
Personalized action plan

Career Readiness

Provides career-oriented feedback using the selected Current Skill and
its proof data.

Profile

Displays stored user information.

🏗️ Application Flow

Splash
  ↓
Login / Registration
  ↓
Dashboard
  ↓
Skills → Select Current Skill
  ↓
Assessment + Evidence + Verification
  ↓
Proof Score
  ↓
Skill Gap Analyzer + Career Readiness

🧱 Architecture

                    ┌─────────────────┐
                    │   SkillProof    │
                    │   Android App   │
                    └────────┬────────┘
                             │
          ┌──────────────────┼──────────────────┐
          ↓                  ↓                  ↓
      Skills            Assessment          Evidence
          │                  │                  │
          └──────────────────┼──────────────────┘
                             ↓
                    ┌─────────────────┐
                    │  SQLite Database│
                    └────────┬────────┘
                             ↓
                    ┌─────────────────┐
                    │  Proof Score    │
                    └────────┬────────┘
                             ↓
              ┌──────────────┼──────────────┐
              ↓              ↓              ↓
        Skill Gap         Career        Dashboard

🛠️ Technology Stack

Technology            Purpose

Kotlin                Android development
Android Studio        Development IDE
XML                   UI design
ConstraintLayout      Screen layouts
Material Components   UI components
RecyclerView          List displays
SQLite                Local database
SharedPreferences     Session and Current Skill
Activity Result API   File selection
GitHub                Version control

💾 Data Storage

SharedPreferences

Preference file:

SkillProofPrefs

Important keys:

user_name
user_email
user_password
is_logged_in
current_skill

SQLite

Database:

SkillProof.db

Main data: - Skills - Evidence - Assessments - Verification status

📂 Important Project Structure

app/src/main/java/com/example/skill_proof_mad_assignment_1/

├── AddEvidenceActivity.kt
├── AddSkillActivity.kt
├── Assessment.kt
├── AssessmentActivity.kt
├── AssessmentHistoryActivity.kt
├── AssessmentHistoryAdapter.kt
├── AssessmentResultActivity.kt
├── Badge.kt
├── BadgeAdapter.kt
├── BadgeManager.kt
├── BadgesActivity.kt
├── BaseActivity.kt
├── CareerActivity.kt
├── DashboardActivity.kt
├── DatabaseHelper.kt
├── Evidence.kt
├── EvidenceActivity.kt
├── EvidenceAdapter.kt
├── GitHubVerificationActivity.kt
├── LoginActivity.kt
├── ProfileActivity.kt
├── ProofScoreActivity.kt
├── RegistrationActivity.kt
├── Skill.kt
├── SkillAdapter.kt
├── SkillGapActivity.kt
├── SkillsActivity.kt
└── SplashActivity.kt

🚀 How to Run

Open the project in Android Studio.

Allow Gradle synchronization.

Connect an Android device or start an emulator.

Run the application.

Register an account.

Login.

Add a skill.

Select it as the Current Skill.

Complete its assessment.

Add evidence.

Verify evidence.

View Proof Score, Skill Gap, and Career modules.

🧪 Recommended Test Flow

Register
   ↓
Login
   ↓
Add Python
   ↓
Set Python as Current Skill
   ↓
Complete Python Assessment
   ↓
Check Assessment Result
   ↓
Check Assessment History
   ↓
Add Python Evidence
   ↓
Verify Evidence
   ↓
Open Proof Score
   ↓
Open Skill Gap Analyzer
   ↓
Open Career

Then change the Current Skill to Java and verify that the relevant
modules update to Java.

📊 Example Proof Score

For a selected skill:

Skill Level   = 80
Assessment    = 80
Evidence     = 80
Verification = 100

Calculation:

(80 × 0.30)
+ (80 × 0.30)
+ (80 × 0.25)
+ (100 × 0.15)

= 83 / 100

Therefore:

83 → Proven Skill

🔐 Security Note

This is an academic project. Authentication data is currently stored
locally for demonstration purposes. A production version should use
secure authentication, encrypted storage, hashed passwords, and a
backend service.

🔮 Future Enhancements

AI-based skill recommendations

NLP resume analysis

LLM-powered career guidance

Automated project evaluation

Certificate authenticity verification

Cloud synchronization

Firebase Authentication

REST API backend

Job-role matching

Resume generation

Personalized learning roadmaps

Advanced analytics

🎓 Academic Information

Project: SkillProof -- Skill Verification & Career Readiness App
Course: 2CEIT5PE18 -- Mobile Application Development
Program: B.Tech CE/IT
Semester: V
Academic Year: 2026--27
University: Ganpat University
Department: Computer Engineering / Information Technology

👥 Contributors
