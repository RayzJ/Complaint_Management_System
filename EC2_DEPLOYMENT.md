# EC2 Manual Deployment Guide

## 🚀 Deploy Complaint Management System to AWS EC2

### Prerequisites
- AWS Account with EC2 access
- Key pair created in AWS
- Basic knowledge of SSH

## Step 1: Launch EC2 Instance

### 1.1 Create EC2 Instance
```bash
# Instance Details
AMI: Ubuntu Server 22.04 LTS
Instance Type: t2.medium (minimum for Docker + MySQL)
Storage: 20 GB GP3
Security Group: Create new with following rules
```

### 1.2 Security Group Rules
```bash
# Inbound Rules
SSH (22) - Your IP
HTTP (80) - 0.0.0.0/0
HTTPS (443) - 0.0.0.0/0
Custom TCP (8080) - 0.0.0.0/0  # Backend API
Custom TCP (3306) - Your IP    # MySQL (optional)
```

## Step 2: Connect to EC2 Instance

```bash
# SSH into your instance
ssh -i your-key.pem ubuntu@YOUR_EC2_PUBLIC_IP

# Update system
sudo apt update && sudo apt upgrade -y
```

## Step 3: Install Docker & Docker Compose

```bash
# Install Docker
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Add user to docker group
sudo usermod -aG docker ubuntu
newgrp docker

# Verify installation
docker --version
docker-compose --version
```

## Step 4: Clone Your Repository

```bash
# Clone your project
git clone https://github.com/RayzJ/Complaint_Management_System.git
cd Complaint_Management_System

# Verify files
ls -la
```

## Step 5: Build and Deploy

```bash
# Build and start all services
docker-compose up --build -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f
```

## Step 6: Test Your Application

### 6.1 Check Services
```bash
# Check if containers are running
docker ps

# Check specific service logs
docker-compose logs frontend
docker-compose logs backend
docker-compose logs mysql
```

### 6.2 Test Endpoints
```bash
# Test backend health
curl http://localhost:8080/actuator/health

# Test frontend
curl http://localhost

# Test database connection
docker exec -it complaint-mysql mysql -u complaint_user -p complaint_db
```

### 6.3 Access Application
```bash
# Open in browser
Frontend: http://YOUR_EC2_PUBLIC_IP
Backend API: http://YOUR_EC2_PUBLIC_IP:8080
```

## Step 7: Login and Test

### Test Users (Password: password123)
- **Admin**: admin1
- **Support**: support1
- **Customer**: customer1

### Test Flow
1. Login as customer1 → Create ticket
2. Login as admin1 → Assign ticket to support1
3. Login as support1 → Change ticket status
4. Check notifications working

## 🔧 Troubleshooting

### Container Issues
```bash
# Restart services
docker-compose restart

# Rebuild if needed
docker-compose down
docker-compose up --build -d

# Check disk space
df -h

# Check memory
free -h
```

### Network Issues
```bash
# Check security group allows traffic
# Verify EC2 public IP is correct
# Check if services are bound to 0.0.0.0

# Test internal connectivity
docker exec -it complaint-backend curl http://mysql:3306
```

### Database Issues
```bash
# Check MySQL logs
docker-compose logs mysql

# Connect to database
docker exec -it complaint-mysql mysql -u root -p

# Check tables
docker exec -it complaint-mysql mysql -u complaint_user -p complaint_db -e "SHOW TABLES;"
```

## 📊 Monitoring Commands

```bash
# Resource usage
docker stats

# Service status
docker-compose ps

# Logs (last 100 lines)
docker-compose logs --tail=100

# Follow logs in real-time
docker-compose logs -f backend
```

## 🛑 Stop Application

```bash
# Stop all services
docker-compose down

# Stop and remove volumes (deletes data)
docker-compose down -v
```

## 📝 Quick Deployment Script

Save this as `deploy.sh`:
```bash
#!/bin/bash
echo "🚀 Deploying Complaint Management System..."

# Pull latest changes
git pull origin master

# Stop existing containers
docker-compose down

# Build and start
docker-compose up --build -d

# Show status
docker-compose ps

echo "✅ Deployment complete!"
echo "Frontend: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)"
echo "Backend: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8080"
```

Make it executable: `chmod +x deploy.sh`
Run: `./deploy.sh`