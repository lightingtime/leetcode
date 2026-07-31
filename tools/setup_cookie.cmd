@echo off
chcp 65001 >nul
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0..\.agents\skills\lc-submit\scripts\setup_cookie.ps1"
pause
