pipeline {
    agent {
        docker {
            image "maven:3.8.1-adoptopenjdk-11"
            args '-v /root/.m2:/root/.m2'
        }
    }

    stages {
        stage("Building Winston-Bot...") {
            steps {
                sh "mvn -B -DskipTests clean package"
            }
        }
        stage("Testing Winston-Bot...") {
            steps {
                sh "mvn test"
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
            cleanWs()
        }
    }
}