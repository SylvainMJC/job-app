# Scripts utilitaires pour Job App

Ce dossier contient des scripts utilitaires pour faciliter le développement et le test de l'application Job App.

## Scripts disponibles

### Exécution de l'application

- `run-app.bat` (Windows) / `run-app.sh` (Linux/Mac) : Démarre l'application Spring Boot

### Scripts de test

- `run-tests.bat` (Windows) / `run-tests.sh` (Linux/Mac) : Exécute tous les tests (unitaires et d'intégration) et génère les rapports de couverture JaCoCo
- `run-unit-tests.bat` (Windows) / `run-unit-tests.sh` (Linux/Mac) : Exécute uniquement les tests unitaires
- `run-integration-tests.bat` (Windows) / `run-integration-tests.sh` (Linux/Mac) : Exécute uniquement les tests d'intégration

## Utilisation

### Sur Windows

```bash
# Exécution de l'application
scripts\run-app.bat

# Exécution de tous les tests
scripts\run-tests.bat

# Exécution des tests unitaires uniquement
scripts\run-unit-tests.bat

# Exécution des tests d'intégration uniquement
scripts\run-integration-tests.bat
```

### Sur Linux/Mac

```bash
# Rendre les scripts exécutables (à faire une seule fois)
chmod +x scripts/*.sh

# Exécution de l'application
./scripts/run-app.sh

# Exécution de tous les tests
./scripts/run-tests.sh

# Exécution des tests unitaires uniquement
./scripts/run-unit-tests.sh

# Exécution des tests d'intégration uniquement
./scripts/run-integration-tests.sh
``` 