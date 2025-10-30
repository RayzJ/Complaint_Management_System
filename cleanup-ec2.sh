#!/bin/bash

echo "🧹 Cleaning up EC2 Docker space..."

# Stop all containers
docker-compose down -v

# Remove all containers
docker container prune -f

# Remove all images
docker image prune -a -f

# Remove all volumes
docker volume prune -f

# Remove all networks
docker network prune -f

# Remove build cache
docker builder prune -a -f

# Clean system
docker system prune -a -f

# Check disk space
echo "📊 Disk space after cleanup:"
df -h

echo "✅ Cleanup complete!"