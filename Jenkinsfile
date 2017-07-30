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
        sh 'chmod 0755 ./gradlew;./gradlew clean build --refresh-dependencies'
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
              summary: BUILD_TAG,
              description: 'New release has been scheduled on project '+RUN_DISPLAY_URL,
              issuetype: [id: '10002']
            ]
          ]
          jiraNewIssue(issue: releaseIssue, site: 'SynloadJira')
        }
        
      }
    }
    stage('CheckJira') {
      steps {
        script {
          def issues = jiraJqlSearch jql: 'summary ~ '+BUILD_TAG, site: 'SynloadJira', failOnError: true
          
          
          echo issues.toString()
        }
        
      }
    }
  }
}