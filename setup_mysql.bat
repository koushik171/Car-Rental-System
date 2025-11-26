@echo off
echo Setting up MySQL for Car Rental System...

REM Create lib directory
if not exist "lib" mkdir lib

echo.
echo MySQL Setup Instructions:
echo 1. Install MySQL Server (https://dev.mysql.com/downloads/mysql/)
echo 2. Download MySQL Connector/J from:
echo    https://dev.mysql.com/downloads/connector/j/
echo 3. Select 'Platform Independent' and download .tar.gz/.zip
echo 4. Extract and copy mysql-connector-j-8.2.0.jar to lib/ folder
echo 4. Create database: CREATE DATABASE car_rental;
echo 5. Update username/password in MySQLDatabaseService.java
echo 6. Run compile_with_mysql.bat
echo.
pause