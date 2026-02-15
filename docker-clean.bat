@echo off
REM ========================================
REM   Student Management System - Docker
REM   Clean/Reset Script
REM ========================================

echo.
echo ================================================
echo   Clean Docker Environment
echo ================================================
echo.
echo WARNING: This will remove all containers and data!
echo.
set /p confirm="Are you sure? (y/n): "

if /i not "%confirm%"=="y" (
    echo.
    echo Cancelled.
    pause
    exit /b 0
)

echo.
echo [1/3] Stopping containers...
docker-compose down

echo.
echo [2/3] Removing volumes (database data)...
docker-compose down -v

echo.
echo [3/3] Removing unused images...
docker image prune -f

echo.
echo ================================================
echo   Cleanup Complete!
echo ================================================
echo.
echo All containers, volumes, and data removed.
echo To start fresh: run docker-start.bat
echo.
pause
