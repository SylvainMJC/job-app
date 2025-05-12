#!/bin/bash

echo "==================================="
echo "Job App - Test Runner"
echo "==================================="
echo

echo "Execution des tests unitaires..."
./gradlew test

echo
echo "Generation du rapport de couverture JaCoCo..."
./gradlew jacocoTestReport

echo
echo "Rapport de couverture généré dans:"
echo "$(pwd)/build/reports/jacoco/test/html/index.html"
echo

echo "Fin de l'execution des tests."
echo "===================================" 