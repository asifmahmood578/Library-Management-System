# Library Management System (LMS) - Spring Boot Project

A complete Library Management System built using **Spring Boot**, **MySQL**, and **REST APIs**, featuring **Customer Registration**, **Book Management**, **Borrow/Return Logic**, **Fine Calculation**, **Dashboards**, and more. ✅

---

## 🛠 Technologies Used

- Java 17
- Spring Boot 3.3.2
- Spring Data JPA
- MySQL Database
- Maven
- Jakarta Validation
- JUnit 5 & Mockito
- Swagger (springdoc-openapi)
- JaCoCo (Code Coverage)
- Git & GitHub

---

## 📁 Project Structure

com.example.lib
├── controller # REST API Controllers
├── service # Business Logic
├── model # Entity Classes
├── repository # JPA Repositories
├── dto (optional) # Data Transfer Objects (if added)
├── exception # Global Exception Handling
└── LibApplication.java # Main class


---

## 🔐 Authentication

- **Register**: `/api/auth/register` → Allows a new customer to register.
- **Login**: `/api/auth/login` → Validates user credentials.

---

## 📚 Book Endpoints

| Method | Endpoint                  | Description                     |
|--------|---------------------------|---------------------------------|
| GET    | `/api/books`              | List all books                  |
| GET    | `/api/books/{id}`         | Get book by ID                  |
| GET    | `/api/books/search`       | Search by title or author       |
| POST   | `/api/books/add`          | Add a new book                  |
| PUT    | `/api/books/edit/{id}`    | Update book details             |
| DELETE | `/api/books/delete/{id}`  | Delete book                     |

---

## 👤 Customer Endpoints

| Method | Endpoint                      | Description             |
|--------|-------------------------------|-------------------------|
| GET    | `/api/customers`              | Get all customers       |
| GET    | `/api/customers/{id}`         | Get customer by ID      |
| POST   | `/api/customers/add`          | Add a customer          |
| PUT    | `/api/customers/edit/{id}`    | Update customer         |
| DELETE | `/api/customers/delete/{id}`  | Delete customer         |

---

## 📖 Borrowing Endpoints

| Method | Endpoint                                      | Description             |
|--------|-----------------------------------------------|-------------------------|
| POST   | `/api/borrow/{bookId}/customer/{customerId}`  | Borrow a book           |
| PUT    | `/api/return/{bookId}/customer/{customerId}`  | Return a book           |

---

## 💰 Fine Endpoints

| Method | Endpoint                              | Description                 |
|--------|---------------------------------------|-----------------------------|
| GET    | `/api/customers/{id}/fines`           | Get all fines by customer   |

---

## 📊 Dashboard Endpoints

| Method | Endpoint                          | Description                 |
|--------|-----------------------------------|-----------------------------|
| GET    | `/api/dashboard/admin`            | Admin dashboard overview    |
| GET    | `/api/dashboard/customer/{id}`    | Customer's dashboard view   |

---

## 🧪 Unit Testing & Coverage

- Written using **JUnit 5** and **Mockito**.
- Run tests using:

- Code coverage reports with **JaCoCo** are generated at:
