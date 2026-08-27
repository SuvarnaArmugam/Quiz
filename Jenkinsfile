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

        APP_JAR = 'target/quizapp.jar'

        BACKEND_PORT = '8080'

        BACKEND_URL = 'http://localhost:8080/user/quizzes'


        // ============================================================
        // TOMCAT / APPZILLON
        // ============================================================

        APPZ_HOME = 'D:/Freshers_Software/Softwarepath/apache-tomcat-9.0.53'

        APPZ_ARTIFACTS = 'D:/jenkins-testing'

        TOMCAT_PORT = '8086'

        APP_CONTEXT = 'quizzapp'

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
                    echo CLEANING OLD TARGET DIRECTORY
                    echo ==========================================

                    if exist "target" (
                        rmdir /S /Q "target"
                    )

                    echo Old target directory removed.

                    echo.
                    echo ==========================================
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
                    echo ==========================================
                    echo TARGET DIRECTORY CONTENTS
                    echo ==========================================

                    dir target

                    echo.
                    echo ==========================================
                    echo SEARCHING FOR GENERATED JAR
                    echo ==========================================

                    if not exist "target\\*.jar" (
                        echo.
                        echo ERROR: No JAR file was generated.
                        echo.
                        echo Target directory contents:
                        dir target
                        exit /b 1
                    )

                    echo.
                    echo JAR FILES FOUND:
                    dir /B "target\\*.jar"

                    echo.
                    echo ==========================================
                    echo CREATING STANDARD JAR NAME
                    echo ==========================================

                    if exist "target\\quizapp.jar" (
                        del /F /Q "target\\quizapp.jar"
                    )

                    for /f "delims=" %%J in ('dir /B /O-D "target\\*.jar" ^| findstr /V /I "quizapp.jar"') do (
                        echo Generated JAR: %%J
                        copy /Y "target\\%%J" "target\\quizapp.jar" >nul
                        goto JAR_COPIED
                    )

                    :JAR_COPIED

                    if not exist "target\\quizapp.jar" (
                        echo.
                        echo ERROR: Could not create target\\quizapp.jar
                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo STANDARD JAR CREATED SUCCESSFULLY
                    echo ==========================================

                    dir "target\\quizapp.jar"
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

                    if not exist "%WORKSPACE%\\target\\quizapp.jar" (
                        echo ERROR: Backend JAR not found.
                        echo Expected:
                        echo %WORKSPACE%\\target\\quizapp.jar
                        exit /b 1
                    )

                    echo Backend JAR found successfully.

                    echo.
                    echo ==========================================
                    echo CHECKING PORT 8080
                    echo ==========================================

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr LISTENING ^| findstr ":8080"') do (
                        echo Found process %%a on port 8080
                        echo Stopping process %%a
                        taskkill /F /PID %%a >nul 2>&1
                    )

                    echo.
                    echo ==========================================
                    echo WAITING FOR PORT 8080
                    echo ==========================================

                    ping -n 4 127.0.0.1 >nul

                    echo.
                    echo ==========================================
                    echo STARTING QUIZAPP BACKEND VIA WMI
                    echo ==========================================

                    set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"

                    if exist "%WORKSPACE%\\backend.log" (
                        del /F /Q "%WORKSPACE%\\backend.log"
                    )

                    echo Starting Spring Boot application...

                    wmic process call create "cmd /c \\"%JAVA_HOME%\\bin\\java.exe\\" -jar \\"%WORKSPACE%\\target\\quizapp.jar\\" > \\"%WORKSPACE%\\backend.log\\" 2>&1"

                    echo.
                    echo BACKEND START COMMAND EXECUTED

                    echo.
                    echo ==========================================
                    echo WAITING FOR BACKEND
                    echo ==========================================

                    ping -n 9 127.0.0.1 >nul

                    echo.
                    echo ==========================================
                    echo PORT 8080 STATUS
                    echo ==========================================

                    netstat -ano | findstr LISTENING | findstr ":8080"

                    echo.
                    echo ==========================================
                    echo BACKEND LOG
                    echo ==========================================

                    if exist "%WORKSPACE%\\backend.log" (
                        powershell -NoProfile -Command "Get-Content '%WORKSPACE%\\backend.log' -Tail 50"
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

                    echo.
                    echo Backend Port:
                    echo %BACKEND_PORT%

                    set RETRIES=30

                    :CHECK_BACKEND

                    echo.
                    echo Checking backend port...
                    echo Attempts remaining: %RETRIES%

                    netstat -ano | findstr LISTENING | findstr ":8080" >nul

                    if not errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo BACKEND PORT IS RUNNING
                        echo ==========================================

                        echo Port 8080 is listening.

                        echo.
                        echo Testing backend URL...

                        curl -s -o nul -w "HTTP Status: %%{http_code}" "%BACKEND_URL%"

                        echo.

                        echo Backend application is running.

                        exit /b 0
                    )

                    echo Backend port is not ready yet.

                    set /a RETRIES-=1

                    if %RETRIES% LEQ 0 (

                        echo.
                        echo ==========================================
                        echo BACKEND FAILED TO START
                        echo ==========================================

                        echo.
                        echo ==========================================
                        echo PORT STATUS
                        echo ==========================================

                        netstat -ano | findstr ":8080"

                        echo.
                        echo ==========================================
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

                    ping -n 3 127.0.0.1 >nul

                    goto CHECK_BACKEND
                '''
            }
        }


        // ============================================================
        // 5. VERIFY APPZILLON WAR
        // ============================================================

        stage('Verify Appzillon WAR') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo VERIFYING APPZILLON WAR
                    echo ==========================================

                    echo Expected WAR:
                    echo %APPZ_ARTIFACTS%\\quizzapp.war

                    if not exist "%APPZ_ARTIFACTS%\\quizzapp.war" (

                        echo.
                        echo ERROR: quizzapp.war not found.

                        echo.
                        echo Checking artifact directory:

                        if exist "%APPZ_ARTIFACTS%" (
                            dir "%APPZ_ARTIFACTS%"
                        ) else (
                            echo Artifact directory does not exist.
                        )

                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo APPZILLON WAR FOUND
                    echo ==========================================

                    dir "%APPZ_ARTIFACTS%\\quizzapp.war"
                '''
            }
        }


        // ============================================================
        // 6. DEPLOY APPZILLON
        // ============================================================

        stage('Deploy Appzillon') {

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo DEPLOYING APPZILLON
                    echo ==========================================

                    echo.
                    echo ==========================================
                    echo CHECKING TOMCAT
                    echo ==========================================

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

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr LISTENING ^| findstr ":8086"') do (

                        echo Stopping process %%a on port 8086

                        taskkill /F /PID %%a >nul 2>&1
                    )

                    echo.
                    echo Waiting for Tomcat to stop...

                    ping -n 6 127.0.0.1 >nul

                    echo.
                    echo ==========================================
                    echo REMOVING OLD APPZILLON
                    echo ==========================================

                    if exist "%APPZ_HOME%\\webapps\\%APP_CONTEXT%" (
                        rmdir /S /Q "%APPZ_HOME%\\webapps\\%APP_CONTEXT%"
                    )

                    if exist "%APPZ_HOME%\\webapps\\%APP_CONTEXT%.war" (
                        del /F /Q "%APPZ_HOME%\\webapps\\%APP_CONTEXT%.war"
                    )

                    echo Old Appzillon application removed.

                    echo.
                    echo ==========================================
                    echo COPYING NEW WAR
                    echo ==========================================

                    copy /Y "%APPZ_ARTIFACTS%\\quizzapp.war" "%APPZ_HOME%\\webapps\\%APP_CONTEXT%.war"

                    if errorlevel 1 (

                        echo ERROR: Failed to copy quizzapp.war.

                        exit /b 1
                    )

                    echo WAR copied successfully.

                    echo.
                    echo ==========================================
                    echo STARTING TOMCAT VIA WMI
                    echo ==========================================

                    if exist "%APPZ_HOME%\\logs\\jenkins-run.log" (
                        del /F /Q "%APPZ_HOME%\\logs\\jenkins-run.log"
                    )

                    wmic process call create "cmd /c \\"%APPZ_HOME%\\bin\\catalina.bat\\" run > \\"%APPZ_HOME%\\logs\\jenkins-run.log\\" 2>&1"

                    echo.
                    echo TOMCAT START COMMAND EXECUTED

                    echo.
                    echo ==========================================
                    echo WAITING FOR TOMCAT
                    echo ==========================================

                    ping -n 16 127.0.0.1 >nul

                    echo.
                    echo ==========================================
                    echo TOMCAT PORT STATUS
                    echo ==========================================

                    netstat -ano | findstr LISTENING | findstr ":8086"

                    echo.
                    echo ==========================================
                    echo TOMCAT LOG
                    echo ==========================================

                    if exist "%APPZ_HOME%\\logs\\jenkins-run.log" (
                        powershell -NoProfile -Command "Get-Content '%APPZ_HOME%\\logs\\jenkins-run.log' -Tail 50"
                    ) else (
                        echo Tomcat log not found.
                    )
                '''
            }
        }


        // ============================================================
        // 7. APPZILLON HEALTH CHECK
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

                    echo.
                    echo Tomcat Port:
                    echo %TOMCAT_PORT%

                    set RETRIES=30

                    :CHECK_APPZILLON

                    echo.
                    echo Checking Tomcat port...
                    echo Attempts remaining: %RETRIES%

                    netstat -ano | findstr LISTENING | findstr ":8086" >nul

                    if not errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo TOMCAT IS RUNNING
                        echo ==========================================

                        echo Port 8086 is listening.

                        echo.
                        echo Testing Appzillon URL...

                        curl -s -o nul -w "HTTP Status: %%{http_code}" "%APPZILLON_URL%"

                        echo.

                        echo Appzillon is running.

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
                        echo PORT STATUS
                        echo ==========================================

                        netstat -ano | findstr ":8086"

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

                    ping -n 4 127.0.0.1 >nul

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