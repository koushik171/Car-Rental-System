@echo off
echo Compiling Car Rental System...
cd src
javac -cp ".;..\lib\mysql-connector-j-8.2.0.jar" com/carrental/*.java

if %errorlevel% equ 0 (
    echo Compilation successful!
    echo Running Car Rental System...
    echo.
    java -cp ".;..\lib\mysql-connector-j-8.2.0.jar" com.carrental.Main
) else (
    echo Compilation failed!
    pause
)