pipeline {
    environment {
        JAVA_TOOL_OPTIONS = '-Duser.home=/root'
    }

    agent {
        docker {
            image "maven:3.8.1-adoptopenjdk-11"
            args '-v /var/maven_data:/root'
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