#!/bin/sh

# Script to initialize Minikube, set up a namespace, configure Ingress, and verify readiness

echo "Starting Minikube with specified resource allocations and port mappings..."

# Start Minikube with Docker driver, setting 2 CPUs, 4GB memory, and custom port mappings for specific services
minikube start --driver=docker --nodes=1 --cpus=2 --memory=4096 \
  --ports=80:80,443:443,18080:30090,30080:30080

echo "\n-----------------------------------------------------\n"

echo "Labeling Minikube node as 'ingress-ready'..."

# Label the Minikube node to signal it is ready for Ingress, which is required by some Ingress configurations
kubectl label nodes minikube ingress-ready=true

echo "\n-----------------------------------------------------\n"

echo "Creating namespace 'springboot-kubernetes'..."

# Create a dedicated namespace called 'springboot-kubernetes' for resources related to this project
kubectl create namespace springboot-kubernetes

echo "\n-----------------------------------------------------\n"

echo "Enabling NGINX Ingress addon in Minikube..."

# Enable the built-in NGINX Ingress controller in Minikube to handle Ingress resources
minikube addons enable ingress

echo "\n-----------------------------------------------------\n"

echo "Waiting for NGINX Ingress controller to be ready in 'springboot-kubernetes' namespace..."

# Wait until the NGINX Ingress controller pods are fully ready within the 'springboot-kubernetes' namespace
# This ensures Ingress services are prepared to manage traffic routing
kubectl wait --namespace springboot-kubernetes \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/name=ingress-nginx \
  --timeout=180s

echo "\n"

# Confirm the successful initialization of the Minikube cluster with Ingress enabled
echo "Minikube cluster is ready in 'springboot-kubernetes' namespace. Happy Sailing!"