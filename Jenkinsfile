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
    stage('Approval Request') {
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
          def newIssue = jiraNewIssue(issue: releaseIssue, site: 'SynloadJira')
          
          echo newIssue.data.toString()
        }
        
      }
    }
    stage('Wait For Approval') {
      steps {
        script {
          def keepGoing = true
          while(keepGoing ){
            sleep 30
            def issues = jiraJqlSearch jql: 'summary ~ '+BUILD_TAG, site: 'SynloadJira', failOnError: true
            if(issues.data.total==1){
              echo issues.data.issues[0].fields.status.name
              if(issues.data.issues[0].fields.status.name.equalsIgnoreCase("APPROVED")){
                keepGoing = false
              }
              if(issues.data.issues[0].fields.status.name.equalsIgnoreCase("REJECTED")){
                sh 'exit -1'
              }
            }
          }
        }
        
      }
    }
    stage('Publish To DockerHub') {
      steps {
        sh 'docker push .'
      }
    }
  }
}