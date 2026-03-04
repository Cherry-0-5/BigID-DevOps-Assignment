#!/bin/bash

set -e

eval $(minikube docker-env)

docker build -t ip-echo-backend:local ./ip-echo-api-service
docker build -t ip-echo-frontend:local ./ip-displayer-frontend

kubectl create namespace staging --dry-run=client -o yaml | kubectl apply -f -

kubectl apply -f frontend/deployment.yaml -n staging
kubectl apply -f backend/deployment.yaml -n staging

kubectl rollout status deployment/ip-echo-frontend -n staging
kubectl rollout status deployment/ip-echo-backend -n staging

kubectl apply -f frontend/service.yaml -n staging
kubectl apply -f backend/service.yaml -n staging

echo "Application URL:"
minikube service ip-echo-frontend -n staging --url
