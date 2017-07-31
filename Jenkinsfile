pipeline {
  agent any
  stages {
    stage('BeginProcess') {
      steps {
        parallel(
          "BeginProcess": {
            echo 'Building Neo4jAccountLibrary'
            script {
              echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Started the pipeline (<${env.BUILD_URL}|Open>)"
              slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Started the pipeline (<${env.BUILD_URL}|Open>)"
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
          echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Compiling Spring application"
          slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Compiling Spring application"
        }
        
        sh 'chmod 0755 ./gradlew;./gradlew clean build --refresh-dependencies'
        script {
          echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Compiled Spring application"
          slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Compiled Spring application"
        }
      }
    }
    stage('Docker Build') {
      steps {
        parallel(
          "Build Docker Image": {
            script {
              echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Building Docker image"
              slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Building Docker image"
            }
            
            sh '''mkdir dockerbuild/
cp build/libs/*.jar dockerbuild/app.jar
cp Dockerfile dockerbuild/Dockerfile
cd dockerbuild/
docker build -t nucleoteam/neo4jdockeraccountservice:latest ./'''
            script {
              echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Built Docker image"
              slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Built Docker image"
            }
          },
          "Save Artifact": {
            script {
              echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Archived artifacts"
              slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Archived artifacts"
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
          
          echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Waiting for approval (<"+newIssue.data.self+"|Jira Ticket>) [60s cycle time]"
          slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Waiting for approval (<"+newIssue.data.self+"|Jira Ticket>) [60s cycle time]"
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
                echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Build accepted"
                slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Build accepted"
              }
              if(issues.data.issues[0].fields.status.name.equalsIgnoreCase("REJECTED") || issues.data.issues[0].fields.status.name.equalsIgnoreCase("DONE")){
                echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Build rejected"
                slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Build rejected"
                sh 'exit -1'
              }
            }
            if(keepGoing == true){
              echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Waiting for approval [60s cycle time]"
              slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Waiting for approval [60s cycle time]"
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
                
                echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Comment to release ticket (<"+response.data.self+"|Jira Comment>)"
                slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Comment to release ticket (<"+response.data.self+"|Jira Comment>)"
                
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
              
              echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] QA ticket created (<"+newIssue.data.self+"|Jira Ticket>)"
              slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] QA ticket created (<"+newIssue.data.self+"|Jira Ticket>)"
            }
            
            
          }
        )
      }
    }
    stage('Publish Latest Image') {
      steps {
        
        script {
          echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Docker publishing to DockerHub"
          slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Docker publishing to DockerHub"
        }
        
        sh 'docker push nucleoteam/neo4jdockeraccountservice:latest'
        
        script {
          echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Docker image published to DockerHub"
          slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Docker image published to DockerHub"
        }
        
      }
    }
    stage('Deploy') {
      steps {
        
        script {
          echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Deploying docker image to Rancher"
          slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Deploying docker image to Rancher"
        }
        
        rancher(environmentId: '1a5', ports: '8000:8080', environments: '1i180', confirm: true, image: 'nucleoteam/neo4jdockeraccountservice:latest', service: 'testapp/AccountManager', endpoint: 'http://212.47.248.38:8080/v2-beta', credentialId: 'rancher-server')

        script {
          echo "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Deployed docker image to Rancher"
          slackSend color: 'good', message: "[${env.JOB_NAME} ${env.BUILD_NUMBER}] Deployed docker image to Rancher"
        }
        
      }
    }
  }
}
