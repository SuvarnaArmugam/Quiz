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
        // ==================================================

        TOMCAT_HOME = "D:\\Freshers_Software\\Softwarepath\\apache-tomcat-9.0.53"


        // ==================================================
        // APPZILLON WAR
        // ==================================================

       APPSERVER_WAR = "C:\\Users\\suvarna.lnu\\Downloads\\jenkins\\quizzapp.war"


        // ==================================================
        // APPZILLON
        // ==================================================

        APP_CONTEXT = "quizzapp"

        // Tomcat/Appzillon port
        // Backend = 8080
        // Tomcat   = 8086

        APPZILLON_PORT = "8086"
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

                    if exist pom.xml (
                        echo pom.xml FOUND
                    ) else (
                        echo ERROR: pom.xml NOT FOUND
                        exit /b 1
                    )

                    echo.
                    echo Searching for source files:

                    if exist src (
                        echo src directory FOUND
                    ) else (
                        echo ERROR: src directory NOT FOUND
                        exit /b 1
                    )

                    echo.
                    echo Checking for existing JAR files:

                    if exist target\\*.jar (
                        dir /s /b target\\*.jar
                    ) else (
                        echo No JAR found yet - this is OK.
                        echo JAR will be created during Build Backend.
                    )

                    echo.
                    echo Checking for existing WAR files:

                    if exist target\\*.war (
                        dir /s /b target\\*.war
                    ) else (
                        echo No WAR found yet - this is OK.
                    )

                    echo.
                    echo Workspace check completed successfully.

                    echo ==========================================
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
                        echo ==========================================
                        echo ERROR: MAVEN BUILD FAILED
                        echo ==========================================
                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo MAVEN BUILD SUCCESSFUL
                    echo ==========================================

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
        // 4. STOP EXISTING BACKEND
        // ==================================================

        stage('Stop Existing Backend') {

            steps {

                bat '''
                    echo ==========================================
                    echo STOPPING EXISTING BACKEND
                    echo ==========================================

                    echo Checking port %BACKEND_PORT%...

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%BACKEND_PORT% ^| findstr LISTENING') do (

                        echo Found process %%a using port %BACKEND_PORT%

                        taskkill /PID %%a /F
                    )

                    echo.
                    echo Checking port again:

                    netstat -ano | findstr :%BACKEND_PORT%

                    echo.
                    echo Backend port %BACKEND_PORT% is available.
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

                    echo.
                    echo Removing previous backend log...

                    if exist backend.log (
                        del /f /q backend.log
                    )

                    echo.
                    echo Starting Spring Boot backend...

                    start "QuizApp Backend" /B cmd /c "java -jar target\\quizapp.jar > backend.log 2>&1"

                    echo.
                    echo Backend process started.

                    echo.
                    echo Waiting for Spring Boot startup...

                    powershell -NoProfile -ExecutionPolicy Bypass -Command "Start-Sleep -Seconds 10"

                    echo.
                    echo Backend log:

                    if exist backend.log (
                        type backend.log
                    ) else (
                        echo backend.log not created yet.
                    )

                    echo.
                    echo ==========================================
                    echo BACKEND START COMMAND COMPLETED
                    echo ==========================================
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

                    echo Backend:
                    echo http://localhost:%BACKEND_PORT%

                    echo.
                    echo Checking port %BACKEND_PORT%...

                    netstat -ano | findstr :%BACKEND_PORT%

                    echo.
                    echo Waiting for Spring Boot health endpoint...

                    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
                        "$healthy=$false; " ^
                        "for($i=1; $i -le 30; $i++) { " ^
                        "    Write-Host ('Health check attempt ' + $i + ' of 30...'); " ^
                        "    try { " ^
                        "        $response=Invoke-WebRequest -Uri 'http://localhost:%BACKEND_PORT%/actuator/health' -UseBasicParsing -TimeoutSec 5; " ^
                        "        if($response.StatusCode -eq 200) { " ^
                        "            Write-Host 'BACKEND IS HEALTHY'; " ^
                        "            Write-Host $response.Content; " ^
                        "            $healthy=$true; " ^
                        "            break; " ^
                        "        } " ^
                        "    } catch { " ^
                        "        Write-Host 'Backend is not ready yet...'; " ^
                        "    } " ^
                        "    Start-Sleep -Seconds 2; " ^
                        "} " ^
                        "if(-not $healthy) { exit 1 }"

                    if errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo BACKEND HEALTH CHECK FAILED
                        echo ==========================================

                        echo.
                        echo Port status:

                        netstat -ano | findstr :%BACKEND_PORT%

                        echo.
                        echo Backend log:

                        if exist backend.log (
                            type backend.log
                        ) else (
                            echo backend.log not found.
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

                    echo.

                    if exist "%APPSERVER_WAR%" (

                        echo ==========================================
                        echo APPZILLON WAR FOUND
                        echo ==========================================

                        dir "%APPSERVER_WAR%"

                    ) else (

                        echo ==========================================
                        echo ERROR: APPZILLON WAR NOT FOUND
                        echo ==========================================

                        echo Expected:
                        echo %APPSERVER_WAR%

                        echo.
                        echo Current Workspace:

                        dir

                        echo.
                        echo Searching entire workspace for WAR:

                        dir /s /b *.war

                        exit /b 1
                    )

                    echo.
                    echo Appzillon WAR verification successful.
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

                    echo Tomcat:
                    echo %TOMCAT_HOME%

                    if exist "%TOMCAT_HOME%\\bin\\shutdown.bat" (

                        echo Running Tomcat shutdown...

                        call "%TOMCAT_HOME%\\bin\\shutdown.bat"

                        echo.
                        echo Tomcat shutdown command executed.

                        echo.
                        echo Waiting for Tomcat to stop...

                        powershell -NoProfile -ExecutionPolicy Bypass -Command "Start-Sleep -Seconds 5"

                    ) else (

                        echo WARNING: Tomcat shutdown.bat not found.

                        echo Expected:
                        echo %TOMCAT_HOME%\\bin\\shutdown.bat
                    )

                    echo.
                    echo Tomcat stop stage completed.
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

                        echo Expected:
                        echo %TOMCAT_HOME%\\webapps

                        exit /b 1
                    )

                    echo.
                    echo Removing previous application...

                    if exist "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%" (

                        echo Removing old exploded application...

                        rmdir /S /Q "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%"
                    )

                    if exist "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%.war" (

                        echo Removing old WAR...

                        del /F /Q "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%.war"
                    )

                    echo.
                    echo Copying new Appzillon WAR...

                    copy /Y "%APPSERVER_WAR%" "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%.war"

                    if errorlevel 1 (

                        echo ==========================================
                        echo ERROR: FAILED TO COPY APPZILLON WAR
                        echo ==========================================

                        exit /b 1
                    )

                    echo.
                    echo WAR copied successfully.

                    dir "%TOMCAT_HOME%\\webapps\\%APP_CONTEXT%.war"

                    echo.
                    echo ==========================================
                    echo STARTING TOMCAT
                    echo ==========================================

                    call "%TOMCAT_HOME%\\bin\\startup.bat"

                    echo.
                    echo Tomcat startup command executed.

                    echo.
                    echo Waiting for Tomcat startup...

                    powershell -NoProfile -ExecutionPolicy Bypass -Command "Start-Sleep -Seconds 10"

                    echo.
                    echo ==========================================
                    echo TOMCAT STARTUP COMMAND COMPLETED
                    echo ==========================================
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

                    echo Appzillon URL:
                    echo http://localhost:%APPZILLON_PORT%/%APP_CONTEXT%/

                    echo.
                    echo Checking Tomcat port %APPZILLON_PORT%...

                    netstat -ano | findstr :%APPZILLON_PORT%

                    echo.
                    echo Waiting for Appzillon...

                    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
                        "$healthy=$false; " ^
                        "for($i=1; $i -le 30; $i++) { " ^
                        "    Write-Host ('Appzillon health check attempt ' + $i + ' of 30...'); " ^
                        "    try { " ^
                        "        $response=Invoke-WebRequest -Uri 'http://localhost:%APPZILLON_PORT%/%APP_CONTEXT%/' -UseBasicParsing -TimeoutSec 5; " ^
                        "        if($response.StatusCode -ge 200 -and $response.StatusCode -lt 500) { " ^
                        "            Write-Host 'APPZILLON IS RUNNING'; " ^
                        "            $healthy=$true; " ^
                        "            break; " ^
                        "        } " ^
                        "    } catch { " ^
                        "        Write-Host 'Appzillon is not ready yet...'; " ^
                        "    } " ^
                        "    Start-Sleep -Seconds 2; " ^
                        "} " ^
                        "if(-not $healthy) { exit 1 }"

                    if errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo APPZILLON HEALTH CHECK FAILED
                        echo ==========================================

                        echo.
                        echo Tomcat port status:

                        netstat -ano | findstr :%APPZILLON_PORT%

                        echo.
                        echo Tomcat logs:

                        if exist "%TOMCAT_HOME%\\logs" (

                            dir "%TOMCAT_HOME%\\logs"

                        ) else (

                            echo Tomcat logs directory not found.
                        )

                        exit /b 1

                    ) else (

                        echo.
                        echo ==========================================
                        echo APPZILLON IS RUNNING
                        echo ==========================================

                        echo URL:
                        echo http://localhost:%APPZILLON_PORT%/%APP_CONTEXT%/
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
http://localhost:8086/quizzapp/

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