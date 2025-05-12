@echo off
echo ===================================
echo Job App - Application Runner
echo ===================================
echo.

cd %~dp0
cd ../backend
echo Demarrage de l'application...
gradlew.bat bootRun

echo.
echo Fin de l'execution de l'application.
echo =================================== 