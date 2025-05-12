#!/bin/bash

echo "==================================="
echo "Job App - Integration Tests Runner"
echo "==================================="
echo

# Se placer dans le répertoire du script puis du backend
cd "$(dirname "$0")"
cd ../backend

echo "Execution des tests d'integration..."
./gradlew integrationTest

echo
echo "Fin de l'execution des tests d'integration."
echo "===================================" 