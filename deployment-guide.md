# Complaint Management System - Deployment Guide

## Prerequisites

1. **AWS Account** with EC2 access
2. **Jenkins Server** with required plugins
3. **Ansible** installed on Jenkins server
4. **Docker** and **Docker Compose**
5. **AWS CLI** configured

## Step 1: AWS Setup

### Create Key Pair
```bash
aws ec2 create-key-pair --key-name complaint-key --query 'KeyMaterial' --output text > complaint-key.pem
chmod 400 complaint-key.pem
```

### Provision EC2 Instance
```bash
cd ansible
ansible-playbook provision-ec2.yml
```

## Step 2: Jenkins Configuration

### Required Plugins
- Docker Pipeline
- Ansible
- AWS Steps
- Git

### Global Tools Configuration
- **Maven**: Maven-3.9
- **NodeJS**: NodeJS-18
- **Docker**: Latest

### Environment Variables
```
AWS_ACCESS_KEY_ID=your_access_key
AWS_SECRET_ACCESS_KEY=your_secret_key
AWS_DEFAULT_REGION=us-east-1
```

## Step 3: Update Configuration Files

### Update Ansible Inventory
Edit `ansible/inventory/hosts`:
```ini
[ec2_instances]
complaint-server ansible_host=YOUR_EC2_PUBLIC_IP ansible_user=ubuntu ansible_ssh_private_key_file=~/.ssh/complaint-key.pem
```

### Update Jenkins Pipeline
Edit `Jenkinsfile` with your Git repository URL.

## Step 4: Deploy Application

### Manual Deployment
```bash
# Build and deploy
docker-compose up --build -d

# Check status
docker-compose ps
```

### Jenkins Pipeline Deployment
1. Create new Pipeline job in Jenkins
2. Point to your Git repository
3. Run the pipeline

## Step 5: Access Application

- **Frontend**: http://YOUR_EC2_PUBLIC_IP
- **Backend API**: http://YOUR_EC2_PUBLIC_IP:8080

## Monitoring Commands

```bash
# Check container logs
docker-compose logs -f

# Check container status
docker ps

# Restart services
docker-compose restart
```

## Security Considerations

1. Use HTTPS in production
2. Configure proper security groups
3. Use environment variables for secrets
4. Regular security updates

## Troubleshooting

### Common Issues
1. **Port conflicts**: Ensure ports 80 and 8080 are available
2. **Permission issues**: Check Docker group membership
3. **Network issues**: Verify security group rules
4. **Build failures**: Check Jenkins logs and dependencies