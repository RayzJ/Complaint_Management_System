# Database Setup Guide

## 🗄️ MySQL Integration

The Complaint Management System now includes MySQL database integration with Docker.

## 📋 Database Schema

### Tables Created:
- **users** - User accounts (Admin, Support Agent, Customer)
- **tickets** - Complaint tickets with status tracking
- **notifications** - User notifications system
- **assignments** - Ticket assignment history
- **ticket_status_history** - Status change tracking

## 🚀 Quick Start

### 1. Full Application (Production)
```bash
# Build and start all services
docker-compose up --build

# Access application
Frontend: http://localhost
Backend API: http://localhost:8080
```

### 2. Database Only (Development)
```bash
# Start only MySQL and Adminer
docker-compose -f docker-compose.dev.yml up

# Access database
Adminer: http://localhost:8081
MySQL: localhost:3307
```

## 🔐 Database Credentials

### Production (docker-compose.yml)
- **Host**: mysql:3306 (internal) / localhost:3306 (external)
- **Database**: complaint_db
- **Username**: complaint_user
- **Password**: complaint_pass
- **Root Password**: rootpassword

### Development (docker-compose.dev.yml)
- **Host**: localhost:3307
- **Database**: complaint_db
- **Username**: complaint_user
- **Password**: complaint_pass

## 👥 Sample Users (Password: password123)

| Username | Role | Full Name | Email |
|----------|------|-----------|-------|
| admin1 | ADMIN | Admin User | admin@complaint.com |
| support1 | SUPPORT_AGENT | John Smith | support1@complaint.com |
| support2 | SUPPORT_AGENT | Mike Wilson | support2@complaint.com |
| customer1 | CUSTOMER | Jane Doe | customer1@complaint.com |
| customer2 | CUSTOMER | Sarah Johnson | customer2@complaint.com |

## 🛠️ Database Management

### Using Adminer (Web Interface)
1. Start dev environment: `docker-compose -f docker-compose.dev.yml up`
2. Open: http://localhost:8081
3. Login with MySQL credentials above

### Using MySQL CLI
```bash
# Connect to running container
docker exec -it complaint-mysql mysql -u complaint_user -p complaint_db

# Or connect from host
mysql -h localhost -P 3306 -u complaint_user -p complaint_db
```

## 🔄 Data Persistence

- **Production**: Data persists in `mysql_data` Docker volume
- **Development**: Data persists in `mysql_dev_data` Docker volume

## 🧹 Reset Database
```bash
# Stop containers
docker-compose down

# Remove volumes (WARNING: This deletes all data)
docker volume rm complaint-management-system_mysql_data

# Restart
docker-compose up --build
```

## 🐛 Troubleshooting

### Connection Issues
- Ensure MySQL container is healthy: `docker-compose ps`
- Check logs: `docker-compose logs mysql`
- Verify network: `docker network ls`

### Performance Issues
- Increase connection pool size in `application-prod.properties`
- Monitor with: `docker stats`

### Data Issues
- Check init.sql execution: `docker-compose logs mysql`
- Verify tables: Connect via Adminer and check structure