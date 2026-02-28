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

# Backend Docker Container

This `Dockerfile` is responsible for packaging the Java backend application into a container image. It takes the compiled JAR file produced by the build pipeline and prepares it to run safely and efficiently in a Kubernetes environment.


### Key Responsibilities

1.  **Lightweight Runtime:** It uses a slim Java runtime environment to keep the container image size small.
2.  **Security:** It configures the application to run as a non-root user for improved security.
3.  **Application Packaging:** It takes the finalized JAR file and sets it up to run inside the container.
4.  **Network Configuration:** It exposes port **8088** for the application to handle requests.

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
