pipeline {
    agent {
        docker {
            image "maven:3.8.1-adoptopenjdk-11"
            args '-v /root/.m2:/root/.m2'
        }
    }

    stages {
        stage("Building") {
            steps {
                sh "mvn -B -DskipTests clean package"
            }
        }
        stage("Testing") {
            steps {
                sh "mvn test"
            }
        }
        stage("Deploying") {
            steps {
                echo "Deploying Winston Bot..."
            }
        }
    }

    post {
        always {
            jacoco(execPattern: 'target/jacoco.exec')
            junit 'target/surefire-reports/*.xml'
            cleanWs()
        }
    }
}