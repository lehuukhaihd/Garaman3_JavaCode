@echo off
setlocal
cd /d "%~dp0"

if defined JAVA_HOME (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_EXE=java"
)

if not defined PORT set "PORT=%~1"
if not defined PORT set "PORT=8080"

set "LAUNCHER_JAR=%~dp0target\GaraMan3_Web-embedded.jar"
set "WAR_FILE=%~dp0target\GaraMan3_Web.war"
set "LIB_DIR=%~dp0target\embedded\lib"

if not exist "%WAR_FILE%" (
    echo Khong tim thay WAR. Hay build du an bang lenh mvn clean package truoc.
    pause
    exit /b 1
)

if not exist "%LAUNCHER_JAR%" (
    echo Khong tim thay embedded launcher. Hay build du an bang lenh mvn clean package truoc.
    pause
    exit /b 1
)

dir /b "%LIB_DIR%\tomcat-embed-core-*.jar" >nul 2>&1
if errorlevel 1 (
    echo Khong tim thay thu vien embedded Tomcat. Hay build lai bang mvn clean package.
    pause
    exit /b 1
)

echo Dang khoi dong GaraMan3 voi embedded Tomcat tren cong %PORT%...
start "GaraMan3 Embedded Tomcat" "%JAVA_EXE%" -cp "%LAUNCHER_JAR%;%LIB_DIR%\*" com.garaman3.launcher.EmbeddedTomcat "%WAR_FILE%" "%PORT%"

set "APP_URL=http://localhost:%PORT%/GaraMan3_Web/"
set "WAIT_COUNT=0"

:wait_for_server
curl.exe --fail --silent --max-time 2 "%APP_URL%" -o NUL >nul 2>&1
if not errorlevel 1 goto open_browser
set /a WAIT_COUNT+=1
if %WAIT_COUNT% GEQ 30 goto server_not_ready
timeout /t 1 /nobreak >nul
goto wait_for_server

:open_browser
echo Dang mo %APP_URL%...
start "" "%APP_URL%"
exit /b 0

:server_not_ready
echo Khong truy cap duoc GaraMan3 sau 30 giay. Kiem tra cua so Tomcat, cong va MySQL.
    pause
    exit /b 1
