def remote = [:]
remote.name = "jenkins"
remote.host = "51.159.152.230"
remote.allowAnyHosts = true

pipeline {
    agent {
        docker {
            image "maven:3.8.1-adoptopenjdk-11"
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        VERSION = readMavenPom().getVersion()
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
                echo "${VERSION}"
                script {
                    withCredentials([sshUserPrivateKey(credentialsId: 'e48b15ad-0f5e-4f07-8706-635c5250fa29', keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'jenkins')]) {
                      remote.user = jenkins
                      remote.identityFile = identity

                      sshCommand remote: remote, command: 'cd Winston-Bot/; ./kill_winston.sh'
                      sshCommand remote: remote, command: 'rm Winston-Bot/*.jar', failOnError:'false'
                      sshCommand remote: remote, command: 'rm -rf Winston-Bot/src', failOnError:'false'
                      sshPut remote: remote, from: "target/Winston-Bot-${VERSION}-jar-with-dependencies.jar", into: 'Winston-Bot/'
                      sshPut remote: remote, from: "src", into: 'Winston-Bot/'
                      sshCommand remote: remote, command: "cd Winston-Bot/; nohup java -jar Winston-Bot-${VERSION}-jar-with-dependencies.jar >> winston.log &"
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