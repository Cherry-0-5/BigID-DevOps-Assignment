# IP Echo API Service

A high-performance RESTful API service built with Java Spring Boot to echo the client's IP address, respecting `X-Forwarded-For` headers for proxy compatibility.

## 🚀 Features
- **Echo IP:** Returns the requester's IP address.
- **Header Parsing:** Correctly handles `X-Forwarded-For` for load balancers.
- **Health Check:** `/health` endpoint for Kubernetes liveness probes.
- **Readiness Check:** `/ready` endpoint for Kubernetes readiness probes.

## 🛠️ Tech Stack
- **Language:** Java 21
- **Framework:** Spring Boot 3.x
- **Build Tool:** Maven

## ⚙️ Installation & Running
1. Clone the repository:
   ```bash
   git clone https://github.com/Cherry-0-5/BigID-DevOps-Assignment.git
   cd ip-echo-api-service
   mvn spring-boot:run
   '''bash

## 📡 API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/` | Returns the client IP address in JSON format. |
| `GET` | `/health` | Returns `{"status": "UP"}` for liveness probes. |
| `GET` | `/ready` | Returns `{"status": "READY"}` for readiness probes. |

## 📂 Project Structure

```text
src/
├── main/
│   └── java/com/example/ipecho/
│       ├── IpController.java     # REST Controller handling HTTP requests
│       ├── IpService.java        # Business logic for IP extraction
│       └── IpEchoApplication.java # Spring Boot main class
└── test/
    └── java/com/example/ipecho/
        └── IpControllerTest.java # Integration tests using MockMvc
