#!/bin/bash
set -e

echo "============================================"
echo "🚀 Installing Docker & Docker Compose (v2)"
echo "============================================"

# Step 1: Update system
echo "[1/6] Updating system packages..."
sudo dnf update -y

# Step 2: Install Docker engine
echo "[2/6] Installing Docker engine..."
sudo dnf install -y docker

# Step 3: Enable and start Docker service
echo "[3/6] Enabling and starting Docker service..."
sudo systemctl enable docker
sudo systemctl start docker

# Step 4: Add current user to docker group
echo "[4/6] Adding user '$USER' to docker group..."
sudo usermod -aG docker $USER

# Step 5: Install Docker Compose (v2)
COMPOSE_VERSION="v2.29.7"
echo "[5/6] Installing Docker Compose $COMPOSE_VERSION..."
sudo curl -L "https://github.com/docker/compose/releases/download/${COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Step 6: Verify installation
echo "[6/6] Verifying installation..."
docker --version
docker-compose version

echo "✅ Docker and Docker Compose installed successfully!"
echo "👉 Log out and back in (or run 'newgrp docker') to use Docker without sudo."
echo "============================================"