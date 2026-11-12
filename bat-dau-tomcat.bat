@echo off
setlocal

set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.12.1"
set "CATALINA_HOME=C:\tomcat9"
set "CATALINA_BASE=C:\tomcat9"

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo Khong tim thay Java tai:
    echo %JAVA_HOME%
    echo Hay sua JAVA_HOME trong file bat-dau-tomcat.bat.
    pause
    exit /b 1
)

if not exist "%CATALINA_HOME%\bin\startup.bat" (
    echo Khong tim thay Tomcat tai:
    echo %CATALINA_HOME%
    pause
    exit /b 1
)

netstat -ano -p tcp | findstr /R /C:":8080 .*LISTENING" >nul
if not errorlevel 1 (
    echo Cong 8080 dang duoc su dung. Tomcat co the da chay.
    echo Mo http://localhost:8080/GaraMan3_Web/ de kiem tra.
    pause
    exit /b 0
)

call "%CATALINA_HOME%\bin\startup.bat"
if errorlevel 1 (
    echo Khong khoi dong duoc Tomcat. Hay kiem tra loi o tren.
    pause
    exit /b 1
)

echo Dang khoi dong Tomcat. Doi vai giay roi mo:
echo http://localhost:8080/GaraMan3_Web/
pause
