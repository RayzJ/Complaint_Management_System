@echo off
echo 🧪 Testing Docker Compose Setup Locally
echo ========================================

REM Check if Docker is running
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker is not running. Please start Docker Desktop.
    pause
    exit /b 1
)

echo ✅ Docker is running

REM Check if docker-compose file exists
if not exist "docker-compose.yml" (
    echo ❌ docker-compose.yml not found
    pause
    exit /b 1
)

echo ✅ docker-compose.yml found

REM Stop any existing containers
echo 🛑 Stopping existing containers...
docker-compose down

REM Build and start containers
echo 🚀 Building and starting containers...
docker-compose up --build -d

REM Wait for containers to start
echo ⏳ Waiting for containers to start...
timeout /t 30 /nobreak >nul

REM Check container status
echo.
echo 📦 Container Status:
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo.
echo 🔍 Detailed Container Info:
docker-compose ps

echo.
echo 📊 Resource Usage:
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}"

echo.
echo 🏥 Health Checks:

REM Test MySQL
echo | set /p="MySQL: "
docker exec complaint-mysql mysqladmin ping -h localhost --silent >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Healthy
) else (
    echo ❌ Unhealthy
)

REM Test Backend
echo | set /p="Backend: "
curl -s http://localhost:8080 >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Accessible
) else (
    echo ❌ Not accessible
)

REM Test Frontend
echo | set /p="Frontend: "
curl -s http://localhost >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Accessible
) else (
    echo ❌ Not accessible
)

echo.
echo 🌐 Access URLs:
echo Frontend: http://localhost
echo Backend:  http://localhost:8080

echo.
echo 📋 Test Commands:
echo View all logs:     docker-compose logs
echo View specific log: docker-compose logs [mysql^|backend^|frontend]
echo Stop containers:   docker-compose down

echo.
pause