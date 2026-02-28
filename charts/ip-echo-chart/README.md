# IP Echo Project - Helm Charts

This repository contains the Helm charts and Kubernetes manifests used to deploy the Backend and Frontend services to a Kubernetes cluster.

## 🏛️ Architectural Overview
This project uses a two-tier architecture deployed on Kubernetes:

1.  **Frontend (Public):** The Node.js frontend is exposed via a **LoadBalancer** service, allowing traffic to enter from the internet.
2.  **Backend (Private):** The Java backend runs entirely within the cluster using **ClusterIP** service and is not exposed to the internet. It communicates with the frontend internally.
3.  **Service Discovery:** The frontend uses the Kubernetes internal DNS to find and connect to the backend service.

## 🚀 Components

### 1. Backend Service (`ip-echo-api-service`)
* **Deployment:** Deploys the Java application pods.
* **Service:** `ClusterIP` service to expose the backend internally to the frontend.


### 2. Frontend Service (`ip-displayer-frontend`)
* **Deployment:** Deploys the Node.js application pods.
* **Service:** `LoadBalancer` service to expose the frontend to the internet via a Cloud Load Balancer.
* 

## 📂 Project Structure

```text
charts/ip-echo-chart/
├── .helmignore          # Files to ignore during packaging
├── Chart.yaml           # Metadata for the chart
├── values.yaml          # Default configuration values
└── templates/
    ├── _helpers.tpl     # Template helpers
    ├── NOTES.txt        # Usage notes
    ├── backend/
    │   ├── deployment.yaml      # Deployment manifest of ip-echo-api-service
    │   └── service.yaml         # Service manifest of ip-echo-api-service
    └── frontend/
        ├── deployment.yaml      # Deployment manifest of ip-displayer-frontend
        └── service.yaml         # Service manifest of ip-displayer-frontend
```

## 🚀 Helm Deployment Command

'''bash
# --- 1. Lint the Chart ---
# Validates the chart structure and templates for errors
helm lint ./charts/ip-echo-chart

# --- 2. Install the Chart ---
# Deploys the components for the first time
helm install ip-echo-release ./charts/ip-echo-chart

# --- 3. Upgrade the Chart ---
# Applies changes made to templates or values.yaml
helm upgrade ip-echo-release ./charts/ip-echo-chart

# --- 4. Uninstall the Chart ---
# Removes all resources deployed by the chart
helm uninstall ip-echo-release
'''


## 🏛️ Architectural Diagram

![Kubernetes Architecture Diagram](charts/ip-echo-chart/kubernetes-architecture-ip-echo-project.png)
