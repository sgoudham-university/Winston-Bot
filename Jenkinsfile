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
                echo "Testing Code..."
                sh "mvn test"
                echo "Generating Test Report..."
                jacoco(execPattern: 'target/*.exec', classPattern: 'target/classes', sourcePattern: 'src/main/java', exclusionPattern: 'src/test*')
            }
        }
        stage("Deploying") {
            when {
                branch 'release'
            }
            steps {
                echo "Deploying Winston Bot..."
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}