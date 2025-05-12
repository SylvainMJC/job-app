# Job App - Application de gestion d'offres d'emploi et de candidatures

Une application web complète permettant de gérer des offres d'emploi et des candidatures. Cette application utilise une architecture moderne avec un backend Spring Boot et un frontend Vue.js.

## Fonctionnalités

### Gestion des offres d'emploi
- Liste des offres disponibles
- Ajout d'une nouvelle offre
- Modification d'une offre existante
- Suppression d'une offre
- Affichage détaillé d'une offre

### Gestion des candidatures
- Liste des candidatures reçues
- Ajout d'une nouvelle candidature
- Modification d'une candidature existante
- Suppression d'une candidature
- Postuler directement à une offre depuis sa page de détail

## Architecture technique

### Backend (Spring Boot)
- Java 17
- Spring Boot 3
- Spring Web (REST API)
- Spring Data JPA
- PostgreSQL
- Lombok
- Validation

### Frontend (Vue.js)
- Vue 3 (Composition API)
- Vue Router
- Axios
- CSS moderne et responsive

### Infrastructure
- Docker & Docker Compose pour conteneuriser l'application
- Base de données PostgreSQL
- Serveurs web pour le frontend et le backend

## Installation et démarrage

### Prérequis
- Docker et Docker Compose
- Docker Desktop (pour Windows)

### Démarrage rapide
1. Clonez ce dépôt :
```bash
git clone https://github.com/votre-utilisateur/job-app.git
cd job-app
```

2. Lancez l'application avec Docker Compose :
```bash
docker-compose up --build
```

3. Accédez à l'application :
   - Frontend: http://localhost:80
   - Backend API: http://localhost:8080
   - Base de données PostgreSQL: localhost:5432 (jobapp/postgres/postgres)

## Structure du projet

```
job-app/
├── backend/              → Application Spring Boot
│   ├── src/              → Code source Java
│   └── Dockerfile        → Configuration Docker
├── frontend/             → Application Vue.js
│   ├── src/              → Code source JavaScript
│   └── Dockerfile        → Configuration Docker
├── docker-compose.yml    → Orchestration des services
└── README.md             → Documentation du projet
```

## Développement

### Backend
Le backend est accessible sur http://localhost:8080 et expose une API REST :
- `/api/offres` : Gestion des offres d'emploi (GET, POST, PUT, DELETE)
- `/api/candidatures` : Gestion des candidatures (GET, POST, PUT, DELETE)

### Frontend
Le frontend est accessible sur http://localhost et comprend les pages suivantes :
- `/offres` : Liste des offres d'emploi
- `/offres/new` : Création d'une nouvelle offre
- `/offres/:id` : Détail d'une offre avec formulaire de candidature
- `/offres/:id/edit` : Modification d'une offre
- `/candidatures` : Liste des candidatures
- `/candidatures/new` : Création d'une nouvelle candidature
- `/candidatures/:id/edit` : Modification d'une candidature

## Licence
Ce projet est distribué sous licence MIT. 