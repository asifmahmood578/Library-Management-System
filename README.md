# 📚 Library Management System (Spring Boot)

This is a **Library Management System** built with **Java Spring Boot**, **MySQL**, and **RESTful APIs**. The system allows **Admins** and **Customers** to manage books, borrow/return them, track fines, and monitor dashboards.

---

## 🔧 Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- REST APIs
- MySQL
- Maven
- Swagger UI (OpenAPI)
- JUnit 5 & Mockito (for Unit Testing)
- JaCoCo (Code Coverage)

---

## 📂 Features

### 👤 User Authentication
- **Register** new users (admin/customer)
- **Login** with credentials (no Spring Security used)

### 📚 Book Management (Admin)
- Add, edit, delete books
- Search by title, author, or keyword
- View all or individual book details

### 👥 Customer Management
- Add, edit, delete customers
- View all customers or by ID

### 🔄 Borrowing & Returning
- Borrow a book by customer ID
- Return a book and auto-calculate fines for late returns

### 💸 Fine Management
- Automatically generates fine if return is late
- View fine per customer

### 📊 Dashboards
- **Admin Dashboard**: total books, borrowed books, users, fines
- **Customer Dashboard**: books borrowed, fine summary

---

## 🔗 API Endpoints (REST)

### 📘 Book APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get book by ID |
| GET | `/api/books/search?query=xyz` | Search books |
| POST | `/api/books/add` | Add a new book |
| PUT | `/api/books/edit/{id}` | Update a book |
| DELETE | `/api/books/delete/{id}` | Delete a book |

### 👤 Customer APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers` | Get all customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| POST | `/api/customers/add` | Add a customer |
| PUT | `/api/customers/edit/{id}` | Update a customer |
| DELETE | `/api/customers/delete/{id}` | Delete a customer |

### 🔐 Auth APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login as user |

### 🔄 Borrowing APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/borrow/{bookId}/customer/{customerId}` | Borrow a book |
| PUT | `/api/return/{bookId}/customer/{customerId}` | Return a book |

### 💰 Fine APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers/{id}/fines` | View all fines for a customer |

### 📊 Dashboard APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/dashboard/admin` | Admin summary |
| GET | `/api/dashboard/customer/{id}` | Customer summary |

---

## 🧪 Testing

- **JUnit 5 + Mockito** used for service & controller testing
- Run tests using:
  ```bash
  mvn test
