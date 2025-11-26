@echo off
echo Compiling Car Rental System with Database Support...

REM Check if SQLite JDBC driver exists
if not exist "lib\sqlite-jdbc-3.44.1.0.jar" (
    echo SQLite JDBC driver not found!
    echo Please run setup_database.bat first
    pause
    exit /b 1
)

cd src
echo Compiling Java files...
javac -cp ".;..\lib\sqlite-jdbc-3.44.1.0.jar" com/carrental/*.java

if %errorlevel% equ 0 (
    echo Compilation successful!
    echo Running Car Rental System with Database...
    echo.
    java -cp ".;..\lib\sqlite-jdbc-3.44.1.0.jar" com.carrental.Main
) else (
    echo Compilation failed!
    pause
)