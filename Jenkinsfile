pipeline {
  agent any
  stages {
    stage('BeginProcess') {
      steps {
        echo 'Building Neo4jAccountLibrary'
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
            sh '''mkdir dockerbuild/
cp build/libs/*.jar dockerbuild/app.jar 
cp Dockerfile dockerbuild/Dockerfile
cd dockerbuild/
docker build'''
            
          },
          "Save Artifact": {
            archiveArtifacts 'build/libs/*.jar'
            
          }
        )
      }
    }
  }
}