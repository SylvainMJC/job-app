#!/bin/bash

echo "==================================="
echo "Job App - Application Runner"
echo "==================================="
echo

# Se placer dans le répertoire du script puis du backend
cd "$(dirname "$0")"
cd ../backend

echo "Demarrage de l'application..."
./gradlew bootRun

echo
echo "Fin de l'execution de l'application."
echo "===================================" 