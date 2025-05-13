<template>
  <div class="login-container">
    <div class="login-form">
      <h1 class="text-2xl font-bold mb-6">Connexion</h1>
      
      <div v-if="message" class="alert" :class="{ 'alert-error': error, 'alert-success': !error }">
        {{ message }}
      </div>
      
      <div class="form-group">
        <label for="username">Nom d'utilisateur</label>
        <input
          type="text"
          id="username"
          v-model="user.username"
          name="username"
          class="form-control"
          required
        />
      </div>
      
      <div class="form-group">
        <label for="password">Mot de passe</label>
        <input
          type="password"
          id="password"
          v-model="user.password"
          name="password"
          class="form-control"
          required
        />
      </div>
      
      <div class="form-actions">
        <button 
          @click="handleLogin" 
          class="btn btn-primary"
          :disabled="loading"
        >
          <span v-if="loading">Chargement...</span>
          <span v-else>Se connecter</span>
        </button>
      </div>
      
      <div class="mt-4 text-center">
        <p>Pas encore de compte? <router-link to="/register" class="text-blue-600">S'inscrire</router-link></p>
      </div>
    </div>
  </div>
</template>

<script>
import AuthService from '@/services/auth.service';

export default {
  name: 'LoginView',
  data() {
    return {
      user: {
        username: '',
        password: ''
      },
      loading: false,
      message: '',
      error: false
    };
  },
  methods: {
    async handleLogin() {
      this.message = '';
      this.loading = true;
      
      try {
        if (this.user.username && this.user.password) {
          await AuthService.login(this.user.username, this.user.password);
          this.$router.push('/');
        } else {
          this.error = true;
          this.message = 'Veuillez remplir tous les champs';
        }
      } catch (error) {
        this.error = true;
        this.message = 'Identifiants invalides';
        console.error(error);
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  padding: 2rem;
}

.login-form {
  width: 100%;
  max-width: 400px;
  padding: 2rem;
  border: 1px solid #e2e8f0;
  border-radius: 0.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-control {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #e2e8f0;
  border-radius: 0.25rem;
}

.form-actions {
  margin-top: 1.5rem;
}

.btn {
  padding: 0.5rem 1rem;
  border-radius: 0.25rem;
  cursor: pointer;
  font-weight: 500;
}

.btn-primary {
  background-color: #4f46e5;
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: #4338ca;
}

.btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.alert {
  padding: 0.75rem;
  margin-bottom: 1rem;
  border-radius: 0.25rem;
}

.alert-error {
  background-color: #fee2e2;
  border: 1px solid #fecaca;
  color: #b91c1c;
}

.alert-success {
  background-color: #dcfce7;
  border: 1px solid #bbf7d0;
  color: #15803d;
}
</style> 