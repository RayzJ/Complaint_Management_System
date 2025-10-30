#!/bin/bash

echo "🚀 Simple EC2 Deployment..."

# Check disk space first
echo "📊 Current disk space:"
df -h

# Clean up if needed
if [ $(df / | tail -1 | awk '{print $5}' | sed 's/%//') -gt 80 ]; then
    echo "⚠️ Low disk space, cleaning up..."
    ./cleanup-ec2.sh
fi

# Build backend JAR
echo "🔨 Building backend..."
cd demo
mvn clean package -DskipTests
cd ..

# Create simple frontend build
echo "🔨 Creating frontend build..."
mkdir -p complaint-management-frontend/dist/complaint-management-frontend
cp -r complaint-management-frontend/src/* complaint-management-frontend/dist/complaint-management-frontend/ 2>/dev/null || true

# Use simple docker-compose
echo "🐳 Starting containers..."
docker-compose -f docker-compose.simple.yml up --build -d

# Check status
echo "📋 Container status:"
docker ps

# Get public IP
PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4 2>/dev/null || echo "localhost")

echo ""
echo "🎉 Deployment complete!"
echo "Frontend: http://$PUBLIC_IP:8082"
echo "Backend:  http://$PUBLIC_IP:8081"