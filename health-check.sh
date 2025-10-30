#!/bin/bash

# Health Check Script for Complaint Management System
echo "🏥 Health Check - Complaint Management System"
echo "=============================================="

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Function to check service health
check_service() {
    local service_name=$1
    local url=$2
    local expected_status=$3
    
    echo -n "Checking $service_name... "
    
    if curl -s -o /dev/null -w "%{http_code}" "$url" | grep -q "$expected_status"; then
        echo -e "${GREEN}✅ HEALTHY${NC}"
        return 0
    else
        echo -e "${RED}❌ UNHEALTHY${NC}"
        return 1
    fi
}

# Check Docker containers
echo "📦 Container Status:"
docker-compose ps

echo ""
echo "🔍 Service Health Checks:"

# Check MySQL
echo -n "MySQL Database... "
if docker exec complaint-mysql mysqladmin ping -h localhost --silent; then
    echo -e "${GREEN}✅ HEALTHY${NC}"
else
    echo -e "${RED}❌ UNHEALTHY${NC}"
fi

# Check Backend
check_service "Backend API" "http://localhost:8080" "200\|404"

# Check Frontend
check_service "Frontend" "http://localhost" "200"

echo ""
echo "📊 Resource Usage:"
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}"

echo ""
echo "💾 Disk Usage:"
df -h | grep -E "Filesystem|/dev/"

echo ""
echo "🔗 Access URLs:"
PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4 2>/dev/null || echo "localhost")
echo "Frontend: http://$PUBLIC_IP"
echo "Backend:  http://$PUBLIC_IP:8080"

echo ""
echo "📋 Quick Commands:"
echo "View logs:     docker-compose logs -f [service]"
echo "Restart:       docker-compose restart [service]"
echo "Scale:         docker-compose up -d --scale backend=2"
echo "Update:        ./deploy.sh"