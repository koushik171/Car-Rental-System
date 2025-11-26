@echo off
echo Compiling Car Rental System with MySQL...

REM Check if MySQL JDBC driver exists
if not exist "lib\mysql-connector-j-8.2.0.jar" (
    echo MySQL JDBC driver not found!
    echo Please run setup_mysql.bat first
    pause
    exit /b 1
)

cd src
echo Compiling Java files...
javac -cp ".;..\lib\mysql-connector-j-8.2.0.jar" com/carrental/*.java

if %errorlevel% equ 0 (
    echo Compilation successful!
    echo Running Car Rental System with MySQL...
    echo.
    java -cp ".;..\lib\mysql-connector-j-8.2.0.jar" com.carrental.Main
) else (
    echo Compilation failed!
    pause
)