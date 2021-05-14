pipeline {
    agent {
        docker {
            image "maven:3.8.1-adoptopenjdk-11"
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        VERSION = readMavenPom().getVersion()
        def REMOTE = [:]
        NAME = "jenkins"
        HOST = "51.159.152.230"
        REMOTE.allowAnyHosts = true
    }

    stages {
        stage("Building") {
            steps {
                sh "mvn -B -DskipTests clean install"
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
            steps {
                script {
                    withCredentials([sshUserPrivateKey(credentialsId: 'e48b15ad-0f5e-4f07-8706-635c5250fa29', keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'jenkins')]) {
                      REMOTE.user = jenkins
                      REMOTE.identityFile = identity

                      sshCommand remote: REMOTE, command: 'cd /home/jenkins/Winston-Bot; ./kill_winston.sh'
                      sshCommand remote: REMOTE, command: 'rm /home/jenkins/Winston-Bot/*.jar', failOnError:'false'
                      sshPut remote: REMOTE, from: "target/Winston-Bot-${VERSION}-jar-with-dependencies.jar", into: '/home/jenkins/Winston-Bot/'
                      sshCommand remote: REMOTE, command: 'cd /home/jenkins/Winston-Bot; ./deploy.sh'
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}