<script setup>
import HelloWorld from './components/HelloWorld.vue'
import TheWelcome from './components/TheWelcome.vue'
import AuthService from '@/services/auth.service'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
</script>

<template>
  <div class="app-container">
    <header class="header">
      <nav class="navbar">
        <div class="nav-brand">
          <router-link to="/" class="nav-logo">Job App</router-link>
        </div>
        <div class="nav-links">
          <router-link to="/" class="nav-item">Accueil</router-link>
          <router-link to="/offres" class="nav-item">Offres</router-link>
          
          <template v-if="isRecruteur">
            <router-link to="/mes-offres" class="nav-item">Mes offres</router-link>
            <router-link to="/publier-offre" class="nav-item">Publier une offre</router-link>
          </template>
          
          <template v-if="isCandidat">
            <router-link to="/mes-candidatures" class="nav-item">Mes candidatures</router-link>
          </template>
          
          <template v-if="isAdmin">
            <router-link to="/admin" class="nav-item">Administration</router-link>
          </template>
        </div>
        <div class="auth-links">
          <template v-if="!currentUser">
            <router-link to="/login" class="auth-btn login-btn">Connexion</router-link>
            <router-link to="/register" class="auth-btn register-btn">Inscription</router-link>
          </template>
          <template v-else>
            <span class="user-info">{{ currentUser.username }}</span>
            <button @click="logout" class="auth-btn logout-btn">Déconnexion</button>
          </template>
        </div>
      </nav>
    </header>
    
    <main class="main-content">
      <router-view></router-view>
    </main>
    
    <footer class="footer">
      <p>© 2023 Job App - Tous droits réservés</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      currentUser: null
    };
  },
  computed: {
    isCandidat() {
      return this.currentUser && 
        (this.currentUser.roles.includes('CANDIDAT') || this.currentUser.roles.includes('ADMIN'));
    },
    isRecruteur() {
      return this.currentUser && 
        (this.currentUser.roles.includes('RECRUTEUR') || this.currentUser.roles.includes('ADMIN'));
    },
    isAdmin() {
      return this.currentUser && this.currentUser.roles.includes('ADMIN');
    }
  },
  methods: {
    logout() {
      AuthService.logout();
      this.currentUser = null;
      this.$router.push('/login');
    }
  },
  created() {
    this.currentUser = AuthService.getCurrentUser();
  }
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Arial', sans-serif;
  line-height: 1.6;
  color: #333;
}

.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.header {
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 10;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.nav-brand {
  font-size: 1.5rem;
  font-weight: bold;
}

.nav-logo {
  color: #4f46e5;
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 1.5rem;
}

.nav-item {
  color: #4b5563;
  text-decoration: none;
  transition: color 0.2s;
}

.nav-item:hover {
  color: #4f46e5;
}

.auth-links {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.auth-btn {
  padding: 0.5rem 1rem;
  border-radius: 0.25rem;
  font-weight: 500;
  cursor: pointer;
  text-decoration: none;
}

.login-btn {
  color: #4f46e5;
}

.register-btn, .logout-btn {
  background-color: #4f46e5;
  color: white;
  border: none;
}

.register-btn:hover, .logout-btn:hover {
  background-color: #4338ca;
}

.user-info {
  margin-right: 0.5rem;
  font-weight: 500;
}

.main-content {
  flex: 1;
  padding: 2rem;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
}

.footer {
  background-color: #f9fafb;
  padding: 1.5rem;
  text-align: center;
  border-top: 1px solid #e5e7eb;
}
</style>
