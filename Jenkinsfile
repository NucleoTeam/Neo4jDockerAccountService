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
            sh '''mkdir dockerbuild/
cp build/libs/*.jar dockerbuild/app.jar
cp Dockerfile dockerbuild/Dockerfile
cd dockerbuild/
docker build -t nucleoteam/neo4jdockeraccountservice:latest ./'''
            
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
              if(issues.data.issues[0].fields.status.name.equalsIgnoreCase("REJECTED") || issues.data.issues[0].fields.status.name.equalsIgnoreCase("DONE")){
                sh 'exit -1'
              }
            }
          }
        }
        
      }
    }
    stage('Set Done') {
      steps {
        script {
          def issues = jiraJqlSearch jql: 'summary ~ '+BUILD_TAG, site: 'SynloadJira', failOnError: true
          if(issues.data.total==1){
            
            
            def response = jiraAddComment idOrKey: issues.data.issues[0].id, comment: 'Uploading '+JOB_NAME+' Build '+BUILD_DISPLAY_NAME, site: 'SynloadJira'
            echo response.toString()
          }
        }
        
      }
    }
    stage('Publish Latest Image') {
      steps {
        sh 'docker push nucleoteam/neo4jdockeraccountservice:latest'
      }
    }
    stage('Deploy') {
      steps {
        rancher(environmentId: '1a5', ports: '8000:8080', environments: '1i180', confirm: true, image: 'nucleoteam/neo4jdockeraccountservice:latest', service: 'testapp/AccountManager', endpoint: 'http://212.47.248.38:8080/v2-beta', credentialId: 'rancher-server')
      }
    }
  }
}