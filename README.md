# Job App

Application de gestion d'offres d'emploi et de candidatures, développée avec Spring Boot (backend) et Vue.js (frontend).

## Architecture

- **Backend** : Spring Boot (Java 17)
- **Frontend** : Vue.js 3
- **Base de données** : PostgreSQL
- **Déploiement** : Docker

## Fonctionnalités

- Gestion des offres d'emploi (CRUD)
- Gestion des candidatures (CRUD)
- Affichage détaillé des offres avec formulaire de candidature intégré
- Interface responsive

## Installation et démarrage

### Prérequis

- Java 17
- Node.js 18 ou supérieur
- Docker et Docker Compose

### Démarrer l'application avec Docker Compose

```bash
docker-compose up -d
```

L'application sera disponible à :
- Frontend : http://localhost:80
- Backend : http://localhost:8080
- Base de données : localhost:5432

### Démarrage en développement

1. **Backend**
   ```bash
   cd backend
   ./gradlew bootRun
   ```

2. **Frontend**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

## Tests

### Tests unitaires

Les tests unitaires utilisent JUnit 5, Mockito et H2 comme base de données de test en mémoire.

```bash
# Sur Linux/macOS
cd backend
./run-tests.sh

# Sur Windows
cd backend
.\run-tests.bat
```

Le rapport de couverture JaCoCo pour les tests unitaires sera généré dans `backend/build/reports/jacoco/test/html/index.html`.

### Tests d'intégration

Les tests d'intégration utilisent SpringBootTest et TestContainers pour démarrer automatiquement des conteneurs Docker pour les tests. Ils nécessitent que Docker soit installé et en cours d'exécution.

```bash
# Sur Linux/macOS
cd backend
./run-integration-tests.sh

# Sur Windows
cd backend
.\run-integration-tests.bat
```

Le rapport de couverture JaCoCo pour les tests d'intégration sera généré dans `backend/build/reports/jacoco/integration/html/index.html`.

### Conventions de nommage des tests

- **Tests unitaires** : fichiers nommés `*Test.java` 
- **Tests d'intégration** : fichiers nommés `*IT.java`

## CI/CD avec GitHub Actions

Le projet est configuré avec deux pipelines CI/CD GitHub Actions :

### 1. Pipeline de tests unitaires

Ce workflow s'exécute à chaque commit et pull request :
- Compile le backend et le frontend
- Exécute les tests unitaires
- Génère un rapport de couverture avec JaCoCo
- Publie le rapport de couverture

### 2. Pipeline de tests d'intégration

Ce workflow utilise Docker-in-Docker (DIND) pour exécuter les tests d'intégration qui nécessitent des conteneurs Docker :
- S'exécute à chaque commit et pull request
- Configure un environnement Docker-in-Docker
- Exécute uniquement les tests d'intégration
- Génère un rapport de couverture spécifique aux tests d'intégration
- Peut être déclenché manuellement depuis l'interface GitHub Actions

## Structure du projet

```
job-app/
├── backend/              # Application Spring Boot
│   ├── src/main/         # Code source principal
│   │   ├── java/         # Code Java
│   │   └── resources/    # Ressources de configuration
│   └── src/test/         # Tests unitaires et d'intégration
├── frontend/             # Application Vue.js
│   ├── public/           # Ressources statiques
│   └── src/              # Code source
│       ├── components/   # Composants Vue
│       ├── views/        # Pages de l'application
│       ├── router/       # Configuration du routeur
│       └── services/     # Services API
├── .github/workflows/    # Configuration CI/CD GitHub Actions
└── docker-compose.yml    # Configuration Docker Compose
```

## Maintien et contribution

### Meilleurs pratiques

1. **Tests** : Maintenir une couverture de test d'au moins 70%
2. **CI/CD** : Utiliser des Pull Requests pour toutes les modifications
3. **Documentation** : Documenter les nouvelles fonctionnalités dans le README

### Licences

Ce projet est distribué sous licence libre. 