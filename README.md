# Campus Lost & Found

A full-stack campus Lost & Found application built with:

- Frontend: React + Vite + React Router
- Backend: Spring Boot 3 + Java 21
- Database: MySQL 8
- Authentication: JWT
- Styling: plain CSS (easy to customize)
- API: REST

## Features in this MVP

- Student registration and login
- JWT authentication
- Report LOST or FOUND items
- Browse/search/filter reports
- Item detail page
- Claim a FOUND item
- Owner/finder can approve or reject claims
- Mark an item as RETURNED
- Student dashboard
- Admin dashboard
- Admin can approve/reject/delete item reports
- MySQL database
- Docker Compose for MySQL

## Requirements

Recommended:

- Java 21
- Maven 3.9+
- Node.js 20+
- npm 10+
- Docker Desktop (optional, recommended for MySQL)

## 1. Start MySQL

From the project root:

```bash
docker compose up -d
```

Database:
- host: localhost
- port: 3306
- database: campus_lost_found
- username: campus
- password: campus123

If you already have MySQL installed, create the same database/user or edit:
`backend/src/main/resources/application.properties`

## 2. Start the backend

```bash
cd backend
mvn spring-boot:run
```

Backend:
http://localhost:8080

Health:
http://localhost:8080/api/health

## 3. Start the frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Frontend:
http://localhost:5173

## Demo accounts

The application creates an admin account automatically on startup:

Email:
admin@campus.local

Password:
Admin@123

You can create normal student accounts from the Register page.

## API overview

### Auth

POST `/api/auth/register`
POST `/api/auth/login`

### Items

GET `/api/items`
GET `/api/items/{id}`
POST `/api/items`
PUT `/api/items/{id}`
DELETE `/api/items/{id}`

### Claims

POST `/api/items/{id}/claims`
GET `/api/claims/my`
PUT `/api/claims/{id}/approve`
PUT `/api/claims/{id}/reject`

### Admin

GET `/api/admin/items/pending`
PUT `/api/admin/items/{id}/approve`
PUT `/api/admin/items/{id}/reject`
DELETE `/api/admin/items/{id}`

## Notes

This is the first complete MVP. Image upload, chat, notifications, smart matching, email, campus maps, and production deployment can be added in the next phases.

For production, change the JWT secret and database credentials.
