@echo off
REM ========================================
REM   Student Management System - Docker
REM   Stop Script
REM ========================================

echo.
echo ================================================
echo   Stopping Student Management System
echo ================================================
echo.

docker-compose stop

echo.
echo Containers stopped successfully!
echo.
echo To start again: run docker-start.bat
echo To remove completely: run docker-clean.bat
echo.
pause
