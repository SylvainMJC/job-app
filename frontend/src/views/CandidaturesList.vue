<template>
  <div class="candidatures-list">
    <div class="header">
      <h1>Candidatures</h1>
      <router-link to="/candidatures/new" class="btn btn-primary">Nouvelle candidature</router-link>
    </div>

    <div class="candidatures-grid">
      <div v-for="candidature in candidatures" :key="candidature.id" class="candidature-card">
        <h2>{{ candidature.nom }} {{ candidature.prenom }}</h2>
        <p class="email">{{ candidature.email }}</p>
        <p class="offre" v-if="candidature.offre">Offre: {{ candidature.offre.titre || 'Non spécifiée' }}</p>
        <p class="date">Date: {{ formatDate(candidature.dateCandidat) }}</p>
        <div class="actions">
          <router-link :to="`/candidatures/${candidature.id}/edit`" class="btn btn-secondary">Modifier</router-link>
          <button @click="deleteCandidature(candidature.id)" class="btn btn-danger">Supprimer</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../services/api'

const candidatures = ref([])

const loadCandidatures = async () => {
  try {
    const response = await api.get('/api/candidatures')
    candidatures.value = response.data
  } catch (error) {
    console.error('Erreur lors du chargement des candidatures:', error)
  }
}

const deleteCandidature = async (id) => {
  if (!confirm('Êtes-vous sûr de vouloir supprimer cette candidature ?')) return

  try {
    await api.delete(`/api/candidatures/${id}`)
    candidatures.value = candidatures.value.filter(candidature => candidature.id !== id)
  } catch (error) {
    console.error('Erreur lors de la suppression:', error)
  }
}

const formatDate = (dateString) => {
  if (!dateString) return 'Non spécifiée'
  const date = new Date(dateString)
  return date.toLocaleDateString()
}

onMounted(loadCandidatures)
</script>

<style scoped>
.candidatures-list {
  padding: 1rem 0;
  width: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.candidatures-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
  width: 100%;
}

@media (min-width: 768px) {
  .candidatures-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1200px) {
  .candidatures-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

.candidature-card {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.candidature-card h2 {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
}

.email {
  color: #666;
  font-weight: 500;
  margin-bottom: 0.5rem;
}

.offre {
  color: #42b983;
  margin-bottom: 0.5rem;
}

.date {
  color: #666;
  margin-bottom: 1rem;
  flex: 1;
}

.actions {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
  font-size: 0.9rem;
  transition: background-color 0.2s;
  white-space: nowrap;
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