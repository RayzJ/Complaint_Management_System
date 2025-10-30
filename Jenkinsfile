pipeline {
    agent any

    environment {
        DOCKERHUB_USER = 'riyaz05042004'   // 🔹 correct Docker Hub username
        BACKEND_IMAGE = 'complaint-backend'
        FRONTEND_IMAGE = 'complaint-frontend'
        DB_IMAGE = 'complaint-database'
        IMAGE_TAG = "v${BUILD_NUMBER}"               // 🔹 auto-tag from Jenkins build number
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "📦 Cloning repository (master branch)..."
                git branch: 'master', url: 'https://github.com/RayzJ/Complaint_Management_System.git'
            }
        }

        stage('Build Backend Image') {
            steps {
                echo "⚙️ Building Backend Docker Image (Tag: ${IMAGE_TAG})..."
                dir('demo') {
                    sh "docker build -t ${DOCKERHUB_USER}/cms:${BACKEND_IMAGE}-${IMAGE_TAG} ."
                }
            }
        }

        stage('Build database Image') {
            steps {
                echo "⚙️ Building database Docker Image (Tag: ${IMAGE_TAG})..."
                dir('database') {
                    sh "docker build -t ${DOCKERHUB_USER}/cms:${DB_IMAGE}-${IMAGE_TAG} ."
                }
            }
        }

        stage('Build Frontend Image') {
            steps {
                echo "⚙️ Building Frontend Docker Image (Tag: ${IMAGE_TAG})..."
                dir('complaint-management-frontend') {
                    sh "docker build -t ${DOCKERHUB_USER}/cms:${FRONTEND_IMAGE}-${IMAGE_TAG} ."
                }
            }
        }

        stage('Login to Docker Hub') {
            steps {
                echo "🔐 Logging into Docker Hub..."
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                        echo "Logging in as: $DOCKER_USER"
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                    '''
                }
            }
        }

        stage('Push Images to Docker Hub') {
            steps {
                echo "📤 Pushing Docker Images with tag ${IMAGE_TAG}..."
                sh """
                docker push ${DOCKERHUB_USER}/cms:${BACKEND_IMAGE}-${IMAGE_TAG}
                docker push ${DOCKERHUB_USER}/cms:${DB_IMAGE}-${IMAGE_TAG}
                docker push ${DOCKERHUB_USER}/cms:${FRONTEND_IMAGE}-${IMAGE_TAG}
                """
            }
        }
    }

    post {
        success {
            echo "✅ Build ${BUILD_NUMBER} complete — Images pushed:"
            echo "   ${DOCKERHUB_USER}/cms:${BACKEND_IMAGE}-${IMAGE_TAG}"
            echo "   ${DOCKERHUB_USER}/cms:${FRONTEND_IMAGE}-${IMAGE_TAG}"
            echo "   ${DOCKERHUB_USER}/cms:${DB_IMAGE}-${IMAGE_TAG}"
        }
        failure {
            echo "❌ Pipeline failed during build ${BUILD_NUMBER}."
        }
    }
}
