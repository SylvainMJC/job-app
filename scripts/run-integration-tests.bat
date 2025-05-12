@echo off
echo ===================================
echo Job App - Integration Tests Runner
echo ===================================
echo.

cd %~dp0
cd ../backend
echo Execution des tests d'integration...
gradlew.bat integrationTest

echo.
echo Fin de l'execution des tests d'integration.
echo =================================== 