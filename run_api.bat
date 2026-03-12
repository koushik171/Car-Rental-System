@echo off
echo ========================================
echo   Starting Car Rental API Server
echo ========================================
echo.

cd backend\src\main\java

echo Starting API server on port 8080...
echo.
echo API will be available at: http://localhost:8080
echo.
echo Press Ctrl+C to stop the server
echo.

java -cp ".;..\..\..\..\lib\gson-2.10.1.jar;..\..\..\..\lib\mysql-connector-j-8.2.0.jar" com.carrental.api.CarRentalAPI

pause
