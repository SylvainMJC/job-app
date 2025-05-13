-- Suppression des tables existantes si elles existent
DROP TABLE IF EXISTS candidature CASCADE;
DROP TABLE IF EXISTS offre CASCADE;
DROP TABLE IF EXISTS utilisateur_roles CASCADE;
DROP TABLE IF EXISTS utilisateurs CASCADE;

-- Création de la table offre
CREATE TABLE IF NOT EXISTS offre (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    entreprise VARCHAR(255) NOT NULL,
    localisation VARCHAR(255),
    description TEXT,
    date_creation TIMESTAMP
);

-- Création de la table candidature
CREATE TABLE IF NOT EXISTS candidature (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    telephone VARCHAR(20),
    message TEXT,
    date_candidat TIMESTAMP,
    offre_id BIGINT,
    FOREIGN KEY (offre_id) REFERENCES offre(id)
);

-- Création de la table utilisateurs
CREATE TABLE IF NOT EXISTS utilisateurs (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    nom VARCHAR(100),
    prenom VARCHAR(100),
    enabled BOOLEAN DEFAULT TRUE
);

-- Création de la table utilisateur_roles
CREATE TABLE IF NOT EXISTS utilisateur_roles (
    utilisateur_id BIGINT NOT NULL,
    roles VARCHAR(50) NOT NULL,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
); 