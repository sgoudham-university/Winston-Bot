pipeline {
    agent {
        docker {
            image "maven:3.8.1-adoptopenjdk-11"
            args '-v /root/.m2:/root/.m2'
        }
    }

    def remote = [:]
    def pomVersion = readMavenPom().version
    remote.name = "jenkins"
    remote.host = "51.159.152.230"
    remote.allowAnyHosts = true

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
            withCredentials([sshUserPrivateKey(credentialsId: 'e48b15ad-0f5e-4f07-8706-635c5250fa29', keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'jenkins')]) {
                  remote.user = jenkins
                  remote.identityFile = identity

                  sshCommand remote: remote, command: 'cd /home/jenkins/Winston-Bot; ./kill_winston.sh'
                  sshCommand remote: remote, command: 'rm /home/jenkins/Winston-Bot/*.jar', failOnError:'false'
                  sshPut remote: remote, from: "target/Winston-Bot-${pomVersion}-jar-with-dependencies.jar", into: '/home/jenkins/Winston-Bot/'
                  sshCommand remote: remote, command: 'cd /home/jenkins/Winston-Bot; ./deploy.sh'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}