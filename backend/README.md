# Job App Backend

Ce dossier contient le backend de l'application de gestion d'offres d'emploi, développé avec Spring Boot.

## Prérequis

- Java 17
- Gradle
- Docker (pour PostgreSQL et les tests d'intégration)

## Démarrage rapide

1. **Lancer la base de données PostgreSQL**
   ```bash
   docker-compose up -d db
   ```

2. **Lancer l'application**
   ```bash
   ./gradlew bootRun
   ```

L'API sera disponible à l'adresse : `http://localhost:8080`

## Tests

### Tests unitaires

Les tests unitaires utilisent H2 (base de données en mémoire) et peuvent être exécutés sans dépendances externes :

```bash
# Sur Linux/macOS
./gradlew test
# ou
./run-tests.sh

# Sur Windows
gradlew.bat test
# ou
.\run-tests.bat
```

Le rapport de couverture JaCoCo pour les tests unitaires sera généré dans `build/reports/jacoco/test/html/index.html`.

### Tests d'intégration

Les tests d'intégration utilisent TestContainers pour démarrer automatiquement une base de données PostgreSQL dans un conteneur Docker. Ils nécessitent que Docker soit installé et en cours d'exécution :

```bash
# Sur Linux/macOS
./gradlew integrationTest
# ou
./run-integration-tests.sh

# Sur Windows
gradlew.bat integrationTest
# ou
.\run-integration-tests.bat
```

Le rapport de couverture JaCoCo pour les tests d'intégration sera généré dans `build/reports/jacoco/integration/html/index.html`.

### Conventions pour les tests

- **Tests unitaires** : fichiers nommés `*Test.java` (ex: `OffreTest.java`)
- **Tests d'intégration** : fichiers nommés `*IT.java` (ex: `OffreControllerIT.java`)

## Structure du projet

- `src/main/java/fr/epsi/b3devc1/sylvainmjc/backend`
  - `config` - Configuration Spring (CORS, etc.)
  - `controller` - Contrôleurs REST
  - `entity` - Entités JPA
  - `repository` - Repositories Spring Data JPA

## Endpoints API

### Offres d'emploi

- `GET /api/offres` - Récupérer toutes les offres
- `GET /api/offres/{id}` - Récupérer une offre par son ID
- `POST /api/offres` - Créer une nouvelle offre
- `PUT /api/offres/{id}` - Mettre à jour une offre
- `DELETE /api/offres/{id}` - Supprimer une offre

### Candidatures

- `GET /api/candidatures` - Récupérer toutes les candidatures
- `GET /api/candidatures/{id}` - Récupérer une candidature par son ID
- `GET /api/candidatures/offre/{offreId}` - Récupérer les candidatures pour une offre
- `POST /api/candidatures` - Créer une candidature
- `POST /api/candidatures/offre/{offreId}` - Créer une candidature pour une offre
- `PUT /api/candidatures/{id}` - Mettre à jour une candidature
- `DELETE /api/candidatures/{id}` - Supprimer une candidature

## Technologies

- Spring Boot 3.2.5
- Java 17
- Spring Security
- JWT Authentication
- PostgreSQL
- TestContainers
- JaCoCo

## Configuration

### JWT Authentication

This application uses JWT version 0.11.5 for authentication. This version was chosen for compatibility reasons as the newer 0.12.x versions require a different API for parsing tokens.

JWT tokens are used for the following roles:
- CANDIDAT: Users who can apply to jobs
- RECRUTEUR: Users who can post and manage jobs
- ADMIN: Users with full access to all features

### Database

The application uses PostgreSQL in production and for integration tests. For unit tests, it uses H2 in-memory database.

## Testing

### Unit Tests

Unit tests are run with the H2 in-memory database and use the JUnit 5 framework with the `@Tag("unit")` annotation.

To run unit tests:
```bash
./gradlew unitTest
```

### Integration Tests

Integration tests use TestContainers to spin up a PostgreSQL database and are tagged with `@Tag("integration")` annotation.

To run integration tests:
```bash
./gradlew integrationTest
```

### Code Coverage

JaCoCo is used for code coverage reporting. Separate reports are generated for unit and integration tests.

To generate all reports:
```bash
./gradlew allTests
```

## Building the Application

To build the application without running tests:
```bash
./gradlew build -x test
```

## Docker Support

The application can be run in a Docker container using the provided Dockerfile. The Docker image uses OpenJDK 17. 