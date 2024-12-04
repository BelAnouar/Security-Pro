pipeline{
   agent any

   parameters {
           string(name: 'GITHUB_SHA', defaultValue: '', description: 'GitHub Commit SHA')
           string(name: 'GITHUB_REF', defaultValue: '', description: 'GitHub Branch/Ref')
       }
   stages{
    stage('Checkout') {
               steps {

                   checkout scm: [
                       $class: 'GitSCM',
                       branches: [[name: params.GITHUB_SHA]],
                       userRemoteConfigs: [[url: 'https://github.com/BelAnouar/Security-Pro.git']]
                   ]
               }
           }
       stage('Build'){
            steps{

            }
       }
       stage('test'){
                   steps{

                   }
              }
       stage('deploy'){
                   steps{

                   }
       }
   }
}