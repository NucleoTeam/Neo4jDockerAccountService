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
  }
}