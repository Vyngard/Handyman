# Handyman Service Backend

A Spring Boot application that provides the backend services for the Handyman platform, connecting users with skilled workers for various home maintenance and improvement tasks.

## Table of Contents
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Database Schema](#database-schema)
- [Contributing](#contributing)

## Technologies Used
- Java 17
- Spring Boot 3.x
- Spring Security with JWT
- Spring Data JPA
- H2 Database (for development)
- Maven
- JUnit 5 & Mockito for testing

## Features
- User and Worker Authentication
- Job Management
- Skill-based Worker Matching
- Rating System
- Transaction History
- Real-time Messaging
- Location-based Search
- Advanced Worker Search

## Prerequisites
- Java Development Kit (JDK) 17 or later
- Maven 3.8.x or later
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

## Setup and Installation

1. Clone the repository:
```bash
git clone https://github.com/your-username/handyman.git
cd handyman/_Backend
```

2. Configure application properties:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Edit application.properties with your database and other configurations
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

The server will start at `http://localhost:8080`

## API Documentation

### Authentication Endpoints
- POST `/auth/register/user` - Register a new user
- POST `/auth/register/worker` - Register a new worker
- POST `/auth/login` - Login for users and workers
- POST `/auth/validate` - Validate JWT token

### User Endpoints
- GET `/users/{id}` - Get user details
- PUT `/users/{id}` - Update user profile
- GET `/users/{id}/jobs` - Get user's jobs
- POST `/users/{id}/credit` - Update user credit

### Worker Endpoints
- GET `/workers` - List all workers
- GET `/workers/{id}` - Get worker details
- PUT `/workers/{id}` - Update worker profile
- GET `/workers/skills/{skill}` - Find workers by skill
- GET `/workers/rating/{rating}` - Find workers by rating
- POST `/workers/{id}/rate` - Rate a worker

### Job Endpoints
- GET `/jobs` - List all jobs
- POST `/jobs` - Create a new job
- GET `/jobs/{id}` - Get job details
- PUT `/jobs/{id}` - Update job
- DELETE `/jobs/{id}` - Delete job
- GET `/jobs/skills` - Find jobs by skills
- GET `/jobs/location/{location}` - Find jobs by location
- POST `/jobs/{id}/complete` - Mark job as complete
- POST `/jobs/{id}/rate` - Rate completed job

## Security
The application uses JWT (JSON Web Tokens) for authentication. Protected endpoints require a valid JWT token in the Authorization header:
```
Authorization: Bearer <your-token>
```

Role-based access control is implemented with the following roles:
- ROLE_USER: Regular users who can post jobs
- ROLE_WORKER: Workers who can accept and complete jobs
- ROLE_ADMIN: Administrative access

## Database Schema

### User Table
- id (Long, PK)
- username (String, unique)
- email (String, unique)
- password (String)
- full_name (String)
- phone (String)
- location (String)
- credit (Double)

### Worker Table
- id (Long, PK)
- username (String, unique)
- email (String, unique)
- password (String)
- full_name (String)
- phone (String)
- location (String)
- description (String)
- working_hours (String)
- average_rating (Double)
- total_jobs (Integer)
- preferred_communication (Enum)

### Job Table
- id (Long, PK)
- title (String)
- description (String)
- budget (Double)
- client_id (Long, FK)
- worker_id (Long, FK)
- created_at (DateTime)
- completed_at (DateTime)
- is_completed (Boolean)
- rating (Double)

### Skill Table
- id (Long, PK)
- name (String, unique)

### Worker_Skills Table
- worker_id (Long, FK)
- skill_id (Long, FK)

### Job_Skills Table
- job_id (Long, FK)
- skill_id (Long, FK)

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
