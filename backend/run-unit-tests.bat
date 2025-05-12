@echo off
echo ===================================
echo Job App - Unit Tests Runner
echo ===================================
echo.

cd %~dp0
echo Execution des tests unitaires specifiques...
gradlew.bat test --tests "fr.epsi.b3devc1.sylvainmjc.backend.entity.*Test"
gradlew.bat test --tests "fr.epsi.b3devc1.sylvainmjc.backend.repository.*Test"

echo.
echo Fin de l'execution des tests.
echo =================================== 