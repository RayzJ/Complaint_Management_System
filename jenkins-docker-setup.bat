@echo off
echo ============================================
echo 🚀 Setting up Jenkins with Docker support
echo ============================================

echo [1/4] Stopping existing Jenkins container...
docker stop jenkins-server 2>nul
docker rm jenkins-server 2>nul

echo [2/4] Starting Jenkins with Docker support...
docker run -d ^
  --name jenkins-server ^
  -p 8080:8080 ^
  -p 50000:50000 ^
  -v jenkins_home:/var/jenkins_home ^
  -v /var/run/docker.sock:/var/run/docker.sock ^
  -v "%USERPROFILE%\.docker:/root/.docker" ^
  --group-add 0 ^
  jenkins/jenkins:lts

echo [3/4] Installing Docker in Jenkins container...
timeout /t 10
docker exec -u root jenkins-server sh -c "apt-get update && apt-get install -y docker.io"

echo [4/4] Getting Jenkins initial password...
timeout /t 20
docker exec jenkins-server cat /var/jenkins_home/secrets/initialAdminPassword

echo ============================================
echo ✅ Jenkins with Docker support is ready!
echo Jenkins URL: http://localhost:8080
echo ============================================
pause