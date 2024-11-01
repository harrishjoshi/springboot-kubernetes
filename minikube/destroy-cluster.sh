#!/bin/sh

echo "Cleaning up Minikube configurations..."

echo "\n-----------------------------------------------------\n"

echo "Disabling NGINX Ingress addon..."

# Disable the NGINX Ingress addon in Minikube
minikube addons disable ingress

echo "\n-----------------------------------------------------\n"

echo "Deleting all resources in 'springboot-kubernetes' namespace..."

# Delete all resources in the 'springboot-kubernetes' namespace if it exists
kubectl delete all --all --namespace springboot-kubernetes --ignore-not-found=true

echo "\n-----------------------------------------------------\n"

echo "Deleting namespace 'springboot-kubernetes'..."

# Delete the 'springboot-kubernetes' namespace if it exists
kubectl delete namespace springboot-kubernetes --ignore-not-found=true

echo "\n-----------------------------------------------------\n"

echo "Deleting all resources in 'ingress-nginx' namespace..."

# Delete all resources in the 'ingress-nginx' namespace if it exists
kubectl delete all --all --namespace ingress-nginx --ignore-not-found=true

echo "\n-----------------------------------------------------\n"

echo "Deleting namespace 'ingress-nginx'..."

# Delete the 'ingress-nginx' namespace if it exists
kubectl delete namespace ingress-nginx --ignore-not-found=true

echo "\n-----------------------------------------------------\n"

echo "Removing 'ingress-ready' label from Minikube node..."

# Remove the 'ingress-ready' label from the Minikube node
kubectl label nodes minikube ingress-ready-

echo "\n-----------------------------------------------------\n"

echo "Deleting Minikube cluster..."

# Delete the Minikube cluster
minikube delete --all --purge

echo "\n-----------------------------------------------------\n"

# Confirm the successful removal of configurations
echo "Cleanup completed. All configurations set by the initial setup have been removed, and Minikube cluster has been deleted."