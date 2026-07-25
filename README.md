# Expense Tracker Backend

An enterprise-grade, secure RESTful API built with **Spring Boot 4.0.5** and **Java 21**, designed to serve as the backend for the Expense Tracker full-stack application. It leverages **Spring Data JDBC** for clean, lightweight persistence and **Spring Security** with **JSON Web Tokens (JWT)** for robust authentication.

---

## Table of Contents
1. [Tech Stack & Architecture](#tech-stack--architecture)
2. [Database Schema & Data Model](#database-schema--data-model)
3. [Startup Data Ingestion](#startup-data-ingestion)
4. [API Endpoint Reference](#api-endpoint-reference)
5. [Frontend-Backend Coupling & Integration](#frontend-backend-coupling--integration)
6. [Getting Started](#getting-started)

---

## Tech Stack & Architecture

*   **Java Version**: 21 (leveraging modern features like Records for data transfer and entity modeling)
*   **Framework**: Spring Boot 4.0.5 (MVC architecture)
*   **Security**: Spring Security 6+ integrated with JSON Web Tokens (JJWT) and BCrypt password hashing
*   **Database & Persistence**: PostgreSQL (Production/Dev default) with H2 support, using **Spring Data JDBC** for relational mapping
*   **JSON Processing**: Jackson ObjectMapper customized with JavaTime support for standard date-time serialization

The project is structured around key domains representing clean boundaries:
*   `config`: Security configurations, custom filters, JWT utilities, and CORS mapping.
*   `user`: User profile, credentials, and contact configurations.
*   `account`: Financial accounts (e.g., checking, savings) representing funding sources.
*   `category`: Budgeting classifications for transactions.
*   `transaction`: Income, expense, and savings entries tying accounts and categories together.

---

## Database Schema & Data Model

The application uses an SQL schema initialized via `schema.sql`.

    users {
        int id PK "Identity"
        string name
        string user_name UNIQUE
        string email
        string password
        string street
        string suite
        string city
        string zipcode
        double geo_lng
        double geo_lat
        string phone
        string website
        string company_name
        string company_catch_phrase
        string company_bs
    }

    accounts {
        int id PK "Identity"
        int user_id FK
        string name
        double balance
    }

    categories {
        int id PK "Identity"
        int user_id FK
        string name
    }

    transactions {
        int id PK "Identity"
        int user_id FK
        double amount
        string type "INCOME, EXPENSE, SAVINGS_*"
        int account_id FK
        int category_id FK
        date date
    }
```

### Table Relationships
*   **Users** are the central entity. All accounts, categories, and transactions belong to a specific user (`user_id` foreign key).
*   **Transactions** must reference a valid `user_id`, `account_id` (funding source), and `category_id` (classification).
*   **Transaction Types**: Restricted by DB-level constraints to `INCOME`, `EXPENSE`, `SAVINGS_FD` (Fixed Deposit), `SAVINGS_RD` (Recurring Deposit), and `SAVINGS_STOCKS`.

---

## Startup Data Ingestion

The application automatically seeds the database on startup if it contains no existing records. Seeding is governed by Spring's `CommandLineRunner` classes executed in a specific sequence using the `@Order` annotation:
1.  **User Seeding** (`@Order(1)`): Reads `users.json`, hashes passwords using `BCryptPasswordEncoder`, and saves users.
2.  **Account & Category Seeding** (`@Order(2)`): Reads `accounts.json` and `categories.json` to set up funding accounts and transaction categories linked to the seeded users.
3.  **Transaction Seeding** (`@Order(3)`): Reads `transactions.json` to populate transaction history.

---

## API Endpoint Reference

### 1. Authentication Endpoints (`/api/auth`)
*   `POST /api/auth/register`: Register a new user.
    *   *Request Body*: `RegisterRequestDTO` (`username`, `email`, `password`)
*   `POST /api/auth/login`: Authenticate a user and receive a JWT.
    *   *Request Body*: `LoginRequestDTO` (`email`, `password`)
    *   *Response*: `JwtResponse` (`token`, `email`)

### 2. User Profile Endpoints (`/api/users`)
*   `GET /api/users/me`: Fetch profile details of the currently authenticated user.
    *   *Response*: Returns user information with the password field wiped out for security.

### 3. Accounts Endpoints (`/api/accounts`)
*   `GET /api/accounts/`: Fetch all accounts belonging to the authenticated user.
*   `POST /api/accounts`: Add a new account.
*   `PUT /api/accounts/{id}`: Edit an existing account.
*   `DELETE /api/accounts/{id}`: Delete an account.

### 4. Categories Endpoints (`/api/categories`)
*   `GET /api/categories/`: Fetch all categories belonging to the authenticated user.
*   `POST /api/categories`: Add a new category.
*   `PUT /api/categories/{id}`: Update an existing category.
*   `DELETE /api/categories/{id}`: Delete a category.

### 5. Transactions Endpoints (`/api/transactions`)
*   `GET /api/transactions/`: Fetch all transactions for the authenticated user, **fully joined** with associated account and category names (returns list of `TransactionResponseDTO`).
*   `GET /api/transactions/{id}`: Fetch a specific transaction.
*   `POST /api/transactions`: Create a transaction (automatically links the authenticated user's ID).
*   `PUT /api/transactions/{id}`: Update a transaction.
*   `DELETE /api/transactions/{id}`: Delete a transaction.

---

## Frontend-Backend Coupling & Integration

The backend is specifically engineered to support a decoupled frontend repository (typically a React/Vite project named `expense-tracker-frontend`). Understanding the coupling points is critical for correct client integration:

### 1. CORS Configuration (Cross-Origin Resource Sharing)
To allow the browser client (hosted on standard frontend development ports) to safely communicate with the API, CORS settings are strictly defined in `SecurityConfig.java` and `AuthController.java`:
*   **Allowed Origins**: `http://localhost:5173` (Vite dev server) and `http://localhost:3000` (React/Next dev server).
*   **Allowed Methods**: `GET`, `POST`, `PUT`, `DELETE`, and `OPTIONS` (essential for preflight checks).
*   **Allowed Headers**: `Authorization` (required for passing the JWT token), `Content-Type` (required for JSON payloads), and `Cache-Control`.
*   **Credentials**: `setAllowCredentials(true)` is enabled, permitting session management or HTTP cookies if needed.

### 2. JWT Authentication Lifecycle
The frontend must manage the session using JSON Web Tokens:
1.  **POST**s login credentials to `/api/auth/login`.
2.  On success, receives a `token` (JWT) and stores it locally (e.g. in `localStorage` or session storage).
3.  Injects the token in the `Authorization` header on every subsequent API request:
    `Authorization: Bearer <your_jwt_token>`
4.  Intercepted on the backend by `AuthTokenFilter`, which validates the token, extracts the user's email, and registers their session context.

### 3. Development Security Bypass Filter
For rapid frontend testing and local UI design iterations, the backend includes a custom filter: `SecurityBypassFilter.java`.
*   **Mechanism**: If `app.security.enabled=false` is set in your `application.properties`, the security filter chain automatically injects a mock authentication token representing the default user (`john.doe@example.com`, ID: `1`) for every request.
*   **Impact on Frontend**: When enabled, the frontend does not need to send the `Authorization: Bearer` header. The backend automatically associates all reads/writes with the seeded John Doe developer account, making local mock testing hassle-free.

### 4. Data Transfer Object (DTO) Optimization
Rather than forcing the frontend to perform multiple API calls to resolve names for account IDs or category IDs, the backend exposes the `GET /api/transactions/` endpoint.
*   This endpoint returns `TransactionResponseDTO` objects which include both `accountId`/`categoryId` and their human-readable equivalents `accountName`/`categoryName` resolved via database joins.
*   The frontend can bind this data directly to lists, grids, and dashboard visualization widgets without nested fetch loops.

### 5. Enum & Type Safety
The enum `TransactionType.java` governs transactional categories:
*   `INCOME` and `EXPENSE`
*   `SAVINGS_FD` (Fixed Deposit), `SAVINGS_RD` (Recurring Deposit), `SAVINGS_STOCKS`
The frontend input forms and chart classification logic must map values to these exact strings, as database constraints in the SQL file check for these values explicitly.

---

## Getting Started

### Prerequisites
*   **Java JDK 21** or higher.
*   **PostgreSQL** running locally (or adjust datasource settings for H2 console development).

### 1. Database Setup
Ensure PostgreSQL is running and update credentials in `src/main/resources/application.properties` (or inject them via environment variables):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. Running the Application
Run the Spring Boot application using the Gradle wrapper:
```bash
# On Windows
gradlew.bat bootRun

# On Unix/macOS
./gradlew bootRun
```
The API server will launch and listen on `http://localhost:8085`.
