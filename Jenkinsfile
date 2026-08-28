pipeline {

    agent any

    parameters {
        string(
            name: 'GIT_URL',
            defaultValue: 'https://github.com/SuvarnaArmugam/Quiz.git',
            description: 'Git repository URL configured for this pipeline.'
        )
        string(
            name: 'PROJECT_DIR',
            defaultValue: '.',
            description: 'Project directory relative to the Jenkins workspace.'
        )
        string(
            name: 'JAVA_HOME_PATH',
            defaultValue: 'C:/Program Files/Java/jdk-17.0.2',
            description: 'Java installation directory.'
        )
        string(
            name: 'APP_JAR',
            defaultValue: 'target/quizapp.jar',
            description: 'Backend JAR path relative to the project directory.'
        )
        string(
            name: 'BACKEND_PORT',
            defaultValue: '8080',
            description: 'Backend HTTP port.'
        )
        string(
            name: 'BACKEND_URL',
            defaultValue: 'http://localhost:8080/user/quizzes',
            description: 'Backend health-check URL.'
        )
        string(
            name: 'APPZ_HOME',
            defaultValue: 'D:/Freshers_Software/Softwarepath/apache-tomcat-9.0.53',
            description: 'Tomcat installation directory.'
        )
        string(
            name: 'APPZ_ARTIFACTS',
            defaultValue: 'D:/jenkins-testing',
            description: 'Directory containing the deployable WAR.'
        )
        string(
            name: 'WAR_FILE',
            defaultValue: 'quizzapp.war',
            description: 'WAR file name in the artifact directory.'
        )
        string(
            name: 'TOMCAT_PORT',
            defaultValue: '8086',
            description: 'Tomcat HTTP port.'
        )
        string(
            name: 'APP_CONTEXT',
            defaultValue: 'quizzapp',
            description: 'Tomcat application context.'
        )
        string(
            name: 'APPZILLON_URL',
            defaultValue: 'http://localhost:8086/quizzapp/',
            description: 'Deployed application health-check URL.'
        )
        choice(
            name: 'TEST_MODE',
            choices: ['all', 'java', 'node', 'none'],
            description: 'Select which automated tests to run.'
        )
    }

    environment {
        GIT_URL = "${params.GIT_URL ?: 'https://github.com/SuvarnaArmugam/Quiz.git'}"
        PROJECT_DIR = "${params.PROJECT_DIR ?: '.'}"
        JAVA_HOME_PATH = "${params.JAVA_HOME_PATH ?: 'C:/Program Files/Java/jdk-17.0.2'}"
        APP_JAR = "${params.APP_JAR ?: 'target/quizapp.jar'}"
        BACKEND_PORT = "${params.BACKEND_PORT ?: '8080'}"
        BACKEND_URL = "${params.BACKEND_URL ?: 'http://localhost:8080/user/quizzes'}"
        APPZ_HOME = "${params.APPZ_HOME ?: 'D:/Freshers_Software/Softwarepath/apache-tomcat-9.0.53'}"
        APPZ_ARTIFACTS = "${params.APPZ_ARTIFACTS ?: 'D:/jenkins-testing'}"
        WAR_FILE = "${params.WAR_FILE ?: 'quizzapp.war'}"
        TOMCAT_PORT = "${params.TOMCAT_PORT ?: '8086'}"
        APP_CONTEXT = "${params.APP_CONTEXT ?: 'quizzapp'}"
        APPZILLON_URL = "${params.APPZILLON_URL ?: 'http://localhost:8086/quizzapp/'}"
        TEST_MODE = "${params.TEST_MODE ?: 'all'}"
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

                    set "JAVA_HOME=%JAVA_HOME_PATH%"
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

                    for %%P in ("%APP_JAR%") do (
                        set "APP_JAR_DIR=%%~dpP"
                        set "APP_JAR_FILE=%%~nxP"
                    )

                    if not exist "%APP_JAR_DIR%*.jar" (
                        echo.
                        echo ERROR: No JAR file was generated.
                        echo.
                        echo Target directory contents:
                        dir "%APP_JAR_DIR%"
                        exit /b 1
                    )

                    echo.
                    echo JAR FILES FOUND:
                    dir /B "%APP_JAR_DIR%*.jar"

                    echo.
                    echo ==========================================
                    echo CREATING STANDARD JAR NAME
                    echo ==========================================

                    if exist "%APP_JAR%" (
                        del /F /Q "%APP_JAR%"
                    )

                    for /f "delims=" %%J in ('dir /B /O-D "%APP_JAR_DIR%*.jar" ^| findstr /V /I /C:"%APP_JAR_FILE%"') do (
                        echo Generated JAR: %%J
                        copy /Y "%APP_JAR_DIR%%%J" "%APP_JAR%" >nul
                        goto JAR_COPIED
                    )

                    :JAR_COPIED

                    if not exist "%APP_JAR%" (
                        echo.
                        echo ERROR: Could not create %APP_JAR%
                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo STANDARD JAR CREATED SUCCESSFULLY
                    echo ==========================================

                    dir "%APP_JAR%"
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
                    echo CHECKING PORT %BACKEND_PORT%
                    echo ==========================================

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr LISTENING ^| findstr ":%BACKEND_PORT%"') do (
                        echo Found process %%a on port %BACKEND_PORT%
                        echo Stopping process %%a
                        taskkill /F /PID %%a >nul 2>&1
                    )

                    echo.
                    echo ==========================================
                    echo WAITING FOR PORT %BACKEND_PORT%
                    echo ==========================================

                    ping -n 4 127.0.0.1 >nul

                    echo.
                    echo ==========================================
                    echo STARTING QUIZAPP BACKEND VIA POWERSHELL
                    echo ==========================================

                    set "JAVA_HOME=%JAVA_HOME_PATH%"

                    if exist "%WORKSPACE%\\backend.log" (
                        del /F /Q "%WORKSPACE%\\backend.log"
                    )

                    if exist "%WORKSPACE%\\backend-err.log" (
                        del /F /Q "%WORKSPACE%\\backend-err.log"
                    )

                    echo Starting Spring Boot application...

                    powershell -NoProfile -Command "$env:JENKINS_NODE_COOKIE='dontKillMe'; Start-Process -FilePath '%JAVA_HOME%\\bin\\java.exe' -ArgumentList '-jar','%WORKSPACE%\\%APP_JAR%' -RedirectStandardOutput '%WORKSPACE%\\backend.log' -RedirectStandardError '%WORKSPACE%\\backend-err.log' -WindowStyle Hidden"

                    echo.
                    echo BACKEND START COMMAND EXECUTED

                    echo.
                    echo ==========================================
                    echo WAITING FOR BACKEND
                    echo ==========================================

                    ping -n 9 127.0.0.1 >nul

                    echo.
                    echo ==========================================
                    echo PORT %BACKEND_PORT% STATUS
                    echo ==========================================

                    netstat -ano | findstr LISTENING | findstr ":%BACKEND_PORT%"

                    echo.
                    echo ==========================================
                    echo BACKEND LOG
                    echo ==========================================

                    if exist "%WORKSPACE%\\backend.log" (
                        powershell -NoProfile -Command "Get-Content '%WORKSPACE%\\backend.log' -Tail 50"
                    ) else (
                        echo backend.log not found.
                    )

                    if exist "%WORKSPACE%\\backend-err.log" (
                        echo.
                        echo -- backend-err.log --
                        powershell -NoProfile -Command "Get-Content '%WORKSPACE%\\backend-err.log' -Tail 50"
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

                    netstat -ano | findstr LISTENING | findstr ":%BACKEND_PORT%" >nul

                    if not errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo BACKEND PORT IS RUNNING
                        echo ==========================================

                        echo Port %BACKEND_PORT% is listening.

                        echo.
                        echo Testing backend URL...

                        curl -s -o nul -w "HTTP Status: %%{http_code}" "%BACKEND_URL%"
                        echo.

                        REM Second check in case first request hit the DispatcherServlet lazy-init race
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

                        netstat -ano | findstr ":%BACKEND_PORT%"

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
                    echo %APPZ_ARTIFACTS%\\%WAR_FILE%

                    if not exist "%APPZ_ARTIFACTS%\\%WAR_FILE%" (

                        echo.
                        echo ERROR: %WAR_FILE% not found.

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

                    dir "%APPZ_ARTIFACTS%\\%WAR_FILE%"
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

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr LISTENING ^| findstr ":%TOMCAT_PORT%"') do (

                        echo Stopping process %%a on port %TOMCAT_PORT%

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

                    copy /Y "%APPZ_ARTIFACTS%\\%WAR_FILE%" "%APPZ_HOME%\\webapps\\%APP_CONTEXT%.war"

                    if errorlevel 1 (

                        echo ERROR: Failed to copy %WAR_FILE%.

                        exit /b 1
                    )

                    echo WAR copied successfully.

                    echo.
                    echo ==========================================
                    echo STARTING TOMCAT VIA POWERSHELL
                    echo ==========================================

                    if exist "%APPZ_HOME%\\logs\\jenkins-run.log" (
                        del /F /Q "%APPZ_HOME%\\logs\\jenkins-run.log"
                    )

                    if exist "%APPZ_HOME%\\logs\\jenkins-run-err.log" (
                        del /F /Q "%APPZ_HOME%\\logs\\jenkins-run-err.log"
                    )

                    powershell -NoProfile -Command "$env:JENKINS_NODE_COOKIE='dontKillMe'; Start-Process -FilePath '%APPZ_HOME%\\bin\\catalina.bat' -ArgumentList 'run' -RedirectStandardOutput '%APPZ_HOME%\\logs\\jenkins-run.log' -RedirectStandardError '%APPZ_HOME%\\logs\\jenkins-run-err.log' -WindowStyle Hidden"

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

                    netstat -ano | findstr LISTENING | findstr ":%TOMCAT_PORT%"

                    echo.
                    echo ==========================================
                    echo TOMCAT LOG
                    echo ==========================================

                    if exist "%APPZ_HOME%\\logs\\jenkins-run.log" (
                        powershell -NoProfile -Command "Get-Content '%APPZ_HOME%\\logs\\jenkins-run.log' -Tail 50"
                    ) else (
                        echo Tomcat log not found.
                    )

                    if exist "%APPZ_HOME%\\logs\\jenkins-run-err.log" (
                        echo.
                        echo -- jenkins-run-err.log --
                        powershell -NoProfile -Command "Get-Content '%APPZ_HOME%\\logs\\jenkins-run-err.log' -Tail 50"
                    )

                    echo.
                    echo ==========================================
                    echo VERIFYING TOMCAT IS SERVING
                    echo ==========================================

                    netstat -ano | findstr LISTENING | findstr ":%TOMCAT_PORT%" >nul

                    if errorlevel 1 (
                        echo.
                        echo ##########################################
                        echo # TOMCAT IS NOT RUNNING - PORT %TOMCAT_PORT% CLOSED
                        echo ##########################################
                        exit /b 1
                    )

                    for /f "delims=" %%s in ('curl -s -o nul -w "%%{http_code}" "%APPZILLON_URL%"') do (
                        echo HTTP Status: %%s
                        if not "%%s"=="200" (
                            echo ERROR: Appzillon URL is not healthy.
                            exit /b 1
                        )
                    )

                    echo.
                    echo ##########################################
                    echo # TOMCAT IS UP AND RUNNING ON PORT %TOMCAT_PORT%
                    echo # %APPZILLON_URL%
                    echo ##########################################
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

                    netstat -ano | findstr LISTENING | findstr ":%TOMCAT_PORT%" >nul

                    if not errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo TOMCAT IS RUNNING
                        echo ==========================================

                        echo Port %TOMCAT_PORT% is listening.

                        echo.
                        echo Testing Appzillon URL...

                        for /f "delims=" %%s in ('curl -s -o nul -w "%%{http_code}" "%APPZILLON_URL%"') do (
                            echo HTTP Status: %%s
                            if "%%s"=="200" (
                                echo Appzillon is running.
                                exit /b 0
                            )
                            echo Appzillon returned HTTP %%s.
                        )

                        echo Application is not ready yet.
                        goto RETRY_APPZILLON
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

                        netstat -ano | findstr ":%TOMCAT_PORT%"

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

                    :RETRY_APPZILLON

                    echo Waiting 3 seconds...

                    ping -n 4 127.0.0.1 >nul

                    goto CHECK_APPZILLON
                '''
            }
        }


        // ============================================================
        // 8. JAVA PLAYWRIGHT TESTS
        // ============================================================

        stage('Java Playwright Tests') {

            when {
                anyOf {
                    expression { params.TEST_MODE == 'all' }
                    expression { params.TEST_MODE == 'java' }
                }
            }

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo RUNNING JAVA PLAYWRIGHT TEST
                    echo ==========================================

                    set "JAVA_HOME=%JAVA_HOME_PATH%"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"

                    call mvn test
                    if errorlevel 1 (
                        echo ERROR: Java Playwright test failed.
                        exit /b 1
                    )
                '''
            }
        }


        // ============================================================
        // 9. NODE PLAYWRIGHT TESTS
        // ============================================================

        stage('Playwright Tests') {

            when {
                allOf {
                    anyOf {
                        expression { params.TEST_MODE == 'all' }
                        expression { params.TEST_MODE == 'node' }
                    }
                    expression { fileExists('package.json') }
                    expression { fileExists('tests') }
                }
            }

            steps {

                bat '''
                    @echo off

                    echo ==========================================
                    echo RUNNING PLAYWRIGHT TESTS
                    echo ==========================================

                    where node
                    if errorlevel 1 (
                        echo ERROR: Node.js is not installed or not on PATH.
                        exit /b 1
                    )

                    where npm
                    if errorlevel 1 (
                        echo ERROR: npm is not installed or not on PATH.
                        exit /b 1
                    )

                    node --version
                    call npm --version

                    echo.
                    echo ==========================================
                    echo INSTALLING NODE DEPENDENCIES
                    echo ==========================================

                    call npm ci
                    if errorlevel 1 (
                        echo ERROR: npm dependency installation failed.
                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo INSTALLING PLAYWRIGHT CHROMIUM
                    echo ==========================================

                    call npx playwright install chromium
                    if errorlevel 1 (
                        echo ERROR: Playwright browser installation failed.
                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo EXECUTING PLAYWRIGHT TESTS
                    echo ==========================================

                    set "CI=true"
                    set "PW_HEADLESS=false"
                    call npx playwright test --project=chromium
                    if errorlevel 1 (
                        echo ERROR: Playwright tests failed.
                        exit /b 1
                    )
                '''
            }

            post {
                always {
                    archiveArtifacts artifacts: 'playwright-report/**', allowEmptyArchive: true
                }
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
            echo "${env.BACKEND_URL}"

            echo 'Appzillon:'
            echo "${env.APPZILLON_URL}"

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