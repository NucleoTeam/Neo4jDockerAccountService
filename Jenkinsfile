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

    }
}
