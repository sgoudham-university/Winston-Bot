pipeline {
    environment {
        JAVA_TOOL_OPTIONS = "-Duser.home=root/maven"
    }

    agent {
        docker {
            image "maven:3.8.1-adoptopenjdk-11"
            args '-v $HOME/.m2:/root/.m2 -e MAVEN_CONFIG=/root/maven/.m2'
        }
    }

    stages {
        stage("Build") {
            steps {
                echo "Building..."
                sh "pwd && cd .."
                sh "pwd && cd .."
                sh "pwd && cd .."
                sh "pwd && cd .."
                sh "pwd && ls -la"
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