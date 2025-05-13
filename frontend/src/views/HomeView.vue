<template>
  <div class="home-container">
    <div class="hero-section">
      <h1 class="hero-title">Trouvez votre futur emploi</h1>
      <p class="hero-subtitle">Explorez les meilleures opportunités professionnelles</p>
      <div class="hero-actions">
        <router-link to="/offres" class="btn btn-primary">Voir les offres</router-link>
        <router-link v-if="!isAuthenticated" to="/register" class="btn btn-secondary">Créer un compte</router-link>
      </div>
    </div>
    
    <div class="features-section">
      <div class="feature-card">
        <h3>Pour les candidats</h3>
        <p>Trouvez des offres d'emploi adaptées à vos compétences et postulez en quelques clics.</p>
        <router-link v-if="!isCandidat" to="/register" class="feature-link">S'inscrire comme candidat</router-link>
        <router-link v-else to="/mes-candidatures" class="feature-link">Gérer mes candidatures</router-link>
      </div>
      
      <div class="feature-card">
        <h3>Pour les recruteurs</h3>
        <p>Publiez vos offres d'emploi et trouvez les talents qui correspondent à vos besoins.</p>
        <router-link v-if="!isRecruteur" to="/register" class="feature-link">S'inscrire comme recruteur</router-link>
        <router-link v-else to="/mes-offres" class="feature-link">Gérer mes offres</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import AuthService from '@/services/auth.service';

export default {
  name: 'HomeView',
  data() {
    return {
      currentUser: null
    };
  },
  computed: {
    isAuthenticated() {
      return !!this.currentUser;
    },
    isCandidat() {
      return this.currentUser && (
        this.currentUser.roles.includes('CANDIDAT') || 
        this.currentUser.roles.includes('ADMIN')
      );
    },
    isRecruteur() {
      return this.currentUser && (
        this.currentUser.roles.includes('RECRUTEUR') || 
        this.currentUser.roles.includes('ADMIN')
      );
    }
  },
  created() {
    this.currentUser = AuthService.getCurrentUser();
  }
};
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.hero-section {
  text-align: center;
  padding: 4rem 2rem;
  background-color: #f3f4f6;
  border-radius: 0.5rem;
  margin-bottom: 3rem;
}

.hero-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #111827;
  margin-bottom: 1rem;
}

.hero-subtitle {
  font-size: 1.25rem;
  color: #6b7280;
  margin-bottom: 2rem;
}

.hero-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.btn {
  padding: 0.75rem 1.5rem;
  border-radius: 0.375rem;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.2s;
}

.btn-primary {
  background-color: #4f46e5;
  color: white;
}

.btn-primary:hover {
  background-color: #4338ca;
}

.btn-secondary {
  background-color: white;
  color: #4f46e5;
  border: 1px solid #4f46e5;
}

.btn-secondary:hover {
  background-color: #f9fafb;
}

.features-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-top: 3rem;
}

.feature-card {
  padding: 2rem;
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  transition: all 0.2s;
}

.feature-card:hover {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transform: translateY(-5px);
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: #111827;
}

.feature-card p {
  color: #6b7280;
  margin-bottom: 1.5rem;
}

.feature-link {
  color: #4f46e5;
  text-decoration: none;
  font-weight: 500;
}

.feature-link:hover {
  text-decoration: underline;
}
</style> 