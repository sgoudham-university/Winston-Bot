pipeline {
    agent {
        docker {
            image "maven:3.8.1-adoptopenjdk-11"
            label "docker"
        }
    }

    stages {
        stage("Build") {
            steps {
                echo "Building..."
                sh "mvn -version"
                sh "mvn clean install"
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }

}