# DevInTen - Complaint Management System

A full-stack complaint management system with role-based access control, built with Angular, Spring Boot, and MySQL.

## 🏗️ Architecture

- **Frontend**: Angular 17 with Bootstrap
- **Backend**: Spring Boot 3.x with JWT Authentication
- **Database**: MySQL 8.0
- **Containerization**: Docker & Docker Compose
- **CI/CD**: Jenkins Pipeline
- **Deployment**: AWS EC2 with Ansible

## 👥 User Roles

- **👤 Customer**: Create tickets, track status, receive notifications
- **🧑🔧 Support Agent**: View assigned tickets, update status, resolve issues
- **🧑💼 Admin**: Manage all tickets, assign agents, user management

## 🚀 Quick Start

### Prerequisites

- Docker & Docker Compose
- Node.js 18+ (for local development)
- Java 17+ (for local development)
- MySQL 8.0 (for local development)

### 🔑 Default Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin_1` | `admin1@123` |
| Support Agent | `support_1` | `support1@123` |
| Customer | `customer_1` | `customer1@123` |

## 📦 Manual Deployment

### Option 1: Docker Compose (Recommended)

```bash
# Clone the repository
git clone https://github.com/RayzJ/Complaint_Management_System.git
cd Complaint_Management_System

# Start all services
docker-compose up -d

# Check status
docker-compose ps
```

**Access the application:**
- Frontend: http://localhost
- Backend API: http://localhost:8080
- Database: localhost:3306

### Option 2: Local Development

#### Backend Setup
```bash
cd demo
mvn clean install
mvn spring-boot:run
```

#### Frontend Setup
```bash
cd complaint-management-frontend
npm install
ng serve
```

#### Database Setup
```bash
# Create database
mysql -u root -p
CREATE DATABASE complaint_db;

# Import schema
mysql -u root -p complaint_db < init.sql
```

## ☁️ AWS EC2 Deployment

### Prerequisites
- AWS EC2 instance (Amazon Linux 2023)
- Security Groups: Allow ports 22, 80, 8080
- SSH key pair

### Manual EC2 Setup

```bash
# SSH to EC2
ssh -i your-key.pem ec2-user@your-ec2-ip

# Install Docker
sudo dnf update -y
sudo dnf install docker -y
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -a -G docker ec2-user

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Create app directory
mkdir ~/cms-app && cd ~/cms-app

# Create docker-compose.yml
cat > docker-compose.yml << 'EOF'
version: '3.8'
services:
  database:
    image: riyaz05042004/cms:complaint-database-v7
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: complaint_db
    ports:
      - "3306:3306"
    restart: unless-stopped

  backend:
    image: riyaz05042004/cms:complaint-backend-v7
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://database:3306/complaint_db?useSSL=false&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: rootpassword
    depends_on:
      - database
    restart: unless-stopped

  frontend:
    image: riyaz05042004/cms:complaint-frontend-v7
    ports:
      - "80:80"
    depends_on:
      - backend
    restart: unless-stopped
EOF

# Deploy application
docker-compose up -d
```

## 🤖 Automated CI/CD Pipeline

### Jenkins Setup

#### 1. Install Jenkins
```bash
# On Windows (using Docker)
docker run -d --name jenkins-server --user root -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts

# Access Jenkins
http://localhost:8080
```

#### 2. Configure Jenkins
1. **Install Plugins**: Docker Pipeline, Ansible
2. **Add Credentials**:
   - Docker Hub: `dockerhub-credentials` (username/password)
   - SSH Key: Upload your EC2 key file
3. **Create Pipeline Job**: Point to GitHub repository

#### 3. Pipeline Stages
The automated pipeline includes:

```yaml
Stages:
  1. 📦 Checkout Code (from GitHub)
  2. 🏗️ Build Backend Image (Spring Boot)
  3. 🗄️ Build Database Image (MySQL + Schema)
  4. 🌐 Build Frontend Image (Angular + Nginx)
  5. 🔐 Login to Docker Hub
  6. 📤 Push Images to Registry
  7. 🚀 Deploy to EC2 with Ansible
```

### Ansible Deployment

