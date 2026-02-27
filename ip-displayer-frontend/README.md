# IP Displayer Frontend

A lightweight frontend application that consumes the IP Echo API and displays the client's IP address in a clean user interface.

## 📸 Preview
![App Screenshot](assets/screenshot.png)

## 🛠️ Tech Stack & Security
- **Runtime:** Node.js 20 (Distroless)
- **Framework:** Express.js (serving EJS templates)
- **Containerization:** Docker (Multi-stage build)

### 🛡️ Distroless Containerization
This application uses a **Distroless** Docker image for production. Unlike standard "slim" images, Distroless images contain only the application and its runtime dependencies, removing shells, package managers, and other unnecessary binaries.

## 🚀 Infrastructure & Deployment
The frontend is deployed on Kubernetes and exposed to the internet using a **Cloud Load Balancer**.

- **Kubernetes Service:** Type `LoadBalancer`.
- **Function:** Automatically distributes incoming external traffic across multiple instances (Pods) of the frontend application for high availability and scalability.

## 📂 Project Structure
```text
/
├── server.js         # Express server configuration
├── package.json      # Dependencies and scripts
├── src/       
│   └── views/       # EJS templates (.ejs files)
└── Dockerfile        # Multi-stage definition (Build -> Distroless)
