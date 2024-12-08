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
        DOCKER_REGISTRY = 'anwarbel'
        APP_NAME = 'bro-app'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
        dockerImage = ''
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

                 dockerImage =    docker.build("${DOCKER_REGISTRY}/${APP_NAME}:${env.BUILD_NUMBER}")
                }
            }
        }
        stage('verify tool docker '){
            steps{
                sh 'docker version'

            }
        }

       stage('Push to Docker Registry') {
           steps {
              script {
                 docker.withRegistry( '', DOCKER_CREDENTIALS_ID ) {
                 dockerImage.push()
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