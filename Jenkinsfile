pipeline {
  agent any
  stages {
    stage('BeginProcess') {
      steps {
        echo 'Building Neo4jAccountLibrary'
        sh './gradlew clean build buildDocker'
      }
    }
  }
}