#!/bin/bash

echo "🚀 Deploying Complaint Management System to EC2..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    print_error "Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    print_error "Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

# Pull latest changes from Git
print_status "Pulling latest changes from Git..."
git pull origin master

# Stop existing containers
print_status "Stopping existing containers..."
docker-compose down

# Remove old images (optional)
print_warning "Removing old Docker images..."
docker system prune -f

# Build and start services
print_status "Building and starting services..."
docker-compose up --build -d

# Wait for services to start
print_status "Waiting for services to start..."
sleep 30

# Check service status
print_status "Checking service status..."
docker-compose ps

# Get EC2 public IP
PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4 2>/dev/null)

if [ -z "$PUBLIC_IP" ]; then
    PUBLIC_IP="localhost"
    print_warning "Could not detect EC2 public IP. Using localhost."
fi

# Test backend health
print_status "Testing backend health..."
if curl -s http://localhost:8080/actuator/health > /dev/null; then
    print_status "Backend is healthy ✅"
else
    print_warning "Backend health check failed. Check logs with: docker-compose logs backend"
fi

# Test frontend
print_status "Testing frontend..."
if curl -s http://localhost > /dev/null; then
    print_status "Frontend is accessible ✅"
else
    print_warning "Frontend test failed. Check logs with: docker-compose logs frontend"
fi

# Display access URLs
echo ""
echo "🎉 Deployment Complete!"
echo "=================================="
echo "Frontend URL: http://$PUBLIC_IP"
echo "Backend API:  http://$PUBLIC_IP:8080"
echo "=================================="
echo ""
echo "📋 Test Users (Password: password123):"
echo "Admin:    admin1"
echo "Support:  support1"
echo "Customer: customer1"
echo ""
echo "🔧 Useful Commands:"
echo "View logs:    docker-compose logs -f"
echo "Check status: docker-compose ps"
echo "Stop app:     docker-compose down"
echo "Restart:      docker-compose restart"
echo ""

# Show container status
print_status "Container Status:"
docker-compose ps