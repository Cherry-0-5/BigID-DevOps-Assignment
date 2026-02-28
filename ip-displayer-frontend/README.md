# IP Displayer Frontend

A lightweight frontend application that consumes the IP Echo API and displays the client's IP address in a clean user interface.

## 🛠️ Tech Stack & Security
- **Runtime:** Node.js 20 (Distroless)
- **Framework:** Express.js (serving EJS templates)
- **Containerization:** Docker (Multi-stage build)

# Frontend Docker Container

This `Dockerfile` is responsible for packaging the frontend application into a secure and lightweight container image. It uses a **multi-stage build** process to ensure the final image only contains the necessary runtime files, improving security and reducing size.

### 🛡️ Distroless Containerization
This application uses a **Distroless** Docker image for production. Unlike standard "slim" images, Distroless images contain only the application and its runtime dependencies, removing shells, package managers, and other unnecessary binaries.

### Key Responsibilities

1.  **Preparation:** The build process sets up the environment and installs the necessary components for the application to run.
2.  **Packaging:** The application code is bundled into a minimal image that contains only what is needed to run, removing unnecessary tools to improve security.
3.  **Security:** The container is configured to run as a non-root user, enhancing security by limiting permissions.
4.  **Configuration:** The container is set to use port **80** and is configured to communicate with the backend service.

## 🚀 Infrastructure & Deployment
The frontend is deployed on Kubernetes and exposed to the internet using a **Cloud Load Balancer**.

- **Kubernetes Service:** Type `LoadBalancer`.
- **Function:** Automatically distributes incoming external traffic across multiple instances (Pods) of the frontend application for high availability and scalability.

## 📂 Project Structure
```text
/
├── server.js         # Express server configuration
├── package.json      # Dependencies and scripts
├── views/       
│   └── index.ejs/       # EJS templates (.ejs files)
└── Dockerfile        # Multi-stage definition (Build -> Distroless)
