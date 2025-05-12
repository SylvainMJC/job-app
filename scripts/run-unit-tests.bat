@echo off
echo ===================================
echo Job App - Unit Tests Runner
echo ===================================
echo.

cd %~dp0
cd ../backend
echo Execution des tests unitaires...
gradlew.bat unitTest

echo.
echo Fin de l'execution des tests.
echo =================================== 