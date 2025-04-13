#!/bin/bash
set -e

echo "Deleting any existing kind cluster (if present)..."
kind delete cluster || true

echo "Creating new kind cluster..."
kind create cluster --config kind-config.yaml

# Wait a moment to ensure the cluster is stable
echo "Waiting for cluster to be ready..."
sleep 20

# Deploy Bitnami MongoDB
echo "Deploying MongoDB..."
helm install bitnami ./charts/bitnami-mongodb

# Deploy the consumer application
echo "Deploying Consumer application..."
helm install java-service ./charts/java-demo-chart

# Deploy the provider application
echo "Deploying Provider application..."
helm install java-service-provider ./charts/java-demo-chart-provider

# Deploy the TestStubSystem application
echo "Deploying TestStubSystem application..."
helm install java-stub ./charts/java-stub-chart


# Show deployments and services
echo "Current cluster state:"
kubectl get all

echo "All applications deployed."