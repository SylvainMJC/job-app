<template>
  <div class="candidature-form">
    <h1>{{ isEdit ? 'Modifier la candidature' : 'Nouvelle candidature' }}</h1>

    <form @submit.prevent="saveCandidature" class="form">
      <div class="form-group">
        <label for="nom">Nom</label>
        <input
          type="text"
          id="nom"
          v-model="candidature.nom"
          required
          class="form-control"
        >
      </div>

      <div class="form-group">
        <label for="prenom">Prénom</label>
        <input
          type="text"
          id="prenom"
          v-model="candidature.prenom"
          required
          class="form-control"
        >
      </div>

      <div class="form-group">
        <label for="email">Email</label>
        <input
          type="email"
          id="email"
          v-model="candidature.email"
          required
          class="form-control"
        >
      </div>

      <div class="form-group">
        <label for="telephone">Téléphone</label>
        <input
          type="tel"
          id="telephone"
          v-model="candidature.telephone"
          class="form-control"
        >
      </div>

      <div class="form-group">
        <label for="offre">Offre</label>
        <select 
          id="offre" 
          v-model="selectedOffreId" 
          class="form-control"
        >
          <option value="">Aucune offre sélectionnée</option>
          <option v-for="offre in offres" :key="offre.id" :value="offre.id">
            {{ offre.titre }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="message">Message / Lettre de motivation</label>
        <textarea
          id="message"
          v-model="candidature.message"
          class="form-control"
          rows="5"
        ></textarea>
      </div>

      <div class="form-actions">
        <router-link to="/candidatures" class="btn btn-secondary">Annuler</router-link>
        <button type="submit" class="btn btn-primary">Enregistrer</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../services/api'

const route = useRoute()
const router = useRouter()

const candidature = ref({
  nom: '',
  prenom: '',
  email: '',
  telephone: '',
  message: '',
  dateCandidat: new Date().toISOString(),
  offre: null
})

const offres = ref([])
const selectedOffreId = ref('')

const isEdit = computed(() => route.params.id !== undefined)

// Charger la liste des offres
const loadOffres = async () => {
  try {
    const response = await api.get('/api/offres')
    offres.value = response.data
  } catch (error) {
    console.error('Erreur lors du chargement des offres:', error)
  }
}

// Charger les détails d'une candidature existante
const loadCandidature = async () => {
  if (!isEdit.value) return

  try {
    const response = await api.get(`/api/candidatures/${route.params.id}`)
    candidature.value = response.data
    
    // Si la candidature a une offre associée, on sélectionne son ID
    if (candidature.value.offre && candidature.value.offre.id) {
      selectedOffreId.value = candidature.value.offre.id
    }
  } catch (error) {
    console.error('Erreur lors du chargement de la candidature:', error)
  }
}

// Mettre à jour l'offre associée quand selectedOffreId change
watch(selectedOffreId, (newVal) => {
  if (newVal) {
    candidature.value.offre = { id: parseInt(newVal) }
  } else {
    candidature.value.offre = null
  }
})

const saveCandidature = async () => {
  try {
    if (isEdit.value) {
      await api.put(`/api/candidatures/${route.params.id}`, candidature.value)
    } else {
      await api.post('/api/candidatures', candidature.value)
    }
    router.push('/candidatures')
  } catch (error) {
    console.error('Erreur lors de l\'enregistrement:', error)
  }
}

onMounted(() => {
  loadOffres()
  loadCandidature()
})
</script>

<style scoped>
.candidature-form {
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