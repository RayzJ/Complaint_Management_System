#!/bin/bash

# DevInTen CMS Deployment Script
echo "🚀 Starting DevInTen CMS Deployment..."

# Set variables
PLAYBOOK_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
IMAGE_TAG=${1:-"v7"}  # Default to v7 if no tag provided

echo "📦 Deploying with image tag: $IMAGE_TAG"

# Update image tag in variables
sed -i "s/image_tag: .*/image_tag: \"$IMAGE_TAG\"/" "$PLAYBOOK_DIR/group_vars/all.yml"

# Run Ansible playbook
cd "$PLAYBOOK_DIR"
ansible-playbook -i inventory/hosts.yml playbooks/deploy.yml -v

if [ $? -eq 0 ]; then
    echo "✅ Deployment completed successfully!"
    echo "🌐 Access your application at: http://YOUR_EC2_IP"
    echo "🔧 Backend API available at: http://YOUR_EC2_IP:8080"
else
    echo "❌ Deployment failed!"
    exit 1
fi