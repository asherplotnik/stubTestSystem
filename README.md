Kubernetes Testing System

This project provides a complete Kubernetes-based testing system for QA, including a front-end application pod, backend services, stub services for simulating responses, and infrastructure configuration. It uses Helm charts to deploy consumer and provider services, a Bitnami MongoDB chart for data, a custom Java stub service, and a dedicated front-end chart. A Kind cluster is used for local testing, and the deployment can be executed with one command via a shell script.

Overview

Purpose:
This system is designed to help QA testers validate full-stack workflows. It dynamically routes requests and provides stub responses for testing. The system includes:

Consumer Service: A Java Spring Boot service acting as the front-end consumer.

Provider Service: A Java Spring Boot service acting as the backend provider.

Stub Service: A Java Spring Boot stub service that intercepts calls and returns pre-configured responses.

Front-End Application: A standalone front-end pod (e.g., React) serving the user interface.

MongoDB: A Bitnami chart providing a MongoDB instance for data persistence.

Technology Stack:

Kubernetes (local cluster via Kind)

Helm for package management

Java Spring Boot services

Fabric8 Kubernetes Client (for dynamic resource creation)

Front-End Framework (e.g., React)

Repository Structure

my-k8s-project/

```
├── README.md
├── deploy-all.sh               # Script to deploy the entire system
├── kind-config.yaml            # Kind cluster configuration (with extra port mappings)
├── charts/                     # Helm chart definitions for services and MongoDB
│   ├── java-demo-chart/                # Consumer service chart
│   ├── java-demo-chart-provider/       # Provider service chart
│   ├── java-stub-chart/                # Stub service chart
│   ├── frontend-chart/                 # Front-end application chart
│   └── bitnami/                        # Bitnami charts (for MongoDB)
├── services/                   # Source code for all Java services
│   ├── java-demo-service/              # Consumer service code
│   ├── java-demo-service-provider/     # Provider service code
│   └── java-stub-service/              # Stub service code
└── docs/
    └── deployment-instructions.md      # Additional deployment notes
```

Prerequisites

Make sure you have the following installed on your system:

Docker Desktop: Required for running Kind.

Kind: Kubernetes in Docker (for local testing).

Helm: Package manager for Kubernetes.

kubectl: Command-line tool for Kubernetes.

Java & Maven: To build your Java services.

Deployment Instructions

Follow these steps to deploy the complete system:

Clone the Repository:

git clone https://github.com/yourusername/my-k8s-project.git
cd my-k8s-project

Deploy with the Script:

The deploy-all.sh script will:

Delete any existing Kind cluster.

Create a new Kind cluster using kind-config.yaml.

Deploy MongoDB, consumer service, provider service, stub service, and front-end application using Helm.

chmod +x deploy-all.sh
./deploy-all.sh

Note: The first deploy may take some time to pull all images.

Verify Deployments:

kubectl get pods,svc -n default

Accessing the Services

After deployment, you can access the services via NodePort or port forwarding:

Consumer Service: http://<node-ip>:30080

Provider Service: http://<node-ip>:30081

Stub Service:    http://<node-ip>:30084

Front-End App:   http://<node-ip>:30082(Check the frontend service NodePort via kubectl get svc if different.)

Cleanup

To tear down the Kind cluster, run:

kind delete cluster --name kind

Contact

If you have any questions or need further assistance, please feel free to open an issue or contact the project maintainer. Asher plotnik asherplotnik@gmail.com