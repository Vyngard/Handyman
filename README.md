# Handyman Service Platform

A full-stack web application that connects users with skilled workers for home maintenance and improvement tasks.

## Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Documentation](#documentation)

## Overview
The Handyman Service Platform is a comprehensive solution that facilitates connections between homeowners and skilled workers. Users can post jobs, search for workers based on skills and location, while workers can showcase their expertise, accept jobs, and build their reputation through ratings.

## Architecture
The application follows a microservices architecture with:
- Frontend: Vue.js single-page application
- Backend: Spring Boot RESTful API
- Database: H2 (development) / PostgreSQL (production)
- Authentication: JWT-based security

## Features
### For Users
- User registration and authentication
- Post jobs with detailed descriptions
- Search workers by skills, location, and ratings
- Real-time messaging with workers
- Rate and review completed jobs
- Track job history and expenses

### For Workers
- Professional profile management
- Skill and expertise showcase
- Job acceptance and management
- Schedule management
- Payment tracking
- Rating and review system

## Technologies

### Frontend (_Frontend)
- Vue.js 3
- Vue Router
- Vuex for state management
- Axios for HTTP requests
- Bootstrap for styling
- Jest for testing

### Backend (_Backend)
- Java 17
- Spring Boot 3.x
- Spring Security with JWT
- Spring Data JPA
- H2 Database (dev)
- Maven
- JUnit & Mockito

## Getting Started

### Prerequisites
- Node.js 16.x or later
- Java Development Kit (JDK) 17 or later
- Maven 3.8.x or later
- Git

### Installation

1. Clone the repository:
```bash
git clone https://github.com/your-username/handyman.git
cd handyman
```

2. Set up the backend:
```bash
cd _Backend
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Edit application.properties with your configurations
mvn clean install
mvn spring-boot:run
```

3. Set up the frontend:
```bash
cd _Frontend
npm install
cp .env.example .env
# Edit .env with your configurations
npm run serve
```

4. Access the application:
- Frontend: http://localhost:8081
- Backend API: http://localhost:8080
- API Documentation: http://localhost:8080/swagger-ui.html

## Project Structure

```
handyman/
├── _Frontend/           # Vue.js frontend application
│   ├── src/
│   │   ├── components/  # Vue components
│   │   ├── views/       # Page components
│   │   ├── router/     # Vue Router configuration
│   │   ├── store/      # Vuex store modules
│   │   └── services/   # API services
│   └── tests/          # Frontend tests
├── _Backend/           # Spring Boot backend application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/   # Java source files
│   │   │   └── resources/ # Application properties
│   │   └── test/       # Backend tests
│   └── pom.xml         # Maven configuration
└── README.md           # Project documentation
```

## Documentation

- [Frontend Documentation](_Frontend/README.md)
- [Backend Documentation](_Backend/README.md)
- [API Documentation](http://localhost:8080/swagger-ui.html) (when running)
- [Contributing Guidelines](CONTRIBUTING.md)

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
