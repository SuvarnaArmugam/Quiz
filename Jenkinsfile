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

        BACKEND_URL = 'http://localhost:8080/api/user'


        // ============================================================
        // TOMCAT / APPZILLON
        // ============================================================

        TOMCAT_HOME = 'D:/Freshers_Software/Softwarepath/apache-tomcat-9.0.53'

        APPZILLON_WAR = 'C:/Users/suvarna.lnu/Downloads/jenkins/quizzapp.war'

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
                    cd

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
        // 2. BUILD BACKEND
        // ============================================================

        stage('Build Backend Jar') {

            steps {

                echo '=========================================='
                echo 'BUILDING QUIZAPP BACKEND'
                echo '=========================================='


                // ----------------------------------------------------
                // JAVA + MAVEN
                // ----------------------------------------------------

                bat '''
                    @echo off

                    set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"

                    echo ==========================================
                    echo JAVA VERSION
                    echo ==========================================

                    java -version

                    echo.
                    echo ==========================================
                    echo MAVEN VERSION
                    echo ==========================================

                    mvn -version
                '''


                // ----------------------------------------------------
                // CHECK MAVEN PROJECT
                // ----------------------------------------------------

                bat '''
                    @echo off

                    echo ==========================================
                    echo CHECKING MAVEN PROJECT
                    echo ==========================================

                    if not exist "pom.xml" (
                        echo ERROR: pom.xml not found.
                        exit /b 1
                    )

                    echo pom.xml found successfully.
                '''


                // ----------------------------------------------------
                // STOP OLD BACKEND
                // ----------------------------------------------------

                bat '''
                    @echo off

                    echo ==========================================
                    echo STOPPING OLD BACKEND
                    echo ==========================================

                    echo Checking port 8080...

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr LISTENING ^| findstr ":8080"') do (
                        echo Stopping process %%a on port 8080
                        taskkill /F /PID %%a >nul 2>&1
                    )

                    echo.
                    echo Waiting for old backend to stop...

                    timeout /t 3 /nobreak >nul

                    echo.
                    echo ==========================================
                    echo FINAL PORT 8080 CHECK
                    echo ==========================================

                    netstat -ano | findstr LISTENING | findstr ":8080"

                    if not errorlevel 1 (
                        echo ERROR: Port 8080 is still in use.
                        exit /b 1
                    )

                    echo Port 8080 is available.

                    echo.
                    echo Old backend cleanup completed successfully.
                '''


                // ----------------------------------------------------
                // MAVEN BUILD
                // ----------------------------------------------------

                bat '''
                    @echo off

                    set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"

                    echo ==========================================
                    echo STARTING MAVEN BUILD
                    echo ==========================================

                    java -version

                    echo.
                    echo Running Maven build...

                    mvn clean package -DskipTests

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
                '''


                // ----------------------------------------------------
                // CHECK GENERATED JAR
                // ----------------------------------------------------

                bat '''
                    @echo off

                    echo ==========================================
                    echo CHECKING GENERATED JAR
                    echo ==========================================

                    if not exist "target\\quizapp.jar" (
                        echo ERROR: target\\quizapp.jar not found.

                        echo.
                        echo Target directory contents:

                        if exist "target" (
                            dir "target"
                        ) else (
                            echo target directory does not exist.
                        )

                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo QUIZAPP JAR FOUND
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

                echo '=========================================='
                echo 'DEPLOYING QUIZAPP BACKEND'
                echo '=========================================='


                bat '''
                    @echo off

                    echo ==========================================
                    echo VERIFYING BACKEND JAR
                    echo ==========================================

                    if not exist "target\\quizapp.jar" (
                        echo ERROR: Backend JAR not found.
                        exit /b 1
                    )

                    echo quizapp.jar found successfully.


                    echo.
                    echo ==========================================
                    echo STOPPING ANY PROCESS ON PORT 8080
                    echo ==========================================

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr LISTENING ^| findstr ":8080"') do (
                        echo Stopping process %%a
                        taskkill /F /PID %%a >nul 2>&1
                    )


                    timeout /t 3 /nobreak >nul


                    echo.
                    echo ==========================================
                    echo STARTING QUIZAPP BACKEND
                    echo ==========================================

                    set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"
                    set "JENKINS_NODE_COOKIE=dontKillMe"


                    if exist "%WORKSPACE%\\backend.log" (
                        del /F /Q "%WORKSPACE%\\backend.log"
                    )


                    echo Java version:
                    java -version


                    echo.
                    echo Starting QuizApp...


                    start "QuizApp-Backend" /B cmd /c "set JENKINS_NODE_COOKIE=dontKillMe&& set JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2&& set PATH=C:\\Program Files\\Java\\jdk-17.0.2\\bin;%%PATH%%&& java -jar \"%WORKSPACE%\\target\\quizapp.jar\" > \"%WORKSPACE%\\backend.log\" 2>&1"


                    echo.
                    echo BACKEND START COMMAND EXECUTED


                    echo.
                    echo Waiting for application to start...

                    timeout /t 8 /nobreak >nul


                    echo.
                    echo ==========================================
                    echo BACKEND PORT STATUS
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

                echo '=========================================='
                echo 'CHECKING QUIZAPP BACKEND'
                echo '=========================================='


                bat '''
                    @echo off

                    echo.
                    echo Backend URL:
                    echo %BACKEND_URL%

                    echo.
                    echo Backend Port:
                    echo %BACKEND_PORT%


                    echo.
                    echo ==========================================
                    echo WAITING FOR BACKEND PORT
                    echo ==========================================


                    set RETRIES=30


                    :CHECK_BACKEND

                    echo.
                    echo Checking port 8080...
                    echo Attempts remaining: %RETRIES%


                    netstat -ano | findstr LISTENING | findstr ":8080" >nul


                    if not errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo BACKEND IS RUNNING
                        echo ==========================================

                        echo Port 8080 is listening.

                        echo.
                        echo Testing API endpoint...

                        curl -s -o nul -w "HTTP Status: %%{http_code}" "%BACKEND_URL%"

                        echo.

                        echo.
                        echo Backend is successfully started.

                        exit /b 0
                    )


                    echo Backend port 8080 is not ready yet.


                    set /a RETRIES-=1


                    if %RETRIES% LEQ 0 (

                        echo.
                        echo ==========================================
                        echo BACKEND FAILED TO START
                        echo ==========================================


                        echo.
                        echo ==========================================
                        echo PORT 8080 STATUS
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


                    echo.
                    echo Waiting 2 seconds...

                    timeout /t 2 /nobreak >nul


                    goto CHECK_BACKEND
                '''
            }
        }


        // ============================================================
        // 5. VERIFY APPZILLON WAR
        // ============================================================

        stage('Verify Appzillon WAR') {

            steps {

                echo '=========================================='
                echo 'CHECKING APPZILLON WAR'
                echo '=========================================='


                bat '''
                    @echo off

                    echo ==========================================
                    echo VERIFYING APPZILLON WAR
                    echo ==========================================

                    echo Expected WAR:
                    echo %APPZILLON_WAR%


                    if not exist "%APPZILLON_WAR%" (

                        echo.
                        echo ERROR:
                        echo Appzillon WAR not found.

                        echo.
                        echo Expected:
                        echo %APPZILLON_WAR%

                        exit /b 1
                    )


                    echo.
                    echo ==========================================
                    echo APPZILLON WAR FOUND
                    echo ==========================================

                    dir "%APPZILLON_WAR%"
                '''
            }
        }


        // ============================================================
        // 6. DEPLOY APPZILLON
        // ============================================================

        stage('Deploy Appzillon') {

            steps {

                echo '=========================================='
                echo 'DEPLOYING APPZILLON QUIZAPP'
                echo '=========================================='


                bat '''
                    @echo off

                    echo ==========================================
                    echo CHECKING TOMCAT
                    echo ==========================================


                    if not exist "%TOMCAT_HOME%\\bin\\catalina.bat" (

                        echo ERROR:
                        echo catalina.bat not found.

                        exit /b 1
                    )


                    echo Tomcat installation found.


                    echo.
                    echo ==========================================
                    echo STOPPING TOMCAT
                    echo ==========================================


                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr LISTENING ^| findstr ":%TOMCAT_PORT%"') do (

                        echo Killing PID %%a on port %TOMCAT_PORT%

                        taskkill /F /PID %%a >nul 2>&1
                    )


                    echo.
                    echo Waiting for Tomcat to stop...

                    timeout /t 5 /nobreak >nul


                    echo.
                    echo ==========================================
                    echo REMOVING OLD APPZILLON APPLICATION
                    echo ==========================================


                    if exist "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%" (

                        echo Removing old exploded application...

                        rmdir /S /Q "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%"
                    )


                    if exist "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%.war" (

                        echo Removing old WAR...

                        del /F /Q "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%.war"
                    )


                    echo.
                    echo ==========================================
                    echo COPYING NEW APPZILLON WAR
                    echo ==========================================


                    copy /Y "%APPZILLON_WAR%" "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%.war"


                    if errorlevel 1 (

                        echo ERROR:
                        echo Failed to copy Appzillon WAR.

                        exit /b 1
                    )


                    echo Appzillon WAR copied successfully.


                    echo.
                    echo ==========================================
                    echo STARTING TOMCAT
                    echo ==========================================


                    set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"
                    set "CATALINA_HOME=%TOMCAT_HOME%"
                    set "JENKINS_NODE_COOKIE=dontKillMe"


                    echo Java version:
                    java -version


                    echo.
                    echo Starting Tomcat...


                    call "%TOMCAT_HOME%\\bin\\startup.bat"


                    if errorlevel 1 (

                        echo.
                        echo ERROR:
                        echo Tomcat failed to start.

                        exit /b 1
                    )


                    echo.
                    echo TOMCAT START COMMAND EXECUTED


                    echo.
                    echo Waiting for Tomcat...

                    timeout /t 15 /nobreak >nul


                    echo.
                    echo ==========================================
                    echo TOMCAT PORT STATUS
                    echo ==========================================

                    netstat -ano | findstr LISTENING | findstr ":%TOMCAT_PORT%"


                    echo.
                    echo ==========================================
                    echo TOMCAT LOGS
                    echo ==========================================

                    if exist "%TOMCAT_HOME%\\logs" (
                        dir "%TOMCAT_HOME%\\logs"
                    ) else (
                        echo Tomcat logs directory not found.
                    )
                '''
            }
        }


        // ============================================================
        // 7. APPZILLON HEALTH CHECK
        // ============================================================

        stage('Appzillon Health Check') {

            steps {

                echo '=========================================='
                echo 'CHECKING APPZILLON'
                echo '=========================================='


                bat '''
                    @echo off

                    echo.
                    echo Appzillon URL:
                    echo %APPZILLON_URL%

                    echo.
                    echo Tomcat Port:
                    echo %TOMCAT_PORT%


                    echo.
                    echo ==========================================
                    echo WAITING FOR TOMCAT
                    echo ==========================================


                    set RETRIES=30


                    :CHECK_APPZILLON


                    echo.
                    echo Checking Tomcat port...
                    echo Attempts remaining: %RETRIES%


                    netstat -ano | findstr LISTENING | findstr ":%TOMCAT_PORT%" >nul


                    if not errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo TOMCAT IS RUNNING
                        echo ==========================================

                        echo Tomcat port %TOMCAT_PORT% is listening.


                        echo.
                        echo Testing Appzillon URL...


                        curl -s -o nul -w "HTTP Status: %%{http_code}" "%APPZILLON_URL%"


                        echo.


                        echo.
                        echo Appzillon deployment process completed.

                        exit /b 0
                    )


                    echo Tomcat port is not ready yet.


                    set /a RETRIES-=1


                    if %RETRIES% LEQ 0 (

                        echo.
                        echo ==========================================
                        echo APPZILLON FAILED TO START
                        echo ==========================================


                        echo.
                        echo ==========================================
                        echo TOMCAT PORT STATUS
                        echo ==========================================

                        netstat -ano | findstr ":8086"


                        echo.
                        echo ==========================================
                        echo TOMCAT LOGS
                        echo ==========================================

                        if exist "%TOMCAT_HOME%\\logs" (
                            dir "%TOMCAT_HOME%\\logs"
                        )


                        exit /b 1
                    )


                    echo.
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
            echo 'http://localhost:8080/api/user'

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