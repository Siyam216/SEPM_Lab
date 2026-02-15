@echo off
REM ========================================
REM   Student Management System - Docker
REM   View Logs Script
REM ========================================

echo.
echo ================================================
echo   Student Management System - Live Logs
echo ================================================
echo.
echo Press Ctrl+C to stop viewing logs
echo.
timeout /t 2 /nobreak >nul

docker-compose logs -f
