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
        // 1. CHECK WORKSPACE
        // ============================================================

        stage('Check Workspace') {

            steps {

                echo '=========================================='
                echo 'CHECKING JENKINS WORKSPACE'
                echo '=========================================='

                bat '''
                    @echo off

                    echo Current workspace:
                    echo %WORKSPACE%

                    echo.
                    echo ==========================================
                    echo WORKSPACE CONTENTS
                    echo ==========================================

                    dir

                    echo.
                    echo ==========================================
                    echo CHECKING POM.XML
                    echo ==========================================

                    if not exist "pom.xml" (
                        echo ERROR: pom.xml not found.
                        exit /b 1
                    )

                    echo pom.xml found successfully.

                    echo.
                    echo ==========================================
                    echo CHECKING SOURCE DIRECTORY
                    echo ==========================================

                    if not exist "src" (
                        echo ERROR: src directory not found.
                        exit /b 1
                    )

                    echo src directory found successfully.

                    echo.
                    echo Workspace check completed successfully.
                '''
            }
        }


        // ============================================================
        // 2. BUILD BACKEND JAR
        // ============================================================

        stage('Build Backend Jar') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo BUILDING QUIZAPP BACKEND
                    echo ==========================================

                    echo.
                    echo ==========================================
                    echo SETTING JAVA HOME
                    echo ==========================================

                    set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"

                    echo JAVA_HOME:
                    echo %JAVA_HOME%

                    echo.
                    echo ==========================================
                    echo JAVA VERSION
                    echo ==========================================

                    java -version

                    if errorlevel 1 (
                        echo ERROR: Java is not working.
                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo MAVEN VERSION
                    echo ==========================================

                    mvn -version

                    if errorlevel 1 (
                        echo ERROR: Maven is not working.
                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo STARTING MAVEN BUILD
                    echo ==========================================

                    echo Running:
                    echo mvn clean package -DskipTests

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
                    echo ==========================================
                    echo TARGET DIRECTORY
                    echo ==========================================

                    if not exist "target" (
                        echo ERROR: target directory not found.
                        exit /b 1
                    )

                    dir target

                    echo.
                    echo ==========================================
                    echo CHECKING GENERATED JAR
                    echo ==========================================

                    if not exist "%WORKSPACE%\\%APP_JAR%" (
                        echo ERROR: QuizApp JAR was not created.
                        echo Expected:
                        echo %WORKSPACE%\\%APP_JAR%

                        echo.
                        echo Available files in target:

                        dir "%WORKSPACE%\\target"

                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo JAR FOUND SUCCESSFULLY
                    echo ==========================================

                    echo JAR:
                    echo %WORKSPACE%\\%APP_JAR%

                    dir "%WORKSPACE%\\%APP_JAR%"
                '''
            }
        }


        // ============================================================
        // 3. DEPLOY BACKEND
        // ============================================================

        stage('Deploy Backend') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo DEPLOYING QUIZAPP BACKEND
                    echo ==========================================


                    echo.
                    echo ==========================================
                    echo VERIFYING BACKEND JAR
                    echo ==========================================

                    if not exist "%WORKSPACE%\\%APP_JAR%" (
                        echo ERROR: Backend JAR not found.
                        echo Expected:
                        echo %WORKSPACE%\\%APP_JAR%
                        exit /b 1
                    )

                    echo Backend JAR found successfully.


                    echo.
                    echo ==========================================
                    echo STOPPING OLD BACKEND
                    echo ==========================================

                    echo Checking port %BACKEND_PORT%...


                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr LISTENING ^| findstr ":%BACKEND_PORT%"') do (

                        echo Found process %%a on port %BACKEND_PORT%

                        echo Stopping process %%a

                        taskkill /F /PID %%a >nul 2>&1
                    )


                    echo.
                    echo ==========================================
                    echo WAITING FOR OLD BACKEND TO STOP
                    echo ==========================================

                    timeout /t 3 /nobreak >nul


                    echo.
                    echo ==========================================
                    echo CHECKING PORT %BACKEND_PORT%
                    echo ==========================================

                    netstat -ano | findstr LISTENING | findstr ":%BACKEND_PORT%" >nul

                    if not errorlevel 1 (
                        echo ERROR: Port %BACKEND_PORT% is still in use.
                        exit /b 1
                    )

                    echo Port %BACKEND_PORT% is available.


                    echo.
                    echo ==========================================
                    echo PREPARING BACKEND LOG
                    echo ==========================================

                    if exist "%WORKSPACE%\\backend.log" (
                        del /F /Q "%WORKSPACE%\\backend.log"
                    )


                    echo.
                    echo ==========================================
                    echo STARTING QUIZAPP BACKEND
                    echo ==========================================

                    set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"
                    set "JENKINS_NODE_COOKIE=dontKillMe"


                    echo Java:
                    java -version


                    echo.
                    echo Starting Spring Boot application...


                    start "QuizApp-Backend" /B cmd /c ^
                    "set JENKINS_NODE_COOKIE=dontKillMe&& java -jar "%WORKSPACE%\\%APP_JAR%" > "%WORKSPACE%\\backend.log" 2>&1"


                    echo.
                    echo BACKEND START COMMAND EXECUTED


                    echo.
                    echo ==========================================
                    echo WAITING FOR BACKEND
                    echo ==========================================

                    timeout /t 8 /nobreak >nul


                    echo.
                    echo ==========================================
                    echo BACKEND PORT STATUS
                    echo ==========================================

                    netstat -ano | findstr LISTENING | findstr ":%BACKEND_PORT%"


                    echo.
                    echo ==========================================
                    echo BACKEND LOG
                    echo ==========================================

                    if exist "%WORKSPACE%\\backend.log" (

                        powershell -NoProfile -Command "Get-Content '%WORKSPACE%\\backend.log' -Tail 40"

                    ) else (

                        echo backend.log not found.

                    )
                '''
            }
        }


        // ============================================================
        // 4. BACKEND HEALTH CHECK
        // ============================================================

        stage('Backend Health Check') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo BACKEND HEALTH CHECK
                    echo ==========================================

                    echo Backend URL:
                    echo %BACKEND_URL%

                    echo Backend Port:
                    echo %BACKEND_PORT%


                    set RETRIES=30


                    :CHECK_BACKEND

                    echo.
                    echo ==========================================
                    echo CHECKING BACKEND
                    echo ==========================================

                    echo Attempts remaining: %RETRIES%


                    netstat -ano | findstr LISTENING | findstr ":%BACKEND_PORT%" >nul


                    if not errorlevel 1 (

                        echo.
                        echo Backend port %BACKEND_PORT% is listening.

                        echo.
                        echo Testing API endpoint:

                        curl -s -o nul -w "HTTP Status: %%{http_code}" "%BACKEND_URL%"

                        echo.

                        echo.
                        echo ==========================================
                        echo BACKEND IS RUNNING
                        echo ==========================================

                        exit /b 0
                    )


                    echo Backend is not ready yet.


                    set /a RETRIES-=1


                    if %RETRIES% LEQ 0 (

                        echo.
                        echo ==========================================
                        echo BACKEND FAILED TO START
                        echo ==========================================

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

                    timeout /t 2 /nobreak >nul

                    goto CHECK_BACKEND
                '''
            }
        }


        // ============================================================
        // 5. DEPLOY APPZILLON
        // ============================================================

        stage('Deploy Appzillon') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo DEPLOYING APPZILLON QUIZAPP
                    echo ==========================================


                    echo.
                    echo ==========================================
                    echo CHECKING QUIZAPP WAR
                    echo ==========================================

                    echo WAR location:
                    echo %APPZ_ARTIFACTS%\\quizzapp.war


                    if not exist "%APPZ_ARTIFACTS%\\quizzapp.war" (

                        echo ERROR: quizzapp.war not found.

                        echo Expected:
                        echo %APPZ_ARTIFACTS%\\quizzapp.war

                        exit /b 1
                    )

                    echo quizzapp.war found successfully.


                    echo.
                    echo ==========================================
                    echo CHECKING TOMCAT
                    echo ==========================================

                    echo Tomcat Home:
                    echo %APPZ_HOME%

                    echo Tomcat Port:
                    echo %TOMCAT_PORT%


                    if not exist "%APPZ_HOME%\\bin\\catalina.bat" (

                        echo ERROR: catalina.bat not found.

                        echo Expected:
                        echo %APPZ_HOME%\\bin\\catalina.bat

                        exit /b 1
                    )

                    echo Tomcat installation found.


                    echo.
                    echo ==========================================
                    echo STOPPING TOMCAT
                    echo ==========================================

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr LISTENING ^| findstr ":%TOMCAT_PORT%"') do (

                        echo Found process %%a on port %TOMCAT_PORT%

                        echo Stopping process %%a

                        taskkill /F /PID %%a >nul 2>&1
                    )


                    echo.
                    echo ==========================================
                    echo WAITING FOR TOMCAT TO STOP
                    echo ==========================================

                    timeout /t 5 /nobreak >nul


                    echo.
                    echo ==========================================
                    echo REMOVING OLD APPZILLON
                    echo ==========================================

                    if exist "%APPZ_HOME%\\webapps\\quizzapp" (

                        echo Removing old exploded application...

                        rmdir /S /Q "%APPZ_HOME%\\webapps\\quizzapp"
                    )


                    if exist "%APPZ_HOME%\\webapps\\quizzapp.war" (

                        echo Removing old WAR...

                        del /F /Q "%APPZ_HOME%\\webapps\\quizzapp.war"
                    )


                    echo.
                    echo ==========================================
                    echo COPYING NEW WAR
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
                    echo ==========================================
                    echo PREPARING TOMCAT LOG
                    echo ==========================================

                    if exist "%APPZ_HOME%\\logs\\jenkins-run.log" (

                        del /F /Q "%APPZ_HOME%\\logs\\jenkins-run.log"
                    )


                    echo.
                    echo ==========================================
                    echo STARTING TOMCAT
                    echo ==========================================

                    set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"
                    set "CATALINA_HOME=%APPZ_HOME%"
                    set "JENKINS_NODE_COOKIE=dontKillMe"


                    echo Java version:

                    java -version


                    echo.
                    echo Starting Tomcat...


                    start "quizzapp-Tomcat" /B cmd /c ^
                    "set JENKINS_NODE_COOKIE=dontKillMe&& call "%APPZ_HOME%\\bin\\catalina.bat" run > "%APPZ_HOME%\\logs\\jenkins-run.log" 2>&1"


                    echo.
                    echo TOMCAT START COMMAND EXECUTED


                    echo.
                    echo ==========================================
                    echo WAITING FOR TOMCAT
                    echo ==========================================

                    timeout /t 10 /nobreak >nul


                    echo.
                    echo ==========================================
                    echo TOMCAT PORT STATUS
                    echo ==========================================

                    netstat -ano | findstr LISTENING | findstr ":%TOMCAT_PORT%"
                '''
            }
        }


        // ============================================================
        // 6. APPZILLON HEALTH CHECK
        // ============================================================

        stage('Appzillon Health Check') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo APPZILLON HEALTH CHECK
                    echo ==========================================

                    echo Appzillon URL:
                    echo %APPZILLON_URL%

                    echo Tomcat Port:
                    echo %TOMCAT_PORT%


                    set RETRIES=30


                    :CHECK_APPZILLON

                    echo.
                    echo ==========================================
                    echo CHECKING APPZILLON
                    echo ==========================================

                    echo Attempts remaining: %RETRIES%


                    netstat -ano | findstr LISTENING | findstr ":%TOMCAT_PORT%" >nul


                    if not errorlevel 1 (

                        echo.
                        echo Tomcat port %TOMCAT_PORT% is listening.

                        echo.
                        echo Testing Appzillon URL:

                        curl -s -o nul -w "HTTP Status: %%{http_code}" "%APPZILLON_URL%"

                        echo.

                        echo.
                        echo ==========================================
                        echo APPZILLON IS RUNNING
                        echo ==========================================

                        exit /b 0
                    )


                    echo Tomcat is not ready yet.


                    set /a RETRIES-=1


                    if %RETRIES% LEQ 0 (

                        echo.
                        echo ==========================================
                        echo APPZILLON FAILED TO START
                        echo ==========================================

                        echo.
                        echo ==========================================
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

                    timeout /t 3 /nobreak >nul

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
            echo 'http://localhost:8080'

            echo 'Backend API:'
            echo 'http://localhost:8080/api/user/quizzes'

            echo 'Appzillon:'
            echo 'http://localhost:8086/quizzapp/'

            echo '=========================================='
        }


        failure {

            echo '=========================================='
            echo 'QUIZAPP DEPLOYMENT FAILED'
            echo '=========================================='

            echo 'Check the failed stage.'
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