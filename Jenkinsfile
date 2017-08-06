pipeline {
  agent any
  stages {
    stage('BeginProcess') {
      steps {
        parallel(
          "BeginProcess": {
            echo 'Building Neo4jAccountLibrary'
            script {
              echo "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Started the pipeline (<${env.BUILD_URL}|Open>)"
              slackSend color: '#cecece', message: "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Started the pipeline (<${env.BUILD_URL}|Open>)"
            }
            
            
          },
          "Delete old build": {
            sh 'rm -rf dockerbuild/'
            
          }
        )
      }
    }
    stage('Build') {
      steps {
        script {
          echo "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Compiling Spring application"
          slackSend color: '#3e6be8', message: "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Compiling Spring application"
        }
        
        sh 'chmod 0755 ./gradlew;./gradlew clean build --refresh-dependencies'
        script {
          echo "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Compiled Spring application"
          slackSend color: '#42e565', message: "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Compiled Spring application"
        }
        
      }
    }
    stage('Docker Build') {
      steps {
        parallel(
          "Build Docker Image": {
            script {
              echo "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Building Docker image"
              slackSend color: '#3e6be8', message: "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Building Docker image"
            }
            
            sh '''mkdir dockerbuild/
cp build/libs/*.jar dockerbuild/app.jar
cp Dockerfile dockerbuild/Dockerfile
cd dockerbuild/
docker build -t nucleoteam/neo4jdockeraccountservice:latest ./'''
            script {
              echo "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Built Docker image"
              slackSend color: '#42e565', message: "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Built Docker image"
            }
            
            
          },
          "Save Artifact": {
            script {
              echo "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Archived artifacts"
              slackSend color: '#f1f430', message: "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Archived artifacts"
            }
            
            archiveArtifacts(artifacts: 'build/libs/*.jar', onlyIfSuccessful: true)
            
          }
        )
      }
    }
    stage('Publish Latest Image') {
      steps {
        script {
          echo "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Docker image publishing to DockerHub"
          slackSend color: '#3e6be8', message: "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Docker image publishing to DockerHub"
        }
        
        sh 'docker push nucleoteam/neo4jdockeraccountservice:latest'
        script {
          echo "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Docker image published to DockerHub"
          slackSend color: '#42e565', message: "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Docker image published to DockerHub"
        }
        
      }
    }
    stage('Deploy') {
      steps {
        script {
          echo "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Deploying docker image to Rancher"
          slackSend color: '#3e6be8', message: "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Deploying docker image to Rancher"
        }
        
        rancher(environmentId: '1a5', ports: '8000:8080', environments: '1i180', confirm: true, image: 'nucleoteam/neo4jdockeraccountservice:latest', service: 'testapp/AccountManager', endpoint: 'http://212.47.248.38:8080/v2-beta', credentialId: 'rancher-server')
        script {
          echo "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Deployed docker image to Rancher"
          slackSend color: '#42e565', message: "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Deployed docker image to Rancher"
        }
        
      }
    }
    stage('Finished') {
      steps {
        script {
          echo "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Finished pipeline"
          slackSend color: '#09a31e', message: "[${env.JOB_NAME} #${env.BUILD_NUMBER}] Finished pipeline"
        }
        
      }
    }
  }
}