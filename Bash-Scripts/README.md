# ⚙️  Recommended Workflow

Follow these steps in order to install Minikube, configure permissions, and deploy the application in local environment.

## 🚀 Run Installation

Execute the following commands to install necessary dependencies and start the Minikube cluster:

```bash
chmod +x install-minikube.sh
./install-minikube.sh
```

## 🚀 Refresh Group Permissions

**Note:** This step must be executed after `install-minikube.sh` script completes. This updates your current terminal session permissions to allow Docker commands to run without sudo permission.

```bash
newgrp docker
```

## 🚀 Run Deployment

Execute the deployment script to build the local images and deploy them to the Minikube cluster:

```bash
chmod +x deploy2minikube.sh
./deploy2minikube.sh
```
