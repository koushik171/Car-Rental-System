@echo off
echo Starting Car Rental System...

REM Check MySQL service
echo Checking MySQL service...
sc query mysql80 | find "RUNNING" >nul
if %errorlevel% neq 0 (
    echo Starting MySQL service...
    net start mysql80
)

REM Test connection
echo Testing MySQL connection...
.\test_mysql.bat

REM Run application
echo Starting Car Rental Application...
.\compile_with_mysql.bat