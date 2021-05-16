pipeline {
    agent {
        docker {
            image "maven:3.8.1-adoptopenjdk-11"
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        VERSION = readMavenPom().getVersion()
        CODECOV_TOKEN = credentials('6cbb6aea-0634-4082-b547-6d6a8802cf8c')
    }

    stages {
        stage("Building") {
            steps {
                sh "mvn -B -DskipTests clean install"
            }
        }
        stage("Testing") {
            steps {
                sh "mvn test"
            }
        }
        stage("Deploying") {
            steps {
                script {
                    def remote = [name: 'jenkins', host: '51.159.152.230', allowAnyHosts: true]
                    withCredentials([sshUserPrivateKey(credentialsId: 'e48b15ad-0f5e-4f07-8706-635c5250fa29', keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'jenkins')]) {
                      remote.user = jenkins
                      remote.identityFile = identity

                      sshCommand remote: remote, command: "sudo systemctl stop winston.service"
                      sshCommand remote: remote, command: 'rm Winston-Bot/*.jar', failOnError:'false'
                      sshCommand remote: remote, command: 'rm -rf Winston-Bot/src', failOnError:'false'
                      sshPut remote: remote, from: "target/Winston-Bot-${VERSION}-jar-with-dependencies.jar", into: "Winston-Bot/"
                      sshPut remote: remote, from: "src", into: "Winston-Bot/"
                      sshCommand remote: remote, command: "echo VERSION=${VERSION} > Winston-Bot/version.txt"
                      sshCommand remote: remote, command: "sudo systemctl start winston.service"
                    }
                }
            }
        }
    }

    post {
        success {
            echo "I'm Feeling Powerful!"

            echo "Generating Test Report..."
            publishCoverage adapters: [jacocoAdapter('target/site/jacoco/jacoco.xml')]

            echo "Sending Report to CodeCov..."
            sh '''#!/bin/bash
                  bash <(curl -s https://codecov.io/bash) -t $CODECOV_TOKEN || echo "Codecov did not collect coverage reports"
               '''
        }
        failure {
            echo 'How Embarrassing!'
        }
        always {
            cleanWs()
        }
    }
}