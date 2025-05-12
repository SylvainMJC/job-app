# Job App Backend

Ce dossier contient le backend de l'application de gestion d'offres d'emploi, développé avec Spring Boot.

## Prérequis

- Java 17
- Gradle
- Docker (pour PostgreSQL)

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

## Exécution des tests

### Exécuter tous les tests
```bash
./gradlew test
```

### Générer le rapport de couverture JaCoCo
```bash
./gradlew jacocoTestReport
```

Le rapport HTML sera généré dans `build/reports/jacoco/test/html/index.html`

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
- `GET /api/offres/{id}/candidatures` - Récupérer les candidatures pour une offre
- `POST /api/offres/{id}/candidatures` - Créer une candidature pour une offre
- `PUT /api/candidatures/{id}` - Mettre à jour une candidature
- `DELETE /api/candidatures/{id}` - Supprimer une candidature 