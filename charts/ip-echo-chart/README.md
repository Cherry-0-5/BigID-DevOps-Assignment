# IP Echo Project - Helm Charts

This repository contains the Helm charts and Kubernetes manifests used to deploy the Backend and Frontend services to a Kubernetes cluster.

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
    │   ├── deployment.yaml
    │   └── service.yaml
    └── frontend/
        ├── deployment.yaml
        └── service.yaml

## 🚀 Components

### 1. Backend Service (`ip-echo-api-service`)
* **Deployment:** Deploys the Java application pods.
* **Service:** `ClusterIP` service to expose the backend internally to the frontend.


### 2. Frontend Service (`ip-displayer-frontend`)
* **Deployment:** Deploys the Node.js (**Distroless**) application pods.
* **Service:** `LoadBalancer` service to expose the frontend to the internet via a Cloud Load Balancer.

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
