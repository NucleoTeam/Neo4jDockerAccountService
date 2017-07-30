pipeline {
  agent any
  stages {
    stage('BeginProcess') {
      steps {
        parallel(
          "BeginProcess": {
            echo 'Building Neo4jAccountLibrary'
            
          },
          "Delete old build": {
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
              description: 'New release has been scheduled on project'+BUILD_TAG+' '+RUN_DISPLAY_URL,
              issuetype: [id: '10002'],
              tag: BUILD_TAG
            ]
          ]
          jiraNewIssue(issue: releaseIssue, site: 'SynloadJira')
        }
        
      }
    }
  }
}