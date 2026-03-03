@echo off
echo Viewing MySQL Database...
javac -cp ".;lib\mysql-connector-j-8.2.0.jar" ViewDatabase.java
java -cp ".;lib\mysql-connector-j-8.2.0.jar" ViewDatabase
pause
