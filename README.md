# 🧠 MindCare – Backend

Backend for a mental health web application that supports emotional wellbeing and a fresh, balanced mind.

**Tech:** Java • Spring Boot • REST API • MySQL  

**Live:** https://mindcare-health.vercel.app/  
> ⚠️ Backend hosted on Render (free tier). First load may take up to **1–2 minutes**.

**Frontend Repository:** [mindcare_frontend](https://github.com/prachu25/mindcare_frontend)

---

## Overview

MindCare Backend is a RESTful API built with Spring Boot that supports a mental health and wellbeing web application.  
It allows users to manage profiles, track mood and sleep, submit mental health assessments, and use a one-to-one support chat through structured APIs backed by MySQL.

### Key Features

- **User Authentication & Profiles** – Secure user registration, login, and profile management  
- **Mood Tracking** – Daily mood logging with mood score, notes, and history  
- **Mental Health Assessments** – Submit and view mental health assessment results  
- **Sleep Tracking** – Track and update daily sleep hours  
- **Support Chat** – One-to-one support chat with message history  
- **User Insights** – View user wellbeing status and summary data  
- **RESTful APIs** – Clean and structured REST APIs consumed by the frontend  
- **Spring Boot Architecture** – Layered design with controllers, services, and repositories  

---

## Architecture
```
User
↓
Frontend (MindCare Web Application)
↓
Spring Boot Backend (REST APIs)
├── Controller Layer
├── Service Layer
└── Repository Layer
↓
MySQL Database
↑
Response back to Frontend
```

### Request Flow 

1. Client sends request to Spring Boot REST API
2. Controller receives the request and validates input data
3. Service layer processes business logic
    - user profile handling
    - mood tracking
    - assessments
    - sleep tracking
    - support chat
4. Repository layer interacts with the MySQL database
5. Data is stored or fetched from the database
6. Processed response is returned to the controller
7. Controller sends response back to the frontend

---

## Tech Stack

### Backend
- Java  
- Spring Boot  
- Spring Web (REST APIs)  
- Spring Data JPA  
- Hibernate  

### Database
- MySQL  

### Frontend
- HTML  
- CSS  
- JavaScript  

### Tools & Testing
- Postman – API testing  
- Maven – Dependency management  
- Git & GitHub – Version control

---

## Project Structure

```
mindcare_backend/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/example/mental_health_backend/
│ │ │ ├── config/
│ │ │ │ ├── CorsConfig.java                  # CORS setup
│ │ │ │ └── SecurityConfig.java              # Security config
│ │ │ ├── controller/
│ │ │ │ ├── AdminController.java             # Admin APIs
│ │ │ │ ├── AuthController.java              # Auth APIs
│ │ │ │ ├── ChatController.java              # Chat APIs
│ │ │ │ └── UserProfileController.java       # Profile APIs
│ │ │ ├── dto/
│ │ │ │ ├── LoginRequest.java                # Login DTO
│ │ │ │ ├── RegisterRequest.java             # Register DTO
│ │ │ │ └── UserProfileRequest.java          # Profile DTO
│ │ │ ├── entity/
│ │ │ │ ├── ChatMessage.java                 # Chat entity
│ │ │ │ ├── MentalAssessment.java            # Assessment entity
│ │ │ │ ├── MoodLog.java                     # Mood entity
│ │ │ │ ├── User.java                        # User entity
│ │ │ │ └── UserProfile.java                 # Profile entity
│ │ │ ├── repository/
│ │ │ │ ├── ChatMessageRepository.java       # Chat DB
│ │ │ │ ├── MentalAssessmentRepository.java  # Assessment DB
│ │ │ │ ├── MoodLogRepository.java           # Mood DB
│ │ │ │ ├── UserProfileRepository.java       # Profile DB
│ │ │ │ └── UserRepository.java              # User DB
│ │ │ ├── service/
│ │ │ │ ├── AdminService.java                # Admin logic
│ │ │ │ ├── AuthService.java                 # Auth logic
│ │ │ │ ├── ChatMessageService.java          # Chat logic
│ │ │ │ ├── MentalAssessmentService.java     # Assessment logic
│ │ │ │ ├── MoodLogService.java              # Mood logic
│ │ │ │ ├── UserProfileService.java          # Profile logic
│ │ │ │ └── UserService.java                 # User logic
│ │ │ └── MentalHealthBackendApplication.java   # Main class
│ │ └── resources/
│ │ ├── templates/ 
│ │ ├── application.properties             # Application configuration
│ │ └── application-prod.properties        # Production configuration
│ └── test/ 
├── .mvn/       # Maven wrapper
├── target/ 
├── Dockerfile  # Docker setup
├── pom.xml     # Dependencies
└── README.md   # Documentation
```

---

## API Endpoints

### Authentication
- **POST** `/api/auth/register` – User registration  
- **POST** `/api/auth/login` – User login  

### User Profile
- **POST** `/api/user/profile` – Create or update profile  
- **GET** `/api/user/profile/{userId}` – Get user profile  
- **GET** `/api/user/profile/exists/{userId}` – Check profile existence  

### Mood
- **POST** `/api/user/mood/{userId}` – Save today’s mood  
- **GET** `/api/user/mood/{userId}` – Get today’s mood  
- **GET** `/api/user/history/mood/{userId}` – View mood history  

### Mental Assessment
- **POST** `/api/user/assessment/{userId}` – Submit assessment  
- **GET** `/api/user/assessment/history/{userId}` – Assessment history  

### Sleep
- **PUT** `/api/user/{userId}/sleep` – Update sleep hours  

### User Stats
- **GET** `/api/user/{userId}/status` – User wellbeing statistics  

### Chat
- **POST** `/api/chat/{userId}` – Send message & get reply  
- **GET** `/api/chat/{userId}` – Chat history  

---

## Installation & Setup 

### 1. Clone the Repository
```bash
git clone <your-repository-url>
cd mindcare_backend
```
### 2. Database Setup
```bash
CREATE DATABASE mindcare_db;
```
### 3. Configure Application Properties
Edit backend config file:
```bash
src/main/resources/application.properties
```
Update values:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/mindcare_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
### 4. Start backend server:
```bash
.\mvnw.cmd spring-boot:run
```
Backend runs on:
```bash
http://localhost:8080
```
### 5. Test APIs (Optional)
Use Postman to test APIs:
```bash
POST http://localhost:8080/api/auth/login
GET  http://localhost:8080/api/user/mood/{userId}
```

---

## Future Enhancements

- Daily mood reminders for consistent tracking  
- Notifications for assessments, sleep, and wellbeing check-ins  
- Enhanced user insights with progress summaries  
- Improved support chat with better conversation management  

---

## License 
This project is licensed under the MIT License.

---






