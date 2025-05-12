#!/bin/bash

echo "==================================="
echo "Job App - Test Runner"
echo "==================================="
echo

# Se placer dans le répertoire du script puis du backend
cd "$(dirname "$0")"
cd ../backend

echo "Execution des tests (unitaires et integration)..."
./gradlew allTests

echo
echo "Generation des rapports de couverture JaCoCo..."
echo "Rapports disponibles dans:"
echo "- Tests unitaires:     $(pwd)/build/reports/jacoco/unit/html/index.html"
echo "- Tests d'integration: $(pwd)/build/reports/jacoco/integration/html/index.html"
echo

echo "Fin de l'execution des tests."
echo "===================================" 