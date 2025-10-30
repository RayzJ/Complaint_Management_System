#!/bin/bash

echo "🧪 Testing Docker Compose Setup Locally"
echo "========================================"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker Desktop."
    exit 1
fi

echo "✅ Docker is running"

# Check if docker-compose file exists
if [ ! -f "docker-compose.yml" ]; then
    echo "❌ docker-compose.yml not found"
    exit 1
fi

echo "✅ docker-compose.yml found"

# Stop any existing containers
echo "🛑 Stopping existing containers..."
docker-compose down

# Build and start containers
echo "🚀 Building and starting containers..."
docker-compose up --build -d

# Wait for containers to start
echo "⏳ Waiting for containers to start..."
sleep 30

# Check container status
echo "📦 Container Status:"
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo "🔍 Detailed Container Info:"
docker-compose ps

echo ""
echo "📊 Resource Usage:"
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}"

echo ""
echo "🏥 Health Checks:"

# Test MySQL
echo -n "MySQL: "
if docker exec complaint-mysql mysqladmin ping -h localhost --silent 2>/dev/null; then
    echo "✅ Healthy"
else
    echo "❌ Unhealthy"
fi

# Test Backend
echo -n "Backend: "
if curl -s http://localhost:8080 > /dev/null 2>&1; then
    echo "✅ Accessible"
else
    echo "❌ Not accessible"
fi

# Test Frontend
echo -n "Frontend: "
if curl -s http://localhost > /dev/null 2>&1; then
    echo "✅ Accessible"
else
    echo "❌ Not accessible"
fi

echo ""
echo "🌐 Access URLs:"
echo "Frontend: http://localhost"
echo "Backend:  http://localhost:8080"

echo ""
echo "📋 Test Commands:"
echo "View all logs:     docker-compose logs"
echo "View specific log: docker-compose logs [mysql|backend|frontend]"
echo "Stop containers:   docker-compose down"