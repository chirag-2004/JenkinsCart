pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        STACK_NAME = "ecommerce"
        DOCKER_USER = "chirag1804"
    }

    stages {

        // -----------------------------
        // CHECKOUT
        // -----------------------------
        stage('Checkout') {
            steps {
                git branch: 'main',
                url: 'https://github.com/chirag-2004/JenkinsCart.git'
            }
        }

        // -----------------------------
        // BUILD SERVICES
        // -----------------------------
        stage('Build Services') {
            steps {
                bat '''
                mvn clean package -DskipTests -f EurekaServer/pom.xml
                mvn clean package -DskipTests -f ConfigServer/pom.xml
                mvn clean package -DskipTests -f APIGateway/pom.xml

                mvn clean package -DskipTests -f Product/pom.xml
                mvn clean package -DskipTests -f ProductCatalog/pom.xml
                mvn clean package -DskipTests -f Inventory/pom.xml
                mvn clean package -DskipTests -f Pricing/pom.xml

                mvn clean package -DskipTests -f Cart/pom.xml
                mvn clean package -DskipTests -f Recommendation/pom.xml
                '''
            }
        }

        // -----------------------------
        // DOCKER BUILD + TAG
        // -----------------------------
        stage('Docker Build & Tag') {
            steps {
                bat '''
                docker build -t %DOCKER_USER%/eureka-service ./EurekaServer
                docker build -t %DOCKER_USER%/config-service ./ConfigServer
                docker build -t %DOCKER_USER%/gateway-service ./APIGateway

                docker build -t %DOCKER_USER%/product-service ./Product
                docker build -t %DOCKER_USER%/catalog-service ./ProductCatalog
                docker build -t %DOCKER_USER%/inventory-service ./Inventory
                docker build -t %DOCKER_USER%/pricing-service ./Pricing

                docker build -t %DOCKER_USER%/cart-service ./Cart
                docker build -t %DOCKER_USER%/recommendation-service ./Recommendation
                '''
            }
        }

        // -----------------------------
        // DOCKER LOGIN
        // -----------------------------
        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'USERNAME',
                    passwordVariable: 'PASSWORD'
                )]) {
                    bat '''
                    echo %PASSWORD% | docker login -u %USERNAME% --password-stdin
                    '''
                }
            }
        }

        // -----------------------------
        // PUSH TO DOCKER HUB
        // -----------------------------
        stage('Push Images') {
            steps {
                bat '''
                docker push %DOCKER_USER%/eureka-service
                docker push %DOCKER_USER%/config-service
                docker push %DOCKER_USER%/gateway-service

                docker push %DOCKER_USER%/product-service
                docker push %DOCKER_USER%/catalog-service
                docker push %DOCKER_USER%/inventory-service
                docker push %DOCKER_USER%/pricing-service

                docker push %DOCKER_USER%/cart-service
                docker push %DOCKER_USER%/recommendation-service
                '''
            }
        }

        // -----------------------------
        // INIT SWARM
        // -----------------------------
        stage('Init Swarm') {
            steps {
                bat '''
                docker info | findstr "Swarm: active"
                IF %ERRORLEVEL% EQU 0 (
                    echo Swarm already initialized
                ) ELSE (
                    docker swarm init
                )
                '''
            }
        }

        // -----------------------------
        // REMOVE OLD STACK
        // -----------------------------
        stage('Remove Old Stack') {
            steps {
                bat '''
                docker stack rm %STACK_NAME%
                timeout /t 10
                exit /b 0
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
            echo "🚀 Deployment + Docker Hub push successful!"
        }
        failure {
            echo "❌ Pipeline failed — check logs"
        }
    }
}
