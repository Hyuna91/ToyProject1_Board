pipeline {
  agent any
  stages {
    stage('Source') {
      steps {
        git(url: 'https://github.com/Hyuna91/ToyProject1_Board.git', branch: 'master')
      }
    }

    stage('Build') {
      steps {
        tool 'Maven'
      }
    }

    stage('Deploy') {
      steps {
        sh 'echo "Deploy Success"'
      }
    }

  }
}