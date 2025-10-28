# 🔑 APIKey-generator

[![Java](https://img.shields.io/badge/Java-17-blue)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)]()
[![MongoDB](https://img.shields.io/badge/MongoDB-Docker%20Container-green)]()
[![JWT](https://img.shields.io/badge/Security-JWT-orange)]()
[![License](https://img.shields.io/badge/License-MIT-yellow)]()

A lightweight **API Key Management Service** built with **Spring Boot**, **Spring Security (JWT)**, and **MongoDB**.  
It provides secure key generation, authentication, and storage workflows.  
This project was created in one day as a learning exercise to explore these technologies in an integrated environment.

---

## 📚 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Requirements](#-requirements)
- [Installation & Execution](#-installation--execution)
- [API Usage](#-api-usage)
- [Testing the Endpoints](#-testing-the-endpoints)
- [Environment Variables](#-environment-variables)
- [Project Structure](#-project-structure)
- [License](#-license)

---

## ✨ Features

- 🔐 **API Key Generation** — Secure creation of API keys for users.
- 🧩 **JWT Authentication** — Token-based access control for API endpoints.
- 🗝️ **Key Validation** — Verify and authenticate using generated API keys.
- 📦 **MongoDB Integration** — Persistent key and user storage using MongoDB.
- ⚙️ **Spring Security** — Role-based authorization and request filtering.
- 🧠 **Learning Project** — Designed to practice modern Spring Boot patterns with Docker and JWT.

---

## 🧰 Tech Stack

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Security (JWT)**
- **MongoDB (via Docker)**
- **Maven 3.9+**

---

## 🧩 Requirements

Before running the project, make sure you have:

- **Java 21+**
- **Maven 3.9+**
- **Docker & Docker Compose**
- **MongoDB** running locally or via Docker

---

## ⚙️ Installation & Execution

### 1️⃣ Clone the Repository
```
git clone https://github.com/yourusername/APIKey-generator.git
cd APIKey-generator
```
2️⃣ Configure Environment Variables
Create a .env file in the project root using the following template
```
Copiar código
# --- MongoDB Configuration ---
MONGO_ROOT_USER='YourMongoUser'
MONGO_ROOT_PASS='YourMongoPassword'
MONGO_DB_NAME=apikeygenerator

# --- Spring Boot Configuration ---
SPRING_MONGO_HOST=localhost
SPRING_MONGO_PORT=27017

SPRING_MONGO_USER='${MONGO_ROOT_USER}'
SPRING_MONGO_PASS='${MONGO_ROOT_PASS}'
SPRING_MONGO_AUTH_DB='${MONGO_DB_NAME}'

# --- JWT Configuration ---
JWT_SECRET='your-super-secret-key'
JWT_EXPIRATION=86400000 # milliseconds (24h)

# --- Server Configuration ---
SERVER_PORT=8080
```

3️⃣ Run the Project
```
mvn spring-boot:run
```

4️⃣ Access the API
By default, the server runs at:
👉 http://localhost:8080

🚀 API Usage
Authentication Header
Most endpoints require a JWT token in the request header:
```
Authorization: Bearer <your_token_here>
```

### Example Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Authenticate user and return JWT token |
| `POST` | `/api/keys/generate` | Generate a new API key |
| `GET` | `/api/keys` | List all generated API keys |
| `DELETE` | `/api/keys/{id}` | Delete a specific API key |

🧪 Testing the Endpoints
You can test the API using your preferred HTTP client:

Insomnia 💤

Postman 🚀

Hoppscotch 🌐

Just import the routes and configure your request headers to include the JWT token.

🌍 Environment Variables
For convenience, you can find detailed information about configuring your .env file in .env.md.

🗂️ Project Structure
bash
Copiar código
APIKey-generator/
├── src/
│   ├── main/
│   │   └── java/com/apikeygen/apikeygenerator/
│   │       ├── config/          # Security & JWT configuration
│   │       ├── controller/      # REST API endpoints
│   │       ├── model/           # Data models (User, ApiKey)
│   │       ├── repository/      # MongoDB repositories
│   │       ├── service/         # Business logic and key handling
│   │       └── security/        # Filters, token management, etc.
│   └── resources/
│       └── application.yml
├── .env.example
├── .gitignore
├── pom.xml
└── README.md

📄 License
This project is licensed under the MIT License — feel free to use and modify it for your own learning or development projects.

