# Docker Troubleshooting Guide

## 🚨 Common Issues & Solutions

### Issue 1: Network Error While Pulling Images
**Error**: `failed to copy: httpReadSeeker: failed open`

**Solutions**:
```bash
# 1. Try pulling MySQL image manually first
docker pull mysql:8.0

# 2. If that fails, try different registry
docker pull mysql:8.0 --platform linux/amd64

# 3. Use simplified compose file
docker-compose -f docker-compose.simple.yml up --build -d
```

### Issue 2: Port Conflicts
**Error**: `Port already in use`

**Solutions**:
```bash
# Check what's using the ports
netstat -ano | findstr :80
netstat -ano | findstr :8080
netstat -ano | findstr :3306

# Stop conflicting services or use different ports
# Use docker-compose.simple.yml (uses ports 8081, 8082, 3307)
```

### Issue 3: Build Failures
**Error**: `failed to build`

**Solutions**:
```bash
# Clean Docker cache
docker system prune -a

# Build images individually
docker build -t complaint-backend ./demo
docker build -t complaint-frontend ./complaint-management-frontend

# Check Dockerfile syntax
```

## 🔧 Step-by-Step Recovery

### Step 1: Clean Everything
```bash
# Stop all containers
docker-compose down -v

# Remove all containers and images
docker system prune -a

# Remove volumes
docker volume prune
```

### Step 2: Test Network Connectivity
```bash
# Test Docker Hub connectivity
docker pull hello-world

# If this fails, check your internet/firewall
```

### Step 3: Try Simple Setup
```bash
# Use simplified compose file
docker-compose -f docker-compose.simple.yml up --build -d

# Check status
docker ps
```

### Step 4: Manual Container Testing
```bash
# Start MySQL only
docker run -d --name test-mysql -e MYSQL_ROOT_PASSWORD=root mysql:8.0

# Check if it works
docker ps
docker logs test-mysql

# Clean up
docker rm -f test-mysql
```

## 🧪 Alternative Testing Methods

### Method 1: Use Pre-built Images
Create `docker-compose.prebuilt.yml`:
```yaml
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: complaint_db
    ports:
      - "3306:3306"
    
  backend:
    image: openjdk:17-jdk-slim
    command: echo "Backend placeholder"
    ports:
      - "8080:8080"
      
  frontend:
    image: nginx:alpine
    ports:
      - "80:80"
```

### Method 2: Build Locally First
```bash
# Build backend JAR first
cd demo
mvn clean package -DskipTests
cd ..

# Build frontend
cd complaint-management-frontend
npm install
npm run build
cd ..

# Then try Docker Compose
docker-compose up --build -d
```

## 🔍 Diagnostic Commands

```bash
# Check Docker version
docker --version
docker-compose --version

# Check Docker daemon
docker info

# Check available space
docker system df

# Check running containers
docker ps -a

# Check logs
docker-compose logs

# Check networks
docker network ls

# Check volumes
docker volume ls
```

## 🚀 Quick Fix Commands

```bash
# Restart Docker Desktop (Windows)
# Close Docker Desktop and restart it

# Clear DNS cache (Windows)
ipconfig /flushdns

# Try with different DNS
# Change Docker Desktop settings to use 8.8.8.8

# Use offline mode
docker-compose up --build -d --no-deps
```