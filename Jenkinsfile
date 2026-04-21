pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        STACK_NAME = "ecommerce"
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
        // DOCKER BUILD
        // -----------------------------
        stage('Docker Build') {
            steps {
                bat '''
                docker build -t eureka-service ./EurekaServer
                docker build -t config-service ./ConfigServer
                docker build -t gateway-service ./APIGateway

                docker build -t product-service ./Product
                docker build -t catalog-service ./ProductCatalog
                docker build -t inventory-service ./Inventory
                docker build -t pricing-service ./Pricing

                docker build -t cart-service ./Cart
                docker build -t recommendation-service ./Recommendation
                '''
            }
        }

        // -----------------------------
        // INIT SWARM (FIXED)
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
        // REMOVE OLD STACK (SAFE)
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
        // VERIFY DEPLOYMENT
        // -----------------------------
        stage('Verify Deployment') {
            steps {
                bat '''
                docker service ls
                docker stack services %STACK_NAME%
                '''
            }
        }

        // -----------------------------
        // OPTIONAL: DEBUG LOGS
        // -----------------------------
        stage('Check Logs (Optional)') {
            steps {
                bat '''
                docker service logs %STACK_NAME%_gateway --tail 20
                '''
                // prevent failure if logs not available
                bat 'exit /b 0'
            }
        }
    }

    post {
        success {
            echo "🚀 Ecommerce Microservices deployed successfully on Docker Swarm!"
        }
        failure {
            echo "❌ Pipeline failed — check Jenkins logs carefully"
        }
    }
}
