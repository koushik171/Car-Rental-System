@echo off
echo ========================================
echo   Compiling Car Rental API Backend
echo ========================================
echo.

cd backend\src\main\java

echo Compiling API classes...
javac -cp ".;..\..\..\..\lib\gson-2.10.1.jar;..\..\..\..\lib\mysql-connector-j-8.2.0.jar" com/carrental/api/*.java

if %errorlevel% equ 0 (
    echo.
    echo ✅ Compilation successful!
    echo.
    echo To run the API server, execute: run_api.bat
) else (
    echo.
    echo ❌ Compilation failed!
    echo.
    echo Make sure you have:
    echo 1. gson-2.10.1.jar in lib folder
    echo 2. mysql-connector-j-8.2.0.jar in lib folder
)

echo.
pause
