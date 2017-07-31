pipeline {
  agent any
  stages {
    stage('BeginProcess') {
      steps {
        parallel(
          "BeginProcess": {
            echo 'Building Neo4jAccountLibrary'
            script {
              echo "Started the pipeline for ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
              slackSend color: 'good', message: "Started the pipeline for ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
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
          echo "Compiling Spring application for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
          slackSend color: 'good', message: "Compiling Spring application for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
        }
        
        sh 'chmod 0755 ./gradlew;./gradlew clean build --refresh-dependencies'
      }
    }
    stage('Docker Build') {
      steps {
        parallel(
          "Build Docker Image": {
            script {
              echo "Building Docker image for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
              slackSend color: 'good', message: "Building Docker image for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
            }
            
            sh '''mkdir dockerbuild/
cp build/libs/*.jar dockerbuild/app.jar
cp Dockerfile dockerbuild/Dockerfile
cd dockerbuild/
docker build -t nucleoteam/neo4jdockeraccountservice:latest ./'''
            
          },
          "Save Artifact": {
            script {
              echo "Archived artifacts for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
              slackSend color: 'good', message: "Archived artifacts for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
            }
            
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
          
          echo "Waiting for approval for ${env.JOB_NAME} ${env.BUILD_NUMBER} (<"+newIssue.data.self+"|Jira Ticket>) [60s cycle time]"
          slackSend color: 'good', message: "Waiting for approval for ${env.JOB_NAME} ${env.BUILD_NUMBER} (<"+newIssue.data.self+"|Jira Ticket>) [60s cycle time]"
        }
        
      }
    }
    stage('Wait For Approval') {
      steps {
        script {
          def keepGoing = true
          while(keepGoing ){
            sleep 60
            def issues = jiraJqlSearch jql: 'summary ~ '+BUILD_TAG, site: 'SynloadJira', failOnError: true
            if(issues.data.total==1){
              echo issues.data.issues[0].fields.status.name
              if(issues.data.issues[0].fields.status.name.equalsIgnoreCase("APPROVED")){
                keepGoing = false
                echo "Build accepted for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                slackSend color: 'good', message: "Build accepted for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
              }
              if(issues.data.issues[0].fields.status.name.equalsIgnoreCase("REJECTED") || issues.data.issues[0].fields.status.name.equalsIgnoreCase("DONE")){
                echo "Build rejected for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                slackSend color: 'good', message: "Build rejected for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
                sh 'exit -1'
              }
            }
            if(keepGoing == true){
              echo "Waiting for approval for ${env.JOB_NAME} ${env.BUILD_NUMBER} [60s cycle time]"
              slackSend color: 'good', message: "Waiting for approval for ${env.JOB_NAME} ${env.BUILD_NUMBER} [60s cycle time]"
            }
          }
        }
        
      }
    }
    stage('Set Done') {
      steps {
        parallel(
          "Set Done": {
            script {
              def issues = jiraJqlSearch jql: 'summary ~ '+BUILD_TAG, site: 'SynloadJira', failOnError: true
              if(issues.data.total==1){
                
                
                def response = jiraAddComment idOrKey: issues.data.issues[0].id, comment: 'Uploading '+JOB_NAME+' Build '+BUILD_DISPLAY_NAME, site: 'SynloadJira'
                echo response.toString()
                echo "Comment to release ticket for ${env.JOB_NAME} ${env.BUILD_NUMBER} (<"+response.data.self+"|Jira Comment>)"
                slackSend color: 'good', message: "Comment to release ticket for ${env.JOB_NAME} ${env.BUILD_NUMBER} (<"+response.data.self+"|Jira Comment>)"
                
              }
            }
            
            
          },
          "QA Release": {
            script {
              def releaseIssue = [
                fields: [
                  project: [ id: '10000' ],
                  summary: BUILD_TAG,
                  description: 'New release has been scheduled on project '+RUN_DISPLAY_URL,
                  issuetype: [id: '10003']
                ]
              ]
              def newIssue = jiraNewIssue(issue: releaseIssue, site: 'SynloadJira')
              
              echo newIssue.data.toString()
              echo "QA ticket for ${env.JOB_NAME} ${env.BUILD_NUMBER} created (<"+newIssue.data.self+"|Jira Ticket>)"
              slackSend color: 'good', message: "QA ticket for ${env.JOB_NAME} ${env.BUILD_NUMBER} created (<"+newIssue.data.self+"|Jira Ticket>)"
            }
            
            
          }
        )
      }
    }
    stage('Publish Latest Image') {
      steps {
        script {
          echo "Docker publishing to DockerHub for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
          slackSend color: 'good', message: "Docker publishing to DockerHub for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
        }
        
        sh 'docker push nucleoteam/neo4jdockeraccountservice:latest'
        script {
          echo "Docker image published to DockerHub for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
          slackSend color: 'good', message: "Docker image published to DockerHub for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
        }
        
      }
    }
    stage('Deploy') {
      steps {
        rancher(environmentId: '1a5', ports: '8000:8080', environments: '1i180', confirm: true, image: 'nucleoteam/neo4jdockeraccountservice:latest', service: 'testapp/AccountManager', endpoint: 'http://212.47.248.38:8080/v2-beta', credentialId: 'rancher-server')
        script {
          echo "Deployed docker image to Rancher for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
          slackSend color: 'good', message: "Deployed docker image to Rancher for ${env.JOB_NAME} ${env.BUILD_NUMBER}"
        }
        
      }
    }
  }
}