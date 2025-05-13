-- Insérer un rôle d'admin pour les tests
INSERT INTO utilisateurs (id, username, password, email, nom, prenom, enabled)
VALUES (1, 'admin', '$2a$10$aMbRlBLYSISX5hP/WAX/vuSwxIWUYqIYY2yvQeAJN5oHM0kcRjwLq', 'admin@test.com', 'Admin', 'Test', true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO utilisateur_roles (utilisateur_id, roles)
VALUES (1, 'ADMIN')
ON CONFLICT DO NOTHING;

-- Insérer un rôle de recruteur pour les tests
INSERT INTO utilisateurs (id, username, password, email, nom, prenom, enabled)
VALUES (2, 'recruteur', '$2a$10$aMbRlBLYSISX5hP/WAX/vuSwxIWUYqIYY2yvQeAJN5oHM0kcRjwLq', 'recruteur@test.com', 'Recruteur', 'Test', true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO utilisateur_roles (utilisateur_id, roles)
VALUES (2, 'RECRUTEUR')
ON CONFLICT DO NOTHING;

-- Insérer un rôle de candidat pour les tests
INSERT INTO utilisateurs (id, username, password, email, nom, prenom, enabled)
VALUES (3, 'candidat', '$2a$10$aMbRlBLYSISX5hP/WAX/vuSwxIWUYqIYY2yvQeAJN5oHM0kcRjwLq', 'candidat@test.com', 'Candidat', 'Test', true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO utilisateur_roles (utilisateur_id, roles)
VALUES (3, 'CANDIDAT')
ON CONFLICT DO NOTHING; 