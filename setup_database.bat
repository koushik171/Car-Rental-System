@echo off
echo Setting up SQLite database for Car Rental System...

REM Create lib directory
if not exist "lib" mkdir lib

REM Download SQLite JDBC driver (you need to download this manually)
echo.
echo Please download sqlite-jdbc-3.44.1.0.jar from:
echo https://github.com/xerial/sqlite-jdbc/releases
echo.
echo Place it in the 'lib' directory and run compile_with_db.bat
echo.
pause