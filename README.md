# TimeX - Task Management API

A Spring Boot REST API for task and time management with JWT authentication.

## Project Overview
TimeX is a RESTful API for managing tasks and projects with user authentication. It provides endpoints for user registration, project creation, and task management with status tracking and time estimates.

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/Tushar3330/Timex
   cd timex
   ```

2. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. The API will be available at: `http://localhost:8080/api/`

## Default Test Accounts

| Username | Password | Role  |
|----------|----------|-------|
| admin    | admin123 | ADMIN |
| user     | user123  | USER  |

## Workflow

1. Login to get a JWT token
2. Use the token in all subsequent requests
3. Create a project
4. Add tasks to the project
5. Update task status as work progresses

## API Endpoints with cURL Examples

### Authentication

#### Login and get JWT token
```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "user123"
  }'
```

#### Register a new user
```bash
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "password123",
    "fullName": "New User"
  }'
```

### Project Management

#### Create a project
```bash
curl -X POST "http://localhost:8080/api/projects" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Project",
    "description": "This is a new project created via API"
  }'
```

#### Get all user projects
```bash
curl -X GET "http://localhost:8080/api/projects" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Get project with tasks
```bash
curl -X GET "http://localhost:8080/api/projects/1/detailed" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Task Management

#### Create a task
```bash
curl -X POST "http://localhost:8080/api/tasks" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "New Task",
    "description": "This is a task created via API",
    "projectId": 1,
    "status": "TODO",
    "priority": 2,
    "estimatedHours": 8,
    "dueDate": "2025-12-31T23:59:59"
  }'
```

#### Get tasks by status
```bash
curl -X GET "http://localhost:8080/api/tasks/project/1/status/IN_PROGRESS" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Update task status
```bash
curl -X PATCH "http://localhost:8080/api/tasks/1/status?status=COMPLETED" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

Note: Replace `YOUR_JWT_TOKEN` with the token received from the login endpoint.
