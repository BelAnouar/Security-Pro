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

       stage('Push to Docker Registry') {
           steps {
               script {
                   docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {

                       docker.image("anwarbel/spring-bro:${env.BUILD_NUMBER}").push()


                       docker.image("anwarbel/spring-bro:${env.BUILD_NUMBER}").push('latest')
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