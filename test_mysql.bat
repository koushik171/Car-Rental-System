@echo off
echo Testing MySQL Connection...

REM Check if MySQL jar exists
if not exist "lib\mysql-connector-j-8.2.0.jar" (
    echo MySQL JDBC driver not found in lib folder!
    echo Please copy mysql-connector-j-8.2.0.jar to lib/ folder
    pause
    exit /b 1
)

cd src
echo Compiling test...
javac -cp ".;..\lib\mysql-connector-j-8.2.0.jar" com/carrental/MySQLDatabaseService.java

if %errorlevel% equ 0 (
    echo Testing MySQL connection...
    java -cp ".;..\lib\mysql-connector-j-8.2.0.jar" com.carrental.MySQLDatabaseService
) else (
    echo Compilation failed!
)
pause