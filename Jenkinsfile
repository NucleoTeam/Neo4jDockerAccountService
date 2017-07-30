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
        sh 'rm -rf $HOME/.gradle/caches/;chmod 0755 ./gradlew;./gradlew clean build --refresh-dependencies'
      }
    }
    stage('Docker Build') {
      steps {
        parallel(
          "Build Docker Image": {
            sh 'mkdir dockerbuild/;cp build/libs/*.jar dockerbuild/app.jar;cp Dockerfile dockerbuild/Dockerfile;cd dockerbuild/;docker build ./'
            
          },
          "Save Artifact": {
            archiveArtifacts(artifacts: 'build/libs/*.jar', onlyIfSuccessful: true)
            
          }
        )
      }
    }
    stage('JIRA') {
      steps {
        script {
          def releaseIssue = [
            fields: [
              project: [ id: '10000' ],
              summary: 'Approval For Release',
              description: 'New release has been scheduled on project ${BUILD_TAG}',
              issuetype: [id: '10002']
            ]
          ]
          response = jiraNewIssue issue: releaseIssue, site: 'SynloadJira'
          echo response.data.toString()
          echo response.successful.toString()
        }
        
      }
    }
  }
}