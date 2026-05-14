# Student Learning System - Spring Boot Rebuild

This project recreates the previous Student Learning System using **Java Spring Boot** while reusing the existing **React + Vite** frontend. It keeps the same major frontend workflows while improving the backend architecture, security, data integrity, documentation, migrations, and developer experience.

## What Was Added Beyond the Original Brief

- Centralized CORS configuration using `FRONTEND_URL`.
- Centralized global exception handling with simple debug messages.
- Clear JWT unauthorized response: `Invalid or missing token`.
- Frontend `.env.example` using `VITE_API_URL`.
- Disabled buttons during save/search requests.
- Toast success/error feedback using `react-hot-toast`.
- Swagger tags and operation summaries on controllers.
- README setup instructions and key file map for presentation.

## Tech Stack

### Backend
- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- MySQL
- Flyway migrations
- Springdoc OpenAPI / Swagger
- Bean Validation
- Lombok

### Frontend
- React + Vite
- Axios
- React Hot Toast
- Custom CSS

## Requirements Checklist

| Requirement | Implemented In |
|---|---|
| Spring Boot API | `backend/src/main/java/com/example/studentlearning` |
| Layered architecture | Controllers, Services, Repositories, DTOs, Entities packages |
| Frontend compatibility | `frontend/src/api/api.js`, same endpoint structure |
| Flyway migrations | `backend/src/main/resources/db/migration/V1__initial_schema.sql` |
| No Hibernate auto-sync | `spring.jpa.hibernate.ddl-auto=validate` |
| Swagger/OpenAPI | `OpenApiConfig.java`, controller annotations |
| Data integrity | unique DB constraints, service checks, Bean Validation |
| No duplicate enrollments | `EnrollmentService.java` |
| No duplicate emails | `AuthService.java`, `StudentService.java` |
| Role-based authorization | `SecurityConfig.java`, `@PreAuthorize` |
| Roles enum | `auth/UserRole.java` |
| Entity auditing | `createdAt`, `updatedAt`, `@PrePersist`, `@PreUpdate` |
| Soft delete | `Student.deletedAt`, `StudentService.softDelete()` |
| Environment config | `application.properties`, `.env.example` |
| Loading states | frontend page components |
| Error handling | `GlobalExceptionHandler.java`, `api.js` |
| Relationship display | Students, Courses, Enrollments pages |
| Toast notifications | frontend page components with `toast.success()` / `toast.error()` |

## Project Structure

```text
student-learning-system-springboot/
├── backend/
│   ├── src/main/java/com/example/studentlearning/
│   │   ├── auth/
│   │   ├── common/config/
│   │   ├── common/exception/
│   │   ├── students/
│   │   ├── profiles/
│   │   ├── courses/
│   │   ├── assignments/
│   │   └── enrollments/
│   └── src/main/resources/db/migration/
└── frontend/
    └── src/
```

## Backend Setup

### 1. Create MySQL Database

```sql
CREATE DATABASE student_learning_db;
```

### 2. Configure Environment

Edit:

```text
backend/src/main/resources/application.properties
```

or set environment variables based on:

```text
backend/.env.example
```

Main values:

```properties
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=student_learning_db
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
JWT_SECRET=king12346king12346king12346king12346
JWT_EXPIRATION_MS=86400000
FRONTEND_URL=http://localhost:5173
PORT=3000
```

### 3. Run Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:3000
```

Swagger runs on:

```text
http://localhost:3000/swagger-ui/index.html
```

Flyway migrations run automatically when Spring Boot starts.

## Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on:

```text
http://localhost:5173
```

Optional frontend env file:

```text
frontend/.env
```

```env
VITE_API_URL=http://localhost:3000
```

## Roles

Spring Boot equivalent of TypeScript enums is a Java enum:

```java
public enum UserRole {
  ADMIN,
  STUDENT
}
```

Located at:

```text
backend/src/main/java/com/example/studentlearning/auth/UserRole.java
```

## Authorization Rules

- `ADMIN` can create, update, delete, and manage enrollments.
- `STUDENT` can view data.
- Protected routes require a JWT Bearer token.

Example controller rule:

```java
@PreAuthorize("hasRole('ADMIN')")
```

## Simplified Debug Error Handling

The backend uses a global exception handler. For debugging, it returns simple error messages and logs only the message in the backend terminal.

Located at:

```text
backend/src/main/java/com/example/studentlearning/common/exception/GlobalExceptionHandler.java
```

Example:

```java
System.err.println("DEBUG ERROR: " + ex.getMessage());
```

## Data Integrity Examples

### Duplicate Email Prevention

Located in:

```text
auth/AuthService.java
students/StudentService.java
```

### Duplicate Enrollment Prevention

Located in:

```text
enrollments/EnrollmentService.java
```

The service checks if a course already exists in the student's course set before saving.

## Soft Delete

Implemented on `Student`.

Files:

```text
students/Student.java
students/StudentService.java
```

The app sets `deletedAt` instead of permanently deleting the student row.

## Swagger Usage

1. Register or login using `/auth/register` or `/auth/login`.
2. Copy the `accessToken`.
3. Click **Authorize** in Swagger.
4. Paste:

```text
Bearer YOUR_ACCESS_TOKEN
```

5. Test protected endpoints.

## Toast Notifications

Frontend uses `react-hot-toast`.

Examples:

```js
toast.success('Student saved successfully');
toast.error(errorMessage(error));
```

## Presentation Summary

This project rebuilds the Student Learning System in Spring Boot using layered architecture. It improves the original app by adding Spring Security with JWT authentication, role-based authorization, Flyway migrations, Swagger documentation, Bean Validation, unique constraints, duplicate enrollment checks, entity timestamps, soft delete, frontend loading states, frontend toast feedback, and clearer relationship displays.

## Latest additions

- Students can now enroll for courses through the same enrollment endpoint used by admins.
- Controllers now return `ResponseEntity<T>` responses to make status codes explicit, for example `201 Created` for create/enroll actions.
- Common DTOs were added for shared request/response patterns:
  - `IdRequest` for path IDs
  - `SearchRequest` for search/list endpoints
  - `DeleteResponse` for delete responses
- Role words are centralized in `SecurityRoleNames` so `ADMIN` and `STUDENT` can be edited in one place.
- Admin-only routes still use Spring Security `hasRole(...)`, but the role value comes from the shared constants class instead of being hardcoded repeatedly.
