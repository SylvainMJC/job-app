@echo off
echo ===================================
echo Job App - Test Runner
echo ===================================
echo.

cd %~dp0
cd ../backend
echo Execution des tests (unitaires et integration)...
call gradlew.bat allTests

echo.
echo Generation des rapports de couverture JaCoCo...
echo Rapports disponibles dans:
echo - Tests unitaires:     %cd%\build\reports\jacoco\unit\html\index.html
echo - Tests d'integration: %cd%\build\reports\jacoco\integration\html\index.html
echo.

echo Fin de l'execution des tests.
echo =================================== 