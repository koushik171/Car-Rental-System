@echo off
echo Compiling Car Rental System...
cd src
javac com/carrental/*.java

if %errorlevel% equ 0 (
    echo Compilation successful!
    echo Running Car Rental System...
    echo.
    java com.carrental.Main
) else (
    echo Compilation failed!
    pause
)