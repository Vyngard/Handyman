# Handyman Service Frontend

Vue.js frontend application for the Handyman Service Platform.

## Table of Contents
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Project Setup](#project-setup)
- [Project Structure](#project-structure)
- [Component Documentation](#component-documentation)
- [State Management](#state-management)
- [API Integration](#api-integration)
- [Testing](#testing)

## Technologies Used
- Vue.js 3
- Vue Router for navigation
- Vuex for state management
- Axios for HTTP requests
- Bootstrap for styling
- Jest for unit testing
- Vue Test Utils for component testing

## Features
- Responsive design
- User authentication and authorization
- Real-time form validation
- Dynamic content loading
- Interactive job management
- Worker profile showcase
- Rating and review system
- Real-time messaging interface
- Location-based search
- Advanced filtering options

## Prerequisites
- Node.js 16.x or later
- npm 8.x or later
- Modern web browser
- Backend API running (see [Backend Documentation](../_Backend/README.md))

## Project Setup

1. Install dependencies:
```bash
npm install
```

2. Configure environment variables:
```bash
cp .env.example .env
# Edit .env with your configurations
```

3. Run development server:
```bash
npm run serve
```

4. Build for production:
```bash
npm run build
```

5. Run tests:
```bash
npm run test:unit
```

## Project Structure
```
_Frontend/
├── public/             # Static assets
├── src/
│   ├── assets/        # Images, fonts, etc.
│   ├── components/    # Reusable Vue components
│   │   ├── DisplayJobs.vue
│   │   ├── DisplayMessages.vue
│   │   ├── DisplayWorkers.vue
│   │   ├── HeaderComponent.vue
│   │   ├── HomePage.vue
│   │   ├── NavBar.vue
│   │   ├── SelectedWorker.vue
│   │   ├── SignUp.vue
│   │   ├── StarShow.vue
│   │   ├── UserDetails.vue
│   │   ├── UserLogin.vue
│   │   └── WorkerDetails.vue
│   ├── router/        # Vue Router configuration
│   │   └── index.js
│   ├── services/      # API services
│   │   ├── FetchDataService.js
│   │   └── LoginService.js
│   ├── store/         # Vuex store modules
│   ├── App.vue        # Root component
│   └── main.js        # Application entry point
├── tests/             # Test files
├── .env.example       # Environment variables example
├── babel.config.js    # Babel configuration
├── package.json       # Project dependencies
└── vue.config.js      # Vue CLI configuration
```

## Component Documentation

### Core Components

#### NavBar.vue
Main navigation component with dynamic menu based on user role.

#### HomePage.vue
Landing page component with featured workers and recent jobs.

#### DisplayJobs.vue
Grid display of available jobs with filtering and sorting options.

#### DisplayWorkers.vue
Worker listing component with search and filter capabilities.

#### UserLogin.vue
Authentication component handling user and worker login.

### Feature Components

#### SelectedWorker.vue
Detailed worker profile view with ratings and reviews.

#### StarShow.vue
Reusable rating component for displaying and submitting ratings.

#### UserDetails.vue
User profile management component.

#### WorkerDetails.vue
Worker profile management with skill selection.

## State Management

### Vuex Store Modules

#### Auth Module
```javascript
// Authentication state management
state: {
  user: null,
  token: null,
  role: null
}
```

#### Jobs Module
```javascript
// Jobs state management
state: {
  jobs: [],
  selectedJob: null,
  filters: {}
}
```

#### Workers Module
```javascript
// Workers state management
state: {
  workers: [],
  selectedWorker: null,
  filters: {}
}
```

## API Integration

### Services

#### LoginService.js
Handles authentication and user management API calls.

#### FetchDataService.js
Manages data fetching for jobs, workers, and other entities.

### Axios Configuration
```javascript
// http-common.js
import axios from 'axios';

export default axios.create({
  baseURL: process.env.VUE_APP_API_URL,
  headers: {
    'Content-type': 'application/json'
  }
});
```

## Testing

### Unit Tests
```bash
npm run test:unit
```

### Component Tests
```bash
npm run test:component
```

### E2E Tests
```bash
npm run test:e2e
```

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
