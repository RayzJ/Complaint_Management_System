#!/bin/bash
set -e

echo "============================================"
echo "🚀 Setting up Jenkins Server on EC2"
echo "============================================"

# Step 1: Update system
echo "[1/8] Updating system packages..."
sudo yum update -y

# Step 2: Install Java 11 (required for Jenkins)
echo "[2/8] Installing Java 11..."
sudo yum install java-11-openjdk java-11-openjdk-devel -y

# Step 3: Add Jenkins repository
echo "[3/8] Adding Jenkins repository..."
sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key

# Step 4: Install Jenkins
echo "[4/8] Installing Jenkins..."
sudo yum install jenkins -y

# Step 5: Start and enable Jenkins
echo "[5/8] Starting Jenkins service..."
sudo systemctl start jenkins
sudo systemctl enable jenkins

# Step 6: Install Docker (for Jenkins to build containers)
echo "[6/8] Installing Docker..."
sudo yum install docker -y
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker jenkins
sudo usermod -aG docker ec2-user

# Step 7: Install Docker Compose
echo "[7/8] Installing Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Step 8: Configure firewall
echo "[8/8] Configuring firewall..."
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --permanent --add-port=80/tcp
sudo firewall-cmd --permanent --add-port=81/tcp
sudo firewall-cmd --reload 2>/dev/null || echo "Firewall not running, skipping..."

# Get Jenkins initial password
echo "============================================"
echo "✅ Jenkins installation completed!"
echo "============================================"
echo "Jenkins URL: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8080"
echo ""
echo "Initial Admin Password:"
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
echo ""
echo "============================================"
echo "Next Steps:"
echo "1. Open Jenkins URL in browser"
echo "2. Use the password above to unlock Jenkins"
echo "3. Install suggested plugins"
echo "4. Create admin user"
echo "5. Configure your pipeline"
echo "============================================"