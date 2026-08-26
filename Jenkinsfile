pipeline {

    agent any

    triggers {
        pollSCM('* * * * *')
    }

    environment {

        // ============================================================
        // JAVA
        // ============================================================

        JAVA_HOME = 'C:/Program Files/Java/jdk-17.0.2'

        // ============================================================
        // BACKEND
        // ============================================================

        APP_JAR = 'target/quizzapp.jar'

        BACKEND_PORT = '8080'

        BACKEND_URL = 'http://localhost:8080/api/user/getQuizzes'

        // ============================================================
        // TOMCAT / APPZILLON
        // ============================================================

        APPZ_HOME = 'D:/Freshers_Software/Softwarepath/apache-tomcat-9.0.53'

        APPZ_ARTIFACTS = 'C:/Users/suvarna.lnu/Downloads/jenkins'

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
                echo 'CHECKING OUT QUIZZAPP'
                echo '=========================================='

                checkout scm

                echo 'QUIZZAPP CHECKOUT SUCCESSFUL'
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
                echo JAVA VERSION
                echo ==========================================

                set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                set "PATH=%JAVA_HOME%\\bin;%PATH%"

                java -version

                echo.
                echo ==========================================
                echo MAVEN VERSION
                echo ==========================================

                mvn -version

                echo.
                echo ==========================================
                echo BUILDING SPRING BOOT BACKEND
                echo ==========================================

                mvn clean package -DskipTests

                if errorlevel 1 (

                    echo.
                    echo ==========================================
                    echo BACKEND BUILD FAILED
                    echo ==========================================

                    exit /b 1
                )

                echo.
                echo ==========================================
                echo CHECKING JAR
                echo ==========================================

                if not exist "%APP_JAR%" (

                    echo ERROR: JAR NOT FOUND
                    echo Expected:
                    echo %APP_JAR%

                    exit /b 1
                )

                echo.
                echo BACKEND JAR FOUND
                echo %APP_JAR%

                dir "%APP_JAR%"

                echo.
                echo BACKEND BUILD SUCCESSFUL

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
                echo STOPPING OLD BACKEND
                echo ==========================================

                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%BACKEND_PORT% ^| findstr LISTENING') do (

                    echo Found process %%a on port %BACKEND_PORT%

                    tasklist /FI "PID eq %%a"

                    taskkill /F /PID %%a >nul 2>&1
                )

                timeout /t 3 /nobreak >nul


                echo.
                echo ==========================================
                echo CHECKING BACKEND PORT
                echo ==========================================

                netstat -ano | findstr :%BACKEND_PORT% | findstr LISTENING

                if not errorlevel 1 (

                    echo ERROR: Port %BACKEND_PORT% is still in use.

                    exit /b 1
                )

                echo Port %BACKEND_PORT% is available.


                echo.
                echo ==========================================
                echo VERIFYING JAR
                echo ==========================================

                if not exist "%APP_JAR%" (

                    echo ERROR: Backend JAR not found.

                    exit /b 1
                )

                echo JAR FOUND.


                echo.
                echo ==========================================
                echo STARTING SPRING BOOT BACKEND
                echo ==========================================

                set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                set "PATH=%JAVA_HOME%\\bin;%PATH%"

                set "JENKINS_NODE_COOKIE=dontKillMe"


                if exist backend.log (

                    del /F /Q backend.log >nul 2>&1
                )


                start "QuizBackend" /B cmd /c "set JENKINS_NODE_COOKIE=dontKillMe && java -jar %APP_JAR% > backend.log 2>&1"


                echo Backend start command executed.


                echo.
                echo ==========================================
                echo WAITING FOR BACKEND
                echo ==========================================

                timeout /t 5 /nobreak >nul


                echo.
                echo ==========================================
                echo CHECKING BACKEND PORT
                echo ==========================================

                netstat -ano | findstr :%BACKEND_PORT% | findstr LISTENING

                if errorlevel 1 (

                    echo.
                    echo ==========================================
                    echo BACKEND FAILED TO START
                    echo ==========================================

                    echo.

                    if exist backend.log (

                        echo ==========================================
                        echo BACKEND LOG
                        echo ==========================================

                        type backend.log

                    ) else (

                        echo backend.log not found.

                    )

                    exit /b 1
                )


                echo.
                echo ==========================================
                echo BACKEND STARTED SUCCESSFULLY
                echo ==========================================

                echo Backend Port: %BACKEND_PORT%
                echo Backend URL: %BACKEND_URL%

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
                echo BACKEND HEALTH CHECK
                echo ==========================================

                echo URL:
                echo %BACKEND_URL%

                setlocal EnableDelayedExpansion

                set RETRIES=20


                :BACKEND_CHECK

                echo.
                echo Checking backend...
                echo Attempts remaining: !RETRIES!


                curl -s -o nul -w "%%{http_code}" "%BACKEND_URL%" | findstr "200 201" >nul


                if not errorlevel 1 (

                    echo.
                    echo ==========================================
                    echo BACKEND IS UP
                    echo ==========================================

                    echo URL:
                    echo %BACKEND_URL%

                    exit /b 0
                )


                echo Backend not ready.

                set /a RETRIES-=1


                if !RETRIES! LEQ 0 (

                    echo.
                    echo ==========================================
                    echo BACKEND HEALTH CHECK FAILED
                    echo ==========================================

                    echo.

                    if exist backend.log (

                        echo ==========================================
                        echo BACKEND LOG
                        echo ==========================================

                        type backend.log

                    ) else (

                        echo backend.log not found.

                    )

                    exit /b 1
                )


                echo Waiting 3 seconds...

                timeout /t 3 /nobreak >nul

                goto BACKEND_CHECK

                '''
            }
        }


        // ============================================================
        // DEPLOY APPZILLON / TOMCAT
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
                echo CHECKING WAR FILES
                echo ==========================================


                if not exist "%APPZ_ARTIFACTS%\\AppzillonServer.war" (

                    echo ERROR:
                    echo AppzillonServer.war NOT FOUND

                    echo Expected:
                    echo %APPZ_ARTIFACTS%\\AppzillonServer.war

                    exit /b 1
                )

                echo AppzillonServer.war found.


                if not exist "%APPZ_ARTIFACTS%\\quizzapp.war" (

                    echo ERROR:
                    echo quizzapp.war NOT FOUND

                    echo Expected:
                    echo %APPZ_ARTIFACTS%\\quizzapp.war

                    exit /b 1
                )

                echo quizzapp.war found.


                echo.
                echo ==========================================
                echo CHECKING TOMCAT
                echo ==========================================


                if not exist "%APPZ_HOME%\\bin\\catalina.bat" (

                    echo ERROR:
                    echo catalina.bat NOT FOUND

                    echo Expected:
                    echo %APPZ_HOME%\\bin\\catalina.bat

                    exit /b 1
                )

                echo Tomcat installation found.


                echo.
                echo ==========================================
                echo CHECKING TOMCAT PORT
                echo ==========================================


                setlocal EnableDelayedExpansion

                set TOMCAT_RUNNING=0


                netstat -ano | findstr :%TOMCAT_PORT% | findstr LISTENING >nul

                if !ERRORLEVEL!==0 (

                    set TOMCAT_RUNNING=1
                )


                echo Tomcat running: !TOMCAT_RUNNING!


                echo.
                echo ==========================================
                echo CHECKING WAR CHANGES
                echo ==========================================


                set CHANGED=0


                if not exist "%APPZ_HOME%\\webapps\\AppzillonServer.war" (

                    set CHANGED=1
                )


                if not exist "%APPZ_HOME%\\webapps\\quizzapp.war" (

                    set CHANGED=1
                )


                rem ==================================================
                rem APPZILLON SERVER MD5
                rem ==================================================


                for /f %%i in ('powershell -NoProfile -Command "(Get-FileHash -Algorithm MD5 ''%APPZ_ARTIFACTS%/AppzillonServer.war'').Hash"') do set SRC1=%%i


                if exist "%APPZ_HOME%\\webapps\\AppzillonServer.war" (

                    for /f %%i in ('powershell -NoProfile -Command "(Get-FileHash -Algorithm MD5 ''%APPZ_HOME%/webapps/AppzillonServer.war'').Hash"') do set DST1=%%i

                ) else (

                    set DST1=NOTFOUND
                )


                echo AppzillonServer Source MD5:
                echo !SRC1!

                echo AppzillonServer Destination MD5:
                echo !DST1!


                if not "!SRC1!"=="!DST1!" (

                    echo AppzillonServer.war has changed.

                    set CHANGED=1
                )


                rem ==================================================
                rem QUIZZAPP MD5
                rem ==================================================


                for /f %%i in ('powershell -NoProfile -Command "(Get-FileHash -Algorithm MD5 ''%APPZ_ARTIFACTS%/quizzapp.war'').Hash"') do set SRC2=%%i


                if exist "%APPZ_HOME%\\webapps\\quizzapp.war" (

                    for /f %%i in ('powershell -NoProfile -Command "(Get-FileHash -Algorithm MD5 ''%APPZ_HOME%/webapps/quizzapp.war'').Hash"') do set DST2=%%i

                ) else (

                    set DST2=NOTFOUND
                )


                echo quizzapp Source MD5:
                echo !SRC2!

                echo quizzapp Destination MD5:
                echo !DST2!


                if not "!SRC2!"=="!DST2!" (

                    echo quizzapp.war has changed.

                    set CHANGED=1
                )


                echo.
                echo ==========================================
                echo DEPLOYMENT STATUS
                echo ==========================================

                echo CHANGED:
                echo !CHANGED!

                echo TOMCAT_RUNNING:
                echo !TOMCAT_RUNNING!


                rem ==================================================
                rem IF NOTHING CHANGED AND TOMCAT IS RUNNING
                rem ==================================================


                if "!CHANGED!"=="0" if "!TOMCAT_RUNNING!"=="1" (

                    echo.
                    echo ==========================================
                    echo WARS UNCHANGED
                    echo TOMCAT ALREADY RUNNING
                    echo SKIPPING REDEPLOYMENT
                    echo ==========================================

                    exit /b 0
                )


                rem ==================================================
                rem STOP TOMCAT
                rem ==================================================


                echo.
                echo ==========================================
                echo STOPPING TOMCAT
                echo ==========================================


                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%TOMCAT_PORT% ^| findstr LISTENING') do (

                    echo Killing Tomcat process %%a

                    taskkill /F /PID %%a >nul 2>&1
                )


                timeout /t 3 /nobreak >nul


                netstat -ano | findstr :%TOMCAT_PORT% | findstr LISTENING >nul

                if not errorlevel 1 (

                    echo ERROR:
                    echo Tomcat port %TOMCAT_PORT% is still in use.

                    exit /b 1
                )


                echo Tomcat stopped.


                rem ==================================================
                rem REMOVE OLD APPLICATIONS
                rem ==================================================


                echo.
                echo ==========================================
                echo REMOVING OLD DEPLOYMENT
                echo ==========================================


                rmdir /S /Q "%APPZ_HOME%\\webapps\\AppzillonServer" >nul 2>&1

                rmdir /S /Q "%APPZ_HOME%\\webapps\\quizzapp" >nul 2>&1


                del /F /Q "%APPZ_HOME%\\webapps\\AppzillonServer.war" >nul 2>&1

                del /F /Q "%APPZ_HOME%\\webapps\\quizzapp.war" >nul 2>&1


                rem ==================================================
                rem COPY WAR FILES
                rem ==================================================


                echo.
                echo ==========================================
                echo COPYING APPZILLON SERVER
                echo ==========================================


                copy /Y "%APPZ_ARTIFACTS%\\AppzillonServer.war" "%APPZ_HOME%\\webapps\\AppzillonServer.war"


                if errorlevel 1 (

                    echo ERROR:
                    echo Failed to copy AppzillonServer.war

                    exit /b 1
                )


                echo AppzillonServer.war copied successfully.


                echo.
                echo ==========================================
                echo COPYING QUIZZAPP
                echo ==========================================


                copy /Y "%APPZ_ARTIFACTS%\\quizzapp.war" "%APPZ_HOME%\\webapps\\quizzapp.war"


                if errorlevel 1 (

                    echo ERROR:
                    echo Failed to copy quizzapp.war

                    exit /b 1
                )


                echo quizzapp.war copied successfully.


                rem ==================================================
                rem START TOMCAT
                rem ==================================================


                echo.
                echo ==========================================
                echo STARTING TOMCAT
                echo ==========================================


                set "JAVA_HOME=C:\\Program Files\\Java\\jdk-17.0.2"
                set "PATH=%JAVA_HOME%\\bin;%PATH%"
                set "CATALINA_HOME=%APPZ_HOME%"
                set "JENKINS_NODE_COOKIE=dontKillMe"


                if exist "%APPZ_HOME%\\logs\\jenkins-run.log" (

                    del /F /Q "%APPZ_HOME%\\logs\\jenkins-run.log" >nul 2>&1
                )


                start "QuizTomcat" /B cmd /c "call "%APPZ_HOME%/bin/catalina.bat" run > "%APPZ_HOME%/logs/jenkins-run.log" 2>&1"


                echo Tomcat start command executed.


                echo.
                echo ==========================================
                echo WAITING FOR TOMCAT
                echo ==========================================


                timeout /t 10 /nobreak >nul


                echo.
                echo ==========================================
                echo TOMCAT PORT STATUS
                echo ==========================================


                netstat -ano | findstr :%TOMCAT_PORT% | findstr LISTENING


                if errorlevel 1 (

                    echo WARNING:
                    echo Tomcat port %TOMCAT_PORT% is not listening yet.

                ) else (

                    echo Tomcat port %TOMCAT_PORT% is listening.
                )


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
                echo APPZILLON HEALTH CHECK
                echo ==========================================

                echo URL:
                echo %APPZILLON_URL%

                setlocal EnableDelayedExpansion

                set RETRIES=45


                :APPZILLON_CHECK

                echo.
                echo Checking Appzillon...
                echo Attempts remaining: !RETRIES!


                curl -s -o nul -w "%%{http_code}" "%APPZILLON_URL%" | findstr "200 302 404" >nul


                if not errorlevel 1 (

                    echo.
                    echo ==========================================
                    echo APPZILLON IS UP
                    echo ==========================================

                    echo URL:
                    echo %APPZILLON_URL%

                    exit /b 0
                )


                echo Appzillon not ready.

                set /a RETRIES-=1


                if !RETRIES! LEQ 0 (

                    echo.
                    echo ==========================================
                    echo APPZILLON HEALTH CHECK FAILED
                    echo ==========================================

                    echo.

                    echo TOMCAT PORT STATUS:
                    netstat -ano | findstr :%TOMCAT_PORT%


                    echo.

                    echo ==========================================
                    echo TOMCAT LOG
                    echo ==========================================


                    if exist "%APPZ_HOME%\\logs\\jenkins-run.log" (

                        powershell -NoProfile -Command "Get-Content '%APPZ_HOME%\\logs\\jenkins-run.log' -Tail 50"

                    ) else (

                        echo Tomcat log not found.

                    )

                    exit /b 1
                )


                echo Waiting 5 seconds...

                timeout /t 5 /nobreak >nul

                goto APPZILLON_CHECK

                '''
            }
        }
    }


    // ================================================================
    // POST ACTIONS
    // ================================================================

    post {

        success {

            echo '=========================================='
            echo 'QUIZZAPP DEPLOYMENT SUCCESSFUL'
            echo '=========================================='

            echo 'Jenkins:'
            echo 'http://localhost:8090'

            echo 'Backend:'
            echo 'http://localhost:8080'

            echo 'Appzillon:'
            echo 'http://localhost:8086/quizzapp/'

            echo '=========================================='
        }


        failure {

            echo '=========================================='
            echo 'QUIZZAPP DEPLOYMENT FAILED'
            echo '=========================================='

            echo 'Check Jenkins console output.'
            echo 'Check backend.log.'
            echo 'Check Tomcat jenkins-run.log.'

            echo '=========================================='
        }
    }
}
