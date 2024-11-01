#!/bin/sh

echo "Cleaning up Minikube configurations..."

echo "\n-----------------------------------------------------\n"

echo "Disabling NGINX Ingress addon..."

# Disable the NGINX Ingress addon in Minikube
minikube addons disable ingress

echo "\n-----------------------------------------------------\n"

echo "Deleting namespace 'springboot-kubernetes'..."

# Delete the 'springboot-kubernetes' namespace if it exists
kubectl delete namespace springboot-kubernetes --ignore-not-found=true

echo "\n-----------------------------------------------------\n"

echo "Removing 'ingress-ready' label from Minikube node..."

# Remove the 'ingress-ready' label from the Minikube node
kubectl label nodes minikube ingress-ready-

echo "\n-----------------------------------------------------\n"

# Confirm the successful removal of configurations
echo "Cleanup completed. All configurations set by the initial setup have been removed."