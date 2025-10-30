@echo off
echo ============================================
echo 🚀 Setting up Jenkins on Windows with Docker
echo ============================================

echo [1/4] Starting Jenkins container...
docker-compose -f docker-compose-jenkins.yml up -d

echo [2/4] Waiting for Jenkins to start...
timeout /t 30

echo [3/4] Getting Jenkins initial password...
docker exec jenkins-server cat /var/jenkins_home/secrets/initialAdminPassword

echo [4/4] Jenkins setup completed!
echo ============================================
echo Jenkins URL: http://localhost:8080
echo ============================================
echo Next Steps:
echo 1. Open http://localhost:8080 in browser
echo 2. Use the password above to unlock Jenkins
echo 3. Install suggested plugins
echo 4. Create admin user
echo 5. Configure your pipeline
echo ============================================

pause