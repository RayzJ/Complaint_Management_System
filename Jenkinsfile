pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9'
        nodejs 'NodeJS-18'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-username/complaint-management-system.git'
            }
        }
        
        stage('Build Backend') {
            steps {
                dir('demo') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Build Frontend') {
            steps {
                dir('complaint-management-frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }
        
        stage('Build Docker Images') {
            steps {
                script {
                    sh 'docker build -t complaint-backend:${BUILD_NUMBER} ./demo'
                    sh 'docker build -t complaint-frontend:${BUILD_NUMBER} ./complaint-management-frontend'
                }
            }
        }
        
        stage('Deploy to EC2') {
            steps {
                script {
                    sh '''
                        ansible-playbook -i inventory/hosts deploy.yml \
                        --extra-vars "build_number=${BUILD_NUMBER}"
                    '''
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
    }
}