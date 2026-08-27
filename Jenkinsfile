pipeline {

    agent any

    environment {

        // ============================================================
        // JAVA
        // ============================================================

        JAVA_HOME = 'C:/Program Files/Java/jdk-17.0.2'


        // ============================================================
        // BACKEND
        // ============================================================

        APP_JAR = 'target/quizapp-0.0.1-SNAPSHOT.jar'

        BACKEND_PORT = '8080'

        BACKEND_URL = 'http://localhost:8080/api/user/quizzes'


        // ============================================================
        // TOMCAT / APPZILLON
        // ============================================================

        APPZ_HOME = 'D:/Freshers_Software/Softwarepath/apache-tomcat-9.0.53'

        APPZ_ARTIFACTS = 'D:/jenkins-testing'

        TOMCAT_PORT = '8086'

        APPZILLON_URL = 'http://localhost:8086/quizzapp/'
    }


    stages {


        // ============================================================
        // CHECKOUT
        // ============================================================

        stage('Checkout') {

            steps {

                echo '=========================================='
                echo 'CHECKING OUT QUIZAPP'
                echo '=========================================='

                git branch: 'main',
                    url: 'https://github.com/SuvarnaArmugam/Quiz.git'

                echo 'QUIZAPP CHECKOUT SUCCESSFUL'
            }
        }


        // ============================================================
        // BUILD BACKEND
        // ============================================================

        stage('Build Backend Jar') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo BUILDING QUIZAPP BACKEND
                    echo ==========================================

                    echo.
                    echo SETTING JAVA HOME
                    echo ==========================================

                    set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"

                    echo.
                    echo JAVA VERSION
                    echo ==========================================

                    java -version

                    if errorlevel 1 (
                        echo ERROR: Java is not working.
                        exit /b 1
                    )

                    echo.
                    echo MAVEN VERSION
                    echo ==========================================

                    mvn -version

                    if errorlevel 1 (
                        echo ERROR: Maven is not working.
                        exit /b 1
                    )

                    echo.
                    echo STARTING MAVEN BUILD
                    echo ==========================================

                    call mvn clean package -DskipTests

                    if errorlevel 1 (
                        echo.
                        echo ==========================================
                        echo MAVEN BUILD FAILED
                        echo ==========================================
                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo MAVEN BUILD SUCCESSFUL
                    echo ==========================================

                    echo.
                    echo TARGET DIRECTORY
                    echo ==========================================

                    dir target

                    echo.
                    echo CHECKING JAR
                    echo ==========================================

                    if not exist "%WORKSPACE%\\%APP_JAR%" (
                        echo.
                        echo ERROR: QuizApp JAR was not created.
                        echo Expected:
                        echo %WORKSPACE%\\%APP_JAR%
                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo JAR FOUND SUCCESSFULLY
                    echo ==========================================

                    echo %APP_JAR%
                '''
            }
        }


        // ============================================================
        // DEPLOY BACKEND
        // ============================================================

        stage('Deploy Backend') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo DEPLOYING QUIZAPP BACKEND
                    echo ==========================================

                    echo.
                    echo CHECKING QUIZAPP JAR
                    echo ==========================================

                    if not exist "%WORKSPACE%\\%APP_JAR%" (
                        echo ERROR: QuizApp JAR not found.
                        echo Expected:
                        echo %WORKSPACE%\\%APP_JAR%
                        exit /b 1
                    )

                    echo QuizApp JAR found successfully.


                    echo.
                    echo CHECKING PORT %BACKEND_PORT%
                    echo ==========================================

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%BACKEND_PORT% ^| findstr LISTENING') do (

                        echo Found process %%a on port %BACKEND_PORT%

                        echo Stopping process %%a

                        taskkill /F /PID %%a >nul 2>&1
                    )


                    echo.
                    echo WAITING FOR PORT %BACKEND_PORT%
                    echo ==========================================

                    ping 127.0.0.1 -n 4 >nul


                    echo.
                    echo STARTING QUIZAPP BACKEND
                    echo ==========================================

                    set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"
                    set "JENKINS_NODE_COOKIE=dontKillMe"


                    if exist "%WORKSPACE%\\backend.log" (
                        del /F /Q "%WORKSPACE%\\backend.log"
                    )


                    echo Starting Spring Boot application...

                    start "QuizApp-Backend" /B cmd /c ^
                    "set JENKINS_NODE_COOKIE=dontKillMe&& java -jar "%WORKSPACE%\\%APP_JAR%" > "%WORKSPACE%\\backend.log" 2>&1"


                    echo.
                    echo QUIZAPP START COMMAND EXECUTED


                    echo.
                    echo WAITING FOR APPLICATION
                    echo ==========================================

                    ping 127.0.0.1 -n 8 >nul


                    echo.
                    echo CHECKING BACKEND PORT
                    echo ==========================================

                    netstat -ano | findstr LISTENING | findstr ":%BACKEND_PORT%"


                    echo.
                    echo BACKEND LOG
                    echo ==========================================

                    if exist "%WORKSPACE%\\backend.log" (

                        powershell -NoProfile -Command "Get-Content '%WORKSPACE%\\backend.log' -Tail 30"

                    ) else (

                        echo WARNING: backend.log not found.

                    )
                '''
            }
        }


        // ============================================================
        // BACKEND HEALTH CHECK
        // ============================================================

        stage('Backend Health Check') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo CHECKING QUIZAPP BACKEND
                    echo ==========================================

                    echo.
                    echo Backend URL:
                    echo %BACKEND_URL%

                    echo.
                    echo Backend Port:
                    echo %BACKEND_PORT%


                    set RETRIES=30


                    :CHECK_BACKEND

                    echo.
                    echo Checking backend...
                    echo Attempts remaining: %RETRIES%


                    netstat -ano | findstr LISTENING | findstr ":%BACKEND_PORT%" >nul


                    if not errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo BACKEND PORT IS RUNNING
                        echo ==========================================

                        echo Port %BACKEND_PORT% is listening.

                        echo.
                        echo Testing backend endpoint...

                        curl -s -o nul -w "HTTP Status: %%{http_code}" "%BACKEND_URL%"

                        echo.

                        exit /b 0
                    )


                    echo Backend is not ready.


                    set /a RETRIES-=1


                    if %RETRIES% LEQ 0 (

                        echo.
                        echo ==========================================
                        echo BACKEND FAILED TO START
                        echo ==========================================

                        echo.
                        echo PORT STATUS
                        echo ==========================================

                        netstat -ano | findstr ":%BACKEND_PORT%"


                        echo.
                        echo BACKEND LOG
                        echo ==========================================

                        if exist "%WORKSPACE%\\backend.log" (

                            type "%WORKSPACE%\\backend.log"

                        ) else (

                            echo backend.log not found.

                        )

                        exit /b 1
                    )


                    echo Waiting 2 seconds...

                    ping 127.0.0.1 -n 3 >nul

                    goto CHECK_BACKEND
                '''
            }
        }


        // ============================================================
        // DEPLOY APPZILLON
        // ============================================================

        stage('Deploy Appzillon') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo DEPLOYING APPZILLON QUIZAPP
                    echo ==========================================


                    echo.
                    echo CHECKING QUIZAPP WAR
                    echo ==========================================

                    if not exist "%APPZ_ARTIFACTS%\\quizzapp.war" (

                        echo ERROR: quizzapp.war not found.

                        echo Expected:
                        echo %APPZ_ARTIFACTS%\\quizzapp.war

                        exit /b 1
                    )

                    echo quizzapp.war found successfully.


                    echo.
                    echo CHECKING TOMCAT
                    echo ==========================================

                    echo Tomcat Home:
                    echo %APPZ_HOME%

                    echo Tomcat Port:
                    echo %TOMCAT_PORT%


                    if not exist "%APPZ_HOME%\\bin\\catalina.bat" (

                        echo ERROR: catalina.bat not found.

                        exit /b 1
                    )

                    echo Tomcat installation found.


                    echo.
                    echo STOPPING TOMCAT
                    echo ==========================================

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%TOMCAT_PORT% ^| findstr LISTENING') do (

                        echo Stopping Tomcat process %%a

                        taskkill /F /PID %%a >nul 2>&1
                    )


                    echo.
                    echo WAITING FOR TOMCAT TO STOP
                    echo ==========================================

                    ping 127.0.0.1 -n 6 >nul


                    echo.
                    echo REMOVING OLD APPZILLON
                    echo ==========================================

                    if exist "%APPZ_HOME%\\webapps\\quizzapp" (

                        rmdir /S /Q "%APPZ_HOME%\\webapps\\quizzapp"
                    )


                    if exist "%APPZ_HOME%\\webapps\\quizzapp.war" (

                        del /F /Q "%APPZ_HOME%\\webapps\\quizzapp.war"
                    )


                    echo.
                    echo COPYING QUIZAPP WAR
                    echo ==========================================

                    copy /Y ^
                    "%APPZ_ARTIFACTS%\\quizzapp.war" ^
                    "%APPZ_HOME%\\webapps\\quizzapp.war"


                    if errorlevel 1 (

                        echo ERROR: Failed to copy quizzapp.war.

                        exit /b 1
                    )

                    echo quizzapp.war copied successfully.


                    echo.
                    echo STARTING TOMCAT
                    echo ==========================================

                    set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"
                    set "CATALINA_HOME=%APPZ_HOME%"
                    set "JENKINS_NODE_COOKIE=dontKillMe"


                    if exist "%APPZ_HOME%\\logs\\jenkins-run.log" (
                        del /F /Q "%APPZ_HOME%\\logs\\jenkins-run.log"
                    )


                    echo Starting Tomcat...


                    start "quizzapp-Tomcat" /B cmd /c ^
                    "set JENKINS_NODE_COOKIE=dontKillMe&& call "%APPZ_HOME%\\bin\\catalina.bat" run > "%APPZ_HOME%\\logs\\jenkins-run.log" 2>&1"


                    echo.
                    echo TOMCAT START COMMAND EXECUTED


                    echo.
                    echo WAITING FOR TOMCAT
                    echo ==========================================

                    ping 127.0.0.1 -n 10 >nul


                    echo.
                    echo TOMCAT PORT STATUS
                    echo ==========================================

                    netstat -ano | findstr LISTENING | findstr ":%TOMCAT_PORT%"
                '''
            }
        }


        // ============================================================
        // APPZILLON HEALTH CHECK
        // ============================================================

        stage('Appzillon Health Check') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo CHECKING APPZILLON
                    echo ==========================================

                    echo.
                    echo Appzillon URL:
                    echo %APPZILLON_URL%

                    echo.
                    echo Tomcat Port:
                    echo %TOMCAT_PORT%


                    set RETRIES=30


                    :CHECK_APPZILLON

                    echo.
                    echo Checking Tomcat...
                    echo Attempts remaining: %RETRIES%


                    netstat -ano | findstr LISTENING | findstr ":%TOMCAT_PORT%" >nul


                    if not errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo TOMCAT IS RUNNING
                        echo ==========================================

                        echo Tomcat port %TOMCAT_PORT% is listening.


                        echo.
                        echo TESTING APPZILLON URL
                        echo ==========================================

                        curl -s -o nul -w "HTTP Status: %%{http_code}" "%APPZILLON_URL%"

                        echo.

                        exit /b 0
                    )


                    echo Tomcat is not ready.


                    set /a RETRIES-=1


                    if %RETRIES% LEQ 0 (

                        echo.
                        echo ==========================================
                        echo APPZILLON FAILED TO START
                        echo ==========================================


                        echo.
                        echo TOMCAT LOG
                        echo ==========================================

                        if exist "%APPZ_HOME%\\logs\\jenkins-run.log" (

                            type "%APPZ_HOME%\\logs\\jenkins-run.log"

                        ) else (

                            echo Tomcat log not found.

                        )

                        exit /b 1
                    )


                    echo Waiting 3 seconds...

                    ping 127.0.0.1 -n 4 >nul

                    goto CHECK_APPZILLON
                '''
            }
        }
    }


    // ============================================================
    // POST ACTIONS
    // ============================================================

    post {

        success {

            echo '=========================================='
            echo 'QUIZAPP DEPLOYMENT SUCCESSFUL'
            echo '=========================================='

            echo 'Backend:'
            echo 'http://localhost:8080/api/user/quizzes'

            echo 'Appzillon:'
            echo 'http://localhost:8086/quizzapp/'

            echo '=========================================='
        }


        failure {

            echo '=========================================='
            echo 'QUIZAPP DEPLOYMENT FAILED'
            echo '=========================================='

            echo 'Check the stage that failed.'
            echo 'Check backend.log.'
            echo 'Check Tomcat logs.'

            echo '=========================================='
        }


        always {

            echo '=========================================='
            echo 'JENKINS PIPELINE COMPLETED'
            echo '=========================================='
        }
    }
}