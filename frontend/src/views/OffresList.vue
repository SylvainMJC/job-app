<template>
  <div class="offres-list">
    <div class="header">
      <h1>Offres d'emploi</h1>
      <router-link to="/offres/new" class="btn btn-primary">Nouvelle offre</router-link>
    </div>

    <div class="offres-grid">
      <div v-for="offre in offres" :key="offre.id" class="offre-card">
        <h2>{{ offre.titre }}</h2>
        <p class="company">{{ offre.entreprise }}</p>
        <p class="location">{{ offre.localisation }}</p>
        <p class="description">{{ offre.description.substring(0, 150) }}{{ offre.description.length > 150 ? '...' : '' }}</p>
        <div class="actions">
          <router-link :to="`/offres/${offre.id}`" class="btn btn-primary">Voir détails</router-link>
          <router-link :to="`/offres/${offre.id}/edit`" class="btn btn-secondary">Modifier</router-link>
          <button @click="deleteOffre(offre.id)" class="btn btn-danger">Supprimer</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../services/api'

const offres = ref([])

const loadOffres = async () => {
  try {
    const response = await api.get('/api/offres')
    offres.value = response.data
  } catch (error) {
    console.error('Erreur lors du chargement des offres:', error)
  }
}

const deleteOffre = async (id) => {
  if (!confirm('Êtes-vous sûr de vouloir supprimer cette offre ?')) return

  try {
    await api.delete(`/api/offres/${id}`)
    offres.value = offres.value.filter(offre => offre.id !== id)
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
  }
}

onMounted(loadOffres)
</script>

<style scoped>
.offres-list {
  padding: 1rem 0;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.offres-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.offre-card {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.offre-card h2 {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
}

.company {
  color: #666;
  font-weight: 500;
  margin-bottom: 0.5rem;
}

.location {
  color: #666;
  margin-bottom: 1rem;
}

.description {
  color: #666;
  margin-bottom: 1.5rem;
  line-height: 1.5;
}

.actions {
  display: flex;
  gap: 0.5rem;
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

.btn-danger {
  background-color: #dc3545;
  color: white;
}

.btn-danger:hover {
  background-color: #c82333;
}
</style> 