pipeline {

    agent any

    environment {

        // ==================================================
        // BACKEND
        // ==================================================
        BACKEND_JAR = "target\\quizapp.jar"
        BACKEND_PORT = "8080"

        // ==================================================
        // TOMCAT
        // Change this if your Tomcat is installed elsewhere
        // ==================================================
        TOMCAT_HOME = "D:\\Freshers_Software\\Softwarepath\\apache-tomcat-9.0.53"

        // ==================================================
        // APPZILLON WAR
        // Change this to your actual WAR file
        // ==================================================
        APPSERVER_WAR = "appzillon\\quizzapp.war"

        // Context path
        APP_CONTEXT = "quizzapp"
    }

    stages {

        // ==================================================
        // 1. CHECK WORKSPACE
        // ==================================================
        stage('Check Workspace') {
            steps {
                bat '''
                echo ==========================================
                echo CHECKING JENKINS WORKSPACE
                echo ==========================================

                echo Current Directory:
                cd

                echo.
                echo Workspace Contents:
                dir

                echo.
                echo Searching for pom.xml:
                dir /s /b pom.xml

                echo.
                echo Searching for WAR files:
                dir /s /b *.war
                '''
            }
        }


        // ==================================================
        // 2. BUILD BACKEND
        // ==================================================
        stage('Build Backend') {
            steps {
                bat '''
                echo ==========================================
                echo BUILDING SPRING BOOT BACKEND
                echo ==========================================

                echo Current Directory:
                cd

                echo.
                echo Checking pom.xml...

                if not exist pom.xml (
                    echo ERROR: pom.xml not found in Jenkins workspace.
                    echo.
                    echo Available pom.xml files:
                    dir /s /b pom.xml
                    exit /b 1
                )

                echo pom.xml found.

                echo.
                echo Running Maven build...

                mvn clean package -DskipTests

                if errorlevel 1 (
                    echo.
                    echo ERROR: Maven build failed.
                    exit /b 1
                )

                echo.
                echo Maven build completed successfully.

                echo.
                echo TARGET DIRECTORY:
                if not exist target (
                    echo ERROR: target directory was not created.
                    exit /b 1
                )

                dir target
                '''
            }
        }


        // ==================================================
        // 3. VERIFY BACKEND JAR
        // ==================================================
        stage('Verify JAR') {
            steps {
                bat '''
                echo ==========================================
                echo VERIFYING JAR
                echo ==========================================

                echo Expected JAR:
                echo target\\quizapp.jar

                echo.

                if exist target\\quizapp.jar (

                    echo ==========================================
                    echo BACKEND JAR FOUND
                    echo ==========================================
                    echo target\\quizapp.jar

                    echo.
                    echo JAR DETAILS:
                    dir target\\quizapp.jar

                ) else (

                    echo ==========================================
                    echo ERROR: BACKEND JAR NOT FOUND
                    echo ==========================================

                    echo Current Directory:
                    cd

                    echo.
                    echo Target Directory:
                    if exist target (
                        dir target
                    ) else (
                        echo target directory does not exist.
                    )

                    echo.
                    echo Searching entire workspace for JAR files:
                    dir /s /b *.jar

                    exit /b 1
                )
                '''
            }
        }


        // ==================================================
        // 4. STOP OLD BACKEND
        // ==================================================
        stage('Stop Existing Backend') {
            steps {
                bat '''
                echo ==========================================
                echo STOPPING EXISTING BACKEND
                echo ==========================================

                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
                    echo Found process %%a using port 8080
                    taskkill /PID %%a /F
                )

                echo.
                echo Port 8080 is now available.
                '''
            }
        }


        // ==================================================
        // 5. START BACKEND
        // ==================================================
        stage('Start Backend') {
            steps {
                bat '''
                echo ==========================================
                echo STARTING BACKEND
                echo ==========================================

                echo Starting:
                echo target\\quizapp.jar

                if not exist target\\quizapp.jar (
                    echo ERROR: quizapp.jar does not exist.
                    exit /b 1
                )

                if exist backend.log (
                    del /f /q backend.log
                )

                start "QuizApp Backend" /B cmd /c "java -jar target\\quizapp.jar > backend.log 2>&1"

                echo Backend process started.

                timeout /t 10 /nobreak

                echo.
                echo Backend log:
                if exist backend.log (
                    type backend.log
                ) else (
                    echo backend.log not created yet.
                )
                '''
            }
        }


        // ==================================================
        // 6. BACKEND HEALTH CHECK
        // ==================================================
        stage('Backend Health Check') {
            steps {
                bat '''
                echo ==========================================
                echo BACKEND HEALTH CHECK
                echo ==========================================

                echo Checking port 8080...

                netstat -ano | findstr :8080

                echo.
                echo Waiting for Spring Boot...
                timeout /t 5 /nobreak

                echo.
                echo Checking backend using curl...

                curl --fail --silent --show-error http://localhost:8080/actuator/health

                if errorlevel 1 (

                    echo.
                    echo ==========================================
                    echo BACKEND HEALTH CHECK FAILED
                    echo ==========================================

                    echo Backend log:
                    if exist backend.log (
                        type backend.log
                    )

                    exit /b 1

                ) else (

                    echo.
                    echo ==========================================
                    echo BACKEND IS HEALTHY
                    echo ==========================================
                )
                '''
            }
        }


        // ==================================================
        // 7. VERIFY APPZILLON WAR
        // ==================================================
        stage('Verify Appzillon WAR') {
            steps {
                bat '''
                echo ==========================================
                echo VERIFYING APPZILLON WAR
                echo ==========================================

                echo Expected WAR:
                echo %APPSERVER_WAR%

                if exist "%APPSERVER_WAR%" (

                    echo.
                    echo Appzillon WAR found.
                    dir "%APPSERVER_WAR%"

                ) else (

                    echo.
                    echo ERROR: Appzillon WAR not found.
                    echo.
                    echo Searching workspace for WAR files:
                    dir /s /b *.war

                    exit /b 1
                )
                '''
            }
        }


        // ==================================================
        // 8. STOP TOMCAT
        // ==================================================
        stage('Stop Tomcat') {
            steps {
                bat '''
                echo ==========================================
                echo STOPPING TOMCAT
                echo ==========================================

                if exist "%TOMCAT_HOME%\\bin\\shutdown.bat" (

                    call "%TOMCAT_HOME%\\bin\\shutdown.bat"

                    echo Tomcat shutdown command executed.

                    timeout /t 5 /nobreak

                ) else (

                    echo WARNING: Tomcat shutdown.bat not found.
                    echo Expected:
                    echo %TOMCAT_HOME%\\bin\\shutdown.bat
                )
                '''
            }
        }


        // ==================================================
        // 9. DEPLOY APPZILLON
        // ==================================================
        stage('Deploy Appzillon') {
            steps {
                bat '''
                echo ==========================================
                echo DEPLOYING APPZILLON
                echo ==========================================

                if not exist "%TOMCAT_HOME%\\webapps" (
                    echo ERROR: Tomcat webapps directory not found.
                    exit /b 1
                )

                echo.
                echo Removing previous application...

                if exist "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%" (
                    rmdir /S /Q "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%"
                )

                if exist "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%.war" (
                    del /F /Q "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%.war"
                )

                echo.
                echo Copying WAR...

                copy /Y "%APPSERVER_WAR%" "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%.war"

                if errorlevel 1 (
                    echo ERROR: Failed to copy Appzillon WAR.
                    exit /b 1
                )

                echo.
                echo WAR copied successfully.

                dir "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%.war"

                echo.
                echo Starting Tomcat...

                call "%TOMCAT_HOME%\\bin\\startup.bat"

                echo Tomcat startup command executed.

                timeout /t 15 /nobreak
                '''
            }
        }


        // ==================================================
        // 10. APPZILLON HEALTH CHECK
        // ==================================================
        stage('Appzillon Health Check') {
            steps {
                bat '''
                echo ==========================================
                echo APPZILLON HEALTH CHECK
                echo ==========================================

                echo Checking Tomcat port 8080...

                netstat -ano | findstr :8080

                echo.
                echo Checking application:

                curl --fail --silent --show-error http://localhost:8080/%APP_CONTEXT%/

                if errorlevel 1 (

                    echo.
                    echo ==========================================
                    echo APPZILLON HEALTH CHECK FAILED
                    echo ==========================================

                    echo.
                    echo Tomcat logs:

                    if exist "%TOMCAT_HOME%\\logs\\catalina.out" (
                        type "%TOMCAT_HOME%\\logs\\catalina.out"
                    )

                    echo.
                    echo Recent Tomcat logs:

                    dir "%TOMCAT_HOME%\\logs"

                    exit /b 1

                ) else (

                    echo.
                    echo ==========================================
                    echo APPZILLON IS RUNNING
                    echo ==========================================
                    echo URL:
                    echo http://localhost:8080/%APP_CONTEXT%/
                )
                '''
            }
        }
    }


    // ======================================================
    // POST ACTIONS
    // ======================================================
    post {

        success {
            echo '''
==========================================
QUIZZAPP DEPLOYMENT SUCCESSFUL
==========================================
Backend:
http://localhost:8080

Appzillon:
http://localhost:8080/quizzapp/

==========================================
'''
        }

        failure {
            echo '''
==========================================
QUIZZAPP DEPLOYMENT FAILED
==========================================
Check Jenkins console output.
Check backend.log.
Check Tomcat logs.
==========================================
'''
        }

        always {
            echo "=========================================="
            echo "JENKINS PIPELINE COMPLETED"
            echo "=========================================="
        }
    }
}