#### Setup Ansible (WSL/Linux)
```bash
# Install Ansible
sudo apt update
sudo apt install ansible -y

# Navigate to project
cd ansible/

# Update inventory with your EC2 IP
nano inventory/hosts.yml

# Test connection
ansible ec2_instances -i inventory/hosts.yml -m ping

# Deploy application
ansible-playbook -i inventory/hosts.yml playbooks/deploy.yml -v
```

#### Ansible Configuration Files
- `inventory/hosts.yml`: EC2 connection details
- `playbooks/deploy.yml`: Deployment automation
- `group_vars/all.yml`: Configuration variables
- `templates/docker-compose.yml.j2`: Dynamic compose template

## 🔧 Configuration

### Environment Variables

#### Backend (application.properties)
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/complaint_db
spring.datasource.username=root
spring.datasource.password=yourpassword

# JWT
app.jwt.secret=MySuperSecretJWTKey1234567890
app.jwt.expirationMs=3600000

# Server
server.port=8080
```

#### Frontend (environment.ts)
```typescript
export const environment = {
  production: false,
  apiUrl: '/api'  // Uses nginx proxy in production
};
```

### Docker Images
- **Backend**: `riyaz05042004/cms:complaint-backend-v{BUILD_NUMBER}`
- **Frontend**: `riyaz05042004/cms:complaint-frontend-v{BUILD_NUMBER}`
- **Database**: `riyaz05042004/cms:complaint-database-v{BUILD_NUMBER}`

## 📊 Database Schema

### Core Tables
- `users`: User accounts and roles
- `roles`: System roles (Admin, Support Agent, Customer)
- `tickets`: Complaint/ticket information
- `assignments`: Ticket-agent assignments
- `notifications`: System notifications
- `comments`: Ticket communication
- `attachments`: File uploads
- `ticket_status_history`: Status change tracking

## 🛠️ Development

### Project Structure
```
Complaint-Management-System/
├── 🌐 complaint-management-frontend/    # Angular app
├── 🖥️ demo/                            # Spring Boot backend
├── 🗄️ database/                        # MySQL setup
├── 🤖 ansible/                         # Deployment automation
├── ⚙️ Jenkinsfile                      # CI/CD pipeline
├── 🐳 docker-compose.yml               # Local development
└── 📋 README.md                        # This file
```

### API Endpoints
- `POST /api/auth/login`: User authentication
- `GET /api/tickets`: Fetch tickets by role
- `POST /api/tickets`: Create new ticket
- `PUT /api/tickets/{id}/status`: Update ticket status
- `POST /api/tickets/assign/{id}`: Assign ticket to agent
- `GET /api/notifications`: Fetch user notifications
- `POST /api/notifications/send`: Send notification

## 🚨 Troubleshooting

### Common Issues

#### 1. CORS Errors
- Ensure backend CORS is configured for frontend domain
- Check nginx proxy configuration

#### 2. Database Connection
```bash
# Check database container
docker logs cms-database

# Connect to database
docker exec -it cms-database mysql -uroot -prootpassword
```

#### 3. Jenkins Build Failures
```bash
# Check Docker daemon
docker ps

# Clean Docker cache
docker system prune -f
```

#### 4. Ansible Connection Issues
```bash
# Test SSH connection
ssh -i ~/.ssh/your-key.pem ec2-user@your-ec2-ip

# Check Ansible inventory
ansible-inventory -i inventory/hosts.yml --list
```

## 🔒 Security

- JWT-based authentication
- Role-based access control (RBAC)
- Password encryption (BCrypt)
- CORS protection
- SQL injection prevention (JPA/Hibernate)

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request


## 👨‍💻 Author

**Riyaz** - [GitHub](https://github.com/RayzJ)

---

## 🎯 Quick Commands Reference

```bash
# Local Development
docker-compose up -d                    # Start all services
docker-compose down                     # Stop all services
docker-compose logs -f                  # View logs

# Production Deployment
ansible-playbook -i inventory/hosts.yml playbooks/deploy.yml -v

# Jenkins Pipeline
# Push code to GitHub → Automatic build and deployment