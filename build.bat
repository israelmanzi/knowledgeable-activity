@echo off
:loop
git stash -q
git pull -q origin main
timeout /t 60 >nul 2>&1
goto loop
