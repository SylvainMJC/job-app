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

## Tests unitaires

### Backend

Le backend utilise JUnit 5, Mockito et H2 comme base de données de test.

Pour exécuter les tests et générer un rapport de couverture JaCoCo :

```bash
# Sur Linux/macOS
cd backend
./run-tests.sh

# Sur Windows
cd backend
.\run-tests.bat
```

Le rapport de couverture sera généré dans `backend/build/reports/jacoco/test/html/index.html`.

### CI/CD avec GitHub Actions

Le projet est configuré avec une pipeline CI/CD GitHub Actions qui :

1. Compile le backend et le frontend
2. Exécute les tests unitaires
3. Génère un rapport de couverture avec JaCoCo
4. Publie le rapport de couverture en commentaire sur les Pull Requests
5. Déploie automatiquement sur la branche `main`

## Structure du projet

```
job-app/
├── backend/              # Application Spring Boot
│   ├── src/main/         # Code source principal
│   │   ├── java/         # Code Java
│   │   └── resources/    # Ressources de configuration
│   └── src/test/         # Tests unitaires
├── frontend/             # Application Vue.js
│   ├── public/           # Ressources statiques
│   └── src/              # Code source
│       ├── components/   # Composants Vue
│       ├── views/        # Pages de l'application
│       ├── router/       # Configuration du routeur
│       └── services/     # Services API
└── docker-compose.yml    # Configuration Docker Compose
```

## Maintien et contribution

### Meilleurs pratiques

1. **Tests** : Maintenir une couverture de test d'au moins 70%
2. **CI/CD** : Utiliser des Pull Requests pour toutes les modifications
3. **Documentation** : Documenter les nouvelles fonctionnalités dans le README

### Licences

Ce projet est distribué sous licence libre. 