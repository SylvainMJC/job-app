FROM mcr.microsoft.com/playwright:v1.42.0-focal

WORKDIR /app

# Copier les fichiers package.json et package-lock.json
COPY package*.json ./

# Installer les dépendances
RUN npm ci

# Copier les fichiers du projet
COPY . .

# Vérifier l'installation des navigateurs
RUN npx playwright install --with-deps chromium

# Commande pour exécuter les tests
CMD ["npm", "run", "test:e2e"] 