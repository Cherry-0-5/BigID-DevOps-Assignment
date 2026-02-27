# IP Echo API Service

A high-performance RESTful API service built with Spring Boot to echo the client's IP address, respecting `X-Forwarded-For` headers for proxy compatibility.

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

   Method,Endpoint,Description
GET,/,Returns the client IP address (JSON).
GET,/health,"Returns {""status"": ""UP""}."
GET,/ready,"Returns {""status"": ""READY""}."
