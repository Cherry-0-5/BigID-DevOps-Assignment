pipeline {
    agent any

    tools {
        maven 'Maven39'       
        jdk 'Java17'     
    }

    environment {
        REGISTRY        = "ghcr.io"
        PROJECT         = "cherry-0-5/ip-echo-project"
        GITHUB_TOKEN    = credentials('GitHub-Token') 
        CHART_PATH      = "charts/ip-echo-chart"
        NAMESPACE       = "staging"
        GIT_SHA         = ""
        IMAGE_TAG       = ""
        API_SERVER_URL  = "https://155.248.254.93:6443" 
    }

    stages {
        stage('Git Init & Tagging') {
            steps {
                script {
		    deleteDir()
                    checkout scm
                    GIT_SHA = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    IMAGE_TAG = "${GIT_SHA}-b${env.BUILD_ID}"
                    
                    step([$class: 'GitHubCommitStatusSetter', statusResultSource: [$class: 'ConditionalStatusResultSource', results: [[$class: 'AnyBuildResult', message: 'Pipeline in progress...', state: 'PENDING']]]])
                }
            }
        }

        stage('Package Build & Unit Test') {
            parallel {
                stage('Java Backend Analysis') {
                    steps {
                        dir('ip-echo-api-service') {
                            echo "Running JUnit, JaCoCo, Checkstyle, PMD, and SpotBugs..."
			    sh "mvn clean verify -Dmaven.repo.local=/opt/jenkins-cache/m2 -Djacoco.haltOnFailure=true -DdependencyCheck.skip=true"
                        }
                    }
                }
                stage('NodeJS Frontend Lint') {
                    steps {
                        dir('ip-displayer-frontend') {
                            sh """
        			docker run --rm \
          			--user \$(id -u):\$(id -g) \
          			-v \$(pwd):/app \
          			-v /opt/jenkins-cache/frontend-node_modules:/app/node_modules \
          			-w /app \
          			-e npm_config_cache=/app/.npm-cache \
          			node:20-slim \
          			sh -c 'npm install && npm run lint'
    			    """
                        }
                    }
                }
            }
        }

        stage('Image Build & SBOM') {
            steps {
                script {
                    def backendImage = "${REGISTRY}/${PROJECT}-backend:${IMAGE_TAG}"
                    def frontendImage = "${REGISTRY}/${PROJECT}-frontend:${IMAGE_TAG}"
                    
                    sh "docker build -t ${backendImage} ./ip-echo-api-service"
                    sh "docker build -t ${frontendImage} ./ip-displayer-frontend"

                    // Produce SBOM (Software Bill of Materials)
                    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock anchore/syft ${backendImage} -o cyclonedx-json > backend-sbom.json"
                }
            }
        }

        stage('Image Scan') {
            steps {
                script {
                    def backendImage = "${REGISTRY}/${PROJECT}-backend:${IMAGE_TAG}"
                   
		    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image --timeout 15m --ignore-unfixed --format template --template '@/contrib/html.tpl' -o trivy-report.html ${backendImage}" 
                
		    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image --timeout 15m --ignore-unfixed --exit-code 0 --severity HIGH,CRITICAL ${backendImage}"    
                }
            }
        }

        stage('GHCR Registry Push') {
            steps {
                script {
			sh "echo '${GITHUB_TOKEN}' | docker login ${REGISTRY} -u Cherry-0-5 --password-stdin"                        

			sh "docker push ${REGISTRY}/${PROJECT}-backend:${IMAGE_TAG}"
                        sh "docker push ${REGISTRY}/${PROJECT}-frontend:${IMAGE_TAG}"
                }
            }
        }

        stage('Helm Lint') {
            steps {
                echo "Validating Helm Chart syntax post-push..."
                sh "docker run --rm -v ${WORKSPACE}:/apps -w /apps alpine/helm:3.12.0 lint ${CHART_PATH} --strict"
            }
        }

        stage('Deploy to OKE') {
            steps {
                withCredentials([string(credentialsId: 'KUBE_TOKEN', variable: 'TOKEN')]) {
                    sh """
                    echo "Configuring kubectl with Service Account Token..."
                    
                    kubectl config set-cluster ip-echo-cluster \
                      --server=${API_SERVER_URL} \
                      --insecure-skip-tls-verify=true
                    
                    kubectl config set-credentials sa-jenkins-ipecho --token=${TOKEN}
                    
                    kubectl config set-context ip-echo-cluster-context \
                      --cluster=ip-echo-cluster \
                      --user=sa-jenkins-ipecho \
                      --namespace=${NAMESPACE}
                    
                    kubectl config use-context ip-echo-cluster-context
                    
                    echo "Deploying with Helm..."
                    
   		    helm upgrade --install ip-echo-staging ${CHART_PATH} \
                      --namespace ${NAMESPACE} \
                      --create-namespace \
                      --set backend.image.tag=${IMAGE_TAG} \
                      --set frontend.image.tag=${IMAGE_TAG} \
                      --wait
                    """
                }
            }
        }

        stage('Smoke Test') {
		    steps {
		        script {
		            try {
		                echo 'Running smoke test against backend...'
		                sh """
		                kubectl run smoketest-pod -n staging --rm -i --tty \
		                --image=docker.io/curlimages/curl:latest \
		                --restart=Never -- \
		                curl -f http://ip-echo-api-service.${NAMESPACE}.svc.cluster.local:8088/health
		                """
		            } catch (Exception e) {
		                echo 'Smoke test failed.'
		                throw e 
		            } finally {
		                echo 'Cleaning up debug pod...'
		                sh "kubectl delete pod smoketest-pod -n staging --ignore-not-found=true"
		            }
		        }
		    }
		    post {
		        failure {
		            slackSend(
		                channel: '#jenkins-alerts',
		                color: 'danger',
		                message: "FAILED: Smoke Test in Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
		            )
		        }
		    }
		}

        stage('Auto-Merge Promotion') {
            when { 
     	         allOf {
            	     not { branch 'main' }

					 success('Deploy to OKE')
					 success('Smoke Test')
       		 }
   	    }
            steps {
                script {
                    echo "Creating Pull Request and triggering Auto-Merge..."
                    sh """
                    export GH_TOKEN=${GITHUB_TOKEN}
                   
		    if gh pr list --head ${env.BRANCH_NAME} --json number | grep -q "\[\]"; then
              		echo "Creating new PR..."
                	gh pr create --base main --head ${env.BRANCH_NAME} \
                    	    --title "Automated Merge: Build #${env.BUILD_NUMBER}" \
                   	    --body "Deployment and Smoke Tests Passed. Promoting ${IMAGE_TAG} to main."
            	   else
                	echo "Pull request already exists for ${env.BRANCH_NAME}."
            	   fi
 
		   gh pr merge --auto --squash --delete-branch ${env.BRANCH_NAME}
                   """
                }
            }
        }
    }

    post {
        always {
            junit 'ip-echo-api-service/target/surefire-reports/*.xml'
            jacoco execPattern: 'ip-echo-api-service/target/*.exec'
            archiveArtifacts artifacts: 'backend-sbom.json, trivy-report.html', fingerprint: true
            
            publishHTML([
                allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true,
                reportDir: '.', reportFiles: 'trivy-report.html', 
                reportName: 'Trivy Security Report'
            ])
            
            recordIssues tools: [
                checkStyle(pattern: 'ip-echo-api-service/target/checkstyle-result.xml'),
                pmdParser(pattern: 'ip-echo-api-service/target/pmd.xml'),
                spotBugs(pattern: 'ip-echo-api-service/target/spotbugsXml.xml')
            ]
        }
        success {
            script {
                step([$class: 'GitHubCommitStatusSetter', statusResultSource: [$class: 'ConditionalStatusResultSource', results: [[$class: 'AnyBuildResult', message: 'Deployment & Auto-Merge Successful!', state: 'SUCCESS']]]])
                // slackSend(color: 'good', message: "✅ *Deployment Successful* - #${env.BUILD_NUMBER}\nArtifact: ${IMAGE_TAG}")
            }
        }
        failure {
            script {
                step([$class: 'GitHubCommitStatusSetter', statusResultSource: [$class: 'ConditionalStatusResultSource', results: [[$class: 'AnyBuildResult', message: 'Pipeline Failed', state: 'FAILURE']]]])
                // slackSend(color: 'danger', message: "❌ *Build Failed* - #${env.BUILD_NUMBER}")
            }
        }
    }
}


