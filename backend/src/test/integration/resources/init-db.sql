-- Drop tables if they exist
DROP TABLE IF EXISTS candidature CASCADE;
DROP TABLE IF EXISTS offre CASCADE;
DROP TABLE IF EXISTS utilisateur_roles CASCADE;
DROP TABLE IF EXISTS utilisateurs CASCADE;

-- Create tables
CREATE TABLE IF NOT EXISTS utilisateurs (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    enabled BOOLEAN DEFAULT TRUE,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS utilisateur_roles (
    utilisateur_id BIGINT NOT NULL,
    roles VARCHAR(255),
    CONSTRAINT fk_utilisateur FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);

CREATE TABLE IF NOT EXISTS offre (
    id BIGSERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    entreprise VARCHAR(255) NOT NULL,
    localisation VARCHAR(255),
    description TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    utilisateur_id BIGINT,
    CONSTRAINT fk_offre_utilisateur FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);

CREATE TABLE IF NOT EXISTS candidature (
    id BIGSERIAL PRIMARY KEY,
    date_candidature TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(50) DEFAULT 'En attente',
    motivation TEXT,
    offre_id BIGINT NOT NULL,
    utilisateur_id BIGINT,
    CONSTRAINT fk_candidature_offre FOREIGN KEY (offre_id) REFERENCES offre(id),
    CONSTRAINT fk_candidature_utilisateur FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);

-- Insert test data for users
INSERT INTO utilisateurs (username, password, email, nom, prenom) 
VALUES 
('admin', '$2a$10$uLEkjZ0vDLRgOEEDjZo6KuxAywKJmJuZ7ByTzj0kMHtbhhz4WFg/q', 'admin@test.com', 'Admin', 'User'),
('recruteur', '$2a$10$uLEkjZ0vDLRgOEEDjZo6KuxAywKJmJuZ7ByTzj0kMHtbhhz4WFg/q', 'recruteur@test.com', 'Recruteur', 'User'),
('candidat', '$2a$10$uLEkjZ0vDLRgOEEDjZo6KuxAywKJmJuZ7ByTzj0kMHtbhhz4WFg/q', 'candidat@test.com', 'Candidat', 'User');

-- Assign roles
INSERT INTO utilisateur_roles (utilisateur_id, roles) 
VALUES 
(1, 'ROLE_ADMIN'),
(2, 'ROLE_RECRUTEUR'),
(3, 'ROLE_CANDIDAT'); 