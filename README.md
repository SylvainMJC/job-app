# Job App - Documentation des Tests


## Tests unitaires (JUnit 5)

Tests isolés des composants avec JUnit 5 et Mockito. Situés dans `backend/src/test/unit/java`.

Exécution : `scripts/run-unit-tests.bat` (Windows) ou `./scripts/run-unit-tests.sh` (Linux/Mac).

Rapports : `backend/build/reports/jacoco/unit/html/index.html`

## Tests d'intégration (SpringBootTest) (avec DIND)

Tests des interactions entre composants avec SpringBootTest et Testcontainers. Situés dans `backend/src/test/integration/java`.

Prérequis : Docker installé pour les conteneurs PostgreSQL.

Exécution : `scripts/run-integration-tests.bat` (Windows) ou `./scripts/run-integration-tests.sh` (Linux/Mac).

Rapports : `backend/build/reports/jacoco/integration/html/index.html`

## Tests E2E (Playwright) (avec DIND)

Tests simulant les interactions utilisateur avec Playwright. Situés dans `frontend/tests/e2e`.

Prérequis : Docker pour l'environnement complet (frontend, backend, BDD).

Exécution : `npm run test:e2e` depuis le répertoire frontend.

## Tests de performance (JMeter) (avec DIND)

Tests de charge avec Apache JMeter. Situés dans `backend/src/test/jmeter`.

Prérequis : Docker pour JMeter et l'application.


## Tests de sécurité (OWASP ZAP) (avec DIND)

Tests de vulnérabilités avec OWASP ZAP.

Prérequis : Docker pour ZAP et l'application.

Problèmes avec l'authentification JWT et les autorisations. Ces tests sont configurés pour ne pas interrompre le workflow CI/CD en cas d'échec, permettant ainsi au pipeline de continuer même si les tests de sécurité échouent.

## Pipeline CI

Configuration dans `.github/workflows` exécutant automatiquement la construction et les tests.
