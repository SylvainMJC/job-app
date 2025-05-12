@echo off
echo ===================================
echo Job App - Test Runner
echo ===================================
echo.

echo Execution des tests unitaires...
call gradlew.bat test

echo.
echo Generation du rapport de couverture JaCoCo...
call gradlew.bat jacocoTestReport

echo.
echo Rapport de couverture genere dans:
echo %cd%\build\reports\jacoco\test\html\index.html
echo.

echo Fin de l'execution des tests.
echo =================================== 