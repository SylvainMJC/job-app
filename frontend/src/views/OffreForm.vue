<template>
  <div class="offre-form">
    <h1>{{ isEdit ? 'Modifier l\'offre' : 'Nouvelle offre' }}</h1>

    <form @submit.prevent="saveOffre" class="form">
      <div class="form-group">
        <label for="titre">Titre</label>
        <input
          type="text"
          id="titre"
          v-model="offre.titre"
          required
          class="form-control"
        >
      </div>

      <div class="form-group">
        <label for="entreprise">Entreprise</label>
        <input
          type="text"
          id="entreprise"
          v-model="offre.entreprise"
          required
          class="form-control"
        >
      </div>

      <div class="form-group">
        <label for="localisation">Localisation</label>
        <input
          type="text"
          id="localisation"
          v-model="offre.localisation"
          required
          class="form-control"
        >
      </div>

      <div class="form-group">
        <label for="description">Description</label>
        <textarea
          id="description"
          v-model="offre.description"
          required
          class="form-control"
          rows="5"
        ></textarea>
      </div>

      <div class="form-actions">
        <router-link to="/offres" class="btn btn-secondary">Annuler</router-link>
        <button type="submit" class="btn btn-primary">Enregistrer</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../services/api'

const route = useRoute()
const router = useRouter()

const offre = ref({
  titre: '',
  entreprise: '',
  localisation: '',
  description: ''
})

const isEdit = computed(() => route.params.id !== undefined)

const loadOffre = async () => {
  if (!isEdit.value) return

  try {
    const response = await api.get(`/api/offres/${route.params.id}`)
    offre.value = response.data
  } catch (error) {
    console.error('Erreur lors du chargement de l\'offre:', error)
  }
}

const saveOffre = async () => {
  try {
    if (isEdit.value) {
      await api.put(`/api/offres/${route.params.id}`, offre.value)
    } else {
      await api.post('/api/offres', offre.value)
    }
    router.push('/offres')
  } catch (error) {
    console.error('Erreur lors de l\'enregistrement:', error)
  }
}

onMounted(loadOffre)
</script>

<style scoped>
.offre-form {
  max-width: 600px;
  margin: 0 auto;
  padding: 1rem 0;
}

.form {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #2c3e50;
  font-weight: 500;
}

.form-control {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.form-control:focus {
  outline: none;
  border-color: #42b983;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
  font-size: 0.9rem;
  transition: background-color 0.2s;
}

.btn-primary {
  background-color: #42b983;
  color: white;
}

.btn-primary:hover {
  background-color: #3aa876;
}

.btn-secondary {
  background-color: #666;
  color: white;
}

.btn-secondary:hover {
  background-color: #555;
}
</style> 