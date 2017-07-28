pipeline {
  agent any
  stages {
    stage('BeginProcess') {
      steps {
        parallel(
          "BeginProcess": {
            echo 'Building Neo4jAccountLibrary'
            
          },
          "error": {
            sh 'rm -rf dockerbuild/'
            
          }
        )
      }
    }
    stage('Build') {
      steps {
        sh 'chmod 0755 ./gradlew;./gradlew clean build'
      }
    }
    stage('error') {
      steps {
        parallel(
          "Build Docker Image": {
            sh './gradlew buildDocker'
            
          },
          "Save Artifact": {
            archiveArtifacts 'build/libs/*.jar'
            
          }
        )
      }
    }
  }
}