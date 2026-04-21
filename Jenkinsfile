pipeline {
    agent any

    tools {
        maven 'maven'
    }

    environment {
        STACK_NAME = "ecommerce"
    }

    stages {

        // -----------------------------
        // CLONE REPO
        // -----------------------------
        stage('Checkout') {
            steps {
                git branch: 'main',
                url: 'https://github.com/chirag-2004/JenkinsCart.git'
            }
        }

        // -----------------------------
        // BUILD ALL SERVICES (MAVEN)
        // -----------------------------
        stage('Build Services') {
            steps {
                bat '''
                mvn clean package -DskipTests -f Ecomm_EurekaServer/pom.xml
                mvn clean package -DskipTests -f Ecomm_ConfigServer/pom.xml
                mvn clean package -DskipTests -f APIGateway/pom.xml

                mvn clean package -DskipTests -f Product/pom.xml
                mvn clean package -DskipTests -f ProductCatalog/pom.xml
                mvn clean package -DskipTests -f ProductInventory/pom.xml
                mvn clean package -DskipTests -f ProductPrice/pom.xml

                mvn clean package -DskipTests -f Cart/pom.xml
                mvn clean package -DskipTests -f Recommendation/pom.xml
                '''
            }
        }

        // -----------------------------
        // BUILD DOCKER IMAGES
        // (Names MUST match docker-stack.yml)
        // -----------------------------
        stage('Docker Build') {
            steps {
                bat '''
                docker build -t eureka-service ./Ecomm_EurekaServer
                docker build -t config-service ./Ecomm_ConfigServer
                docker build -t gateway-service ./APIGateway

                docker build -t product-service ./Product
                docker build -t catalog-service ./ProductCatalog
                docker build -t inventory-service ./ProductInventory
                docker build -t pricing-service ./ProductPrice

                docker build -t cart-service ./Cart
                docker build -t recommendation-service ./Recommendation
                '''
            }
        }

        // -----------------------------
        // INIT SWARM (SAFE)
        // -----------------------------
        stage('Init Swarm') {
            steps {
                bat '''
                docker swarm init || echo Swarm already initialized
                '''
            }
        }

        // -----------------------------
        // REMOVE OLD STACK
        // -----------------------------
        stage('Remove Old Stack') {
            steps {
                bat '''
                docker stack rm %STACK_NAME% || echo No stack running
                timeout /t 10
                '''
            }
        }

        // -----------------------------
        // DEPLOY STACK
        // -----------------------------
        stage('Deploy to Swarm') {
            steps {
                bat '''
                docker stack deploy -c docker-stack.yml %STACK_NAME%
                '''
            }
        }

        // -----------------------------
        // VERIFY
        // -----------------------------
        stage('Verify Deployment') {
            steps {
                bat '''
                docker service ls
                docker stack services %STACK_NAME%
                '''
            }
        }
    }

    post {
        success {
            echo "🚀 Ecommerce Cart Microservices deployed successfully on Docker Swarm!"
        }
        failure {
            echo "❌ Pipeline failed — check Jenkins logs"
        }
    }
}
