#!/bin/bash

echo "==================================="
echo "Job App - Unit Tests Runner"
echo "==================================="
echo

# Se placer dans le répertoire du script puis du backend
cd "$(dirname "$0")"
cd ../backend

echo "Execution des tests unitaires..."
./gradlew unitTest

echo
echo "Fin de l'execution des tests unitaires."
echo "===================================" 