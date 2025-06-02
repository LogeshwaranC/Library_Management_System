@echo off
del /s *.class
javac -cp ".;lib/mysql-connector-j-9.3.0.jar" Main\**\*.java
pause
