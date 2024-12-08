pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK'
        dockerTool 'Docker'
    }

    parameters {
        string(name: 'GITHUB_SHA', defaultValue: '', description: 'GitHub Commit SHA')
        string(name: 'GITHUB_REF', defaultValue: '', description: 'GitHub Branch/Ref')
    }

    environment {
        PATH = "${tool 'Docker'}/bin:${env.PATH}"
        DOCKER_REGISTRY = 'anwarbel'
        APP_NAME = 'bro-app'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'

        DOCKER_USERNAME = credentials('DOCKER_USERNAME')
        DOCKER_PASSWORD = credentials('DOCKER_PASSWORD')

    }

    stages {
        stage('Build') {
            steps {
                script {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Unit Tests') {
            steps {
                script {
                    sh 'mvn test -Pprod'
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    jacoco execPattern: 'target/jacoco.exec'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_REGISTRY}/${APP_NAME}:${env.BUILD_NUMBER}")
                }
            }
        }

        stage('Verify Docker Tool') {
            steps {
                sh 'docker version'
            }
        }
        stage('Verify Docker') {
            steps {
                script {
                    sh 'which docker'
                    sh 'docker --version'
                }
            }
        }

       stage('Push to Docker Registry') {
           steps {
               script {
                   withCredentials([usernamePassword(
                       credentialsId: DOCKER_CREDENTIALS_ID,
                       usernameVariable: 'DOCKER_USERNAME',
                       passwordVariable: 'DOCKER_PASSWORD'
                   )]) {
                       sh """
                           docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}
                           docker push ${DOCKER_REGISTRY}/${APP_NAME}:${env.BUILD_NUMBER}
                       """
                   }
               }
           }
       }

        stage('Deploy to Staging') {
            steps {
                script {
                    sh '''
                        docker-compose down
                        docker-compose up -d
                    '''
                }
            }
        }

        stage('Integration Tests') {
            steps {
                script {
                    sh 'mvn verify -P integration-tests'
                }
            }
        }

        stage('Deploy to Production') {
            when {
                branch 'master'
            }
            steps {
                script {
                    sh '''
                        echo "Deploying to production"
                        # Add your production deployment commands
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Build and Deploy succeeded!'
        }
        failure {
            echo 'Build or Deploy failed!'
        }
    }
}
