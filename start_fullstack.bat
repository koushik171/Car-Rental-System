@echo off
cls
echo ========================================
echo   Car Rental Full-Stack Application
echo ========================================
echo.
echo What would you like to do?
echo.
echo 1. Compile API Backend
echo 2. Run API Server
echo 3. Open Frontend
echo 4. Run Everything (API + Frontend)
echo 5. Exit
echo.
set /p choice="Enter your choice (1-5): "

if "%choice%"=="1" (
    call compile_api.bat
) else if "%choice%"=="2" (
    call run_api.bat
) else if "%choice%"=="3" (
    call open_frontend.bat
) else if "%choice%"=="4" (
    echo.
    echo Starting API server...
    start "Car Rental API" cmd /k run_api.bat
    timeout /t 3 /nobreak >nul
    echo.
    echo Opening frontend...
    call open_frontend.bat
) else if "%choice%"=="5" (
    exit
) else (
    echo Invalid choice!
    pause
    start_fullstack.bat
)
