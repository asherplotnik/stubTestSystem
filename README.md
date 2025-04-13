# Kubernetes Testing System
This project provides a complete Kubernetes-based testing system for QA, including backend services, stub services for simulating responses, and infrastructure configuration. It uses Helm charts to deploy consumer and provider services, a Bitnami MongoDB chart for data, and custom Java services for stubbing. A Kind cluster is used for local testing, and the deployment can be executed with one command via a shell script.

## Overview
**Purpose:**
This system is designed to help QA testers validate backend-for-frontend workflows. It dynamically routes requests and provides stub responses for testing. The system includes:

Consumer Service: A Java Spring Boot service acting as the front-end consumer.

Provider Service: A Java Spring Boot service acting as the backend provider.

Stub Service: A Java Spring Boot stub service that intercepts calls and returns pre-configured responses.

MongoDB: A Bitnami chart providing a MongoDB instance for data persistence.

Technology Stack:

Kubernetes (local cluster via Kind)

Helm for package management

Java Spring Boot services

Fabric8 Kubernetes Client (for dynamic resource creation)

## Repository Structure
perl
Copy
my-k8s-project/

├── README.md

├── deploy-all.sh               # Script to deploy the entire system

├── kind-config.yaml            # Kind cluster configuration (with extra port mappings)

├── charts/                     # Helm chart definitions for services and MongoDB

│   ├── java-demo-chart/                # Consumer service chart

│   ├── java-demo-chart-provider/       # Provider service chart

│   └── bitnami/                        # Bitnami charts (for MongoDB)

├── services/                   # Source code for all Java services

│   ├── java-demo-service/              # Consumer service code

│   ├── java-demo-service-provider/     # Provider service code

│   └── java-stub-service/              # Stub service code

└── docs/

    └── deployment-instructions.md      # Additional deployment notes
Prerequisites
Make sure you have the following installed on your system:

Docker: Required for running Kind.

Kind: Kubernetes in Docker (for local testing)
Installation instructions

Helm: Package manager for Kubernetes
Installation instructions

kubectl: Command-line tool for Kubernetes
Installation instructions

Java & Maven: To build your Java services.

Deployment Instructions
Follow these steps to deploy the complete system:

Clone the Repository:

bash
Copy
git clone https://github.com/yourusername/my-k8s-project.git
cd my-k8s-project

Deploy the System with the Deployment Script:

The deploy-all.sh script will:

Delete any existing Kind cluster.

Create a new Kind cluster using kind-config.yaml.

Deploy MongoDB, consumer service, provider service, and stub service using Helm.

Run the deployment script:

bash
Copy
chmod +x deploy-all.sh
./deploy-all.sh
Monitor the output for any errors. Once completed, verify the deployments using:

bash
Copy
kubectl get pods,svc -n default
Accessing the Services:

The consumer service is typically exposed on NodePort 30080.

The provider service is exposed on NodePort 30081.

The stub service is exposed on NodePort 30084.

MongoDB is deployed as a ClusterIP service (internal).

You can access endpoints (like Swagger UI or test endpoints) using the appropriate URL (e.g., http://<node-ip>:<NodePort>/<endpoint>).

Testing the Stub Deployment:

Use your testing API (or curl/Postman) to trigger stub deployment or to validate that the stub service is functioning correctly.

## Additional Information
RBAC & Permissions:
The Helm charts include necessary RBAC definitions (ServiceAccounts, ClusterRoles, and ClusterRoleBindings) for interacting with the Kubernetes API.

## Cleanup:
To tear down the Kind cluster, run:

bash
Copy
kind delete cluster --name kind
Documentation:
For more detailed information, refer to the docs/deployment-instructions.md file.

## Contact
If you have any questions or need further assistance, please feel free to open an issue or contact the project maintainer.

