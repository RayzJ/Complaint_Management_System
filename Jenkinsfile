pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'your-docker-registry'
        APP_NAME = 'complaint-management-system'
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }
        
        stage('Build Backend') {
            steps {
                echo 'Building Spring Boot backend...'
                script {
                    dir('demo') {
                        sh 'docker build -t complaint-backend:${BUILD_NUMBER} .'
                        sh 'docker tag complaint-backend:${BUILD_NUMBER} complaint-backend:latest'
                    }
                }
            }
        }
        
        stage('Build Frontend') {
            steps {
                echo 'Building Angular frontend...'
                script {
                    dir('complaint-management-frontend') {
                        sh 'docker build -t complaint-frontend:${BUILD_NUMBER} .'
                        sh 'docker tag complaint-frontend:${BUILD_NUMBER} complaint-frontend:latest'
                    }
                }
            }
        }
        
        stage('Test') {
            parallel {
                stage('Backend Tests') {
                    steps {
                        echo 'Running backend tests...'
                        script {
                            dir('demo') {
                                sh 'mvn test'
                            }
                        }
                    }
                }
                stage('Frontend Tests') {
                    steps {
                        echo 'Running frontend tests...'
                        script {
                            dir('complaint-management-frontend') {
                                sh 'npm test -- --watch=false --browsers=ChromeHeadless'
                            }
                        }
                    }
                }
            }
        }
        
        stage('Deploy to Staging') {
            steps {
                echo 'Deploying to staging environment...'
                sh 'docker-compose -f docker-compose.staging.yml up -d'
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'master'
            }
            steps {
                echo 'Deploying to production environment...'
                sh 'docker-compose up -d'
                
                // Health check
                script {
                    sleep(30)
                    sh 'curl -f http://localhost:80 || exit 1'
                    sh 'curl -f http://localhost:8080/actuator/health || exit 1'
                }
            }
        }
    }
    
    post {
        always {
            echo 'Cleaning up...'
            sh 'docker system prune -f'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}