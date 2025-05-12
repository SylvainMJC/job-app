<template>
  <div class="offre-detail">
    <div v-if="loading" class="loading">
      Chargement...
    </div>
    <div v-else-if="error" class="error">
      {{ error }}
    </div>
    <div v-else class="content">
      <!-- Détails de l'offre -->
      <div class="offre-card">
        <div class="header-actions">
          <h1>{{ offre.titre }}</h1>
          <div class="actions">
            <router-link :to="`/offres/${offre.id}/edit`" class="btn btn-secondary">Modifier</router-link>
            <router-link to="/offres" class="btn btn-outline">Retour</router-link>
          </div>
        </div>
        <div class="offre-info">
          <div class="info-item">
            <span class="label">Entreprise</span>
            <span class="value">{{ offre.entreprise }}</span>
          </div>
          <div class="info-item">
            <span class="label">Localisation</span>
            <span class="value">{{ offre.localisation }}</span>
          </div>
          <div class="info-item" v-if="offre.dateCreation">
            <span class="label">Date de publication</span>
            <span class="value">{{ formatDate(offre.dateCreation) }}</span>
          </div>
        </div>
        <div class="description">
          <h2>Description</h2>
          <p>{{ offre.description }}</p>
        </div>
      </div>

      <!-- Formulaire de candidature -->
      <div class="candidature-section">
        <h2>Postuler à cette offre</h2>
        <form @submit.prevent="submitCandidature" class="candidature-form">
          <div class="form-group">
            <label for="nom">Nom</label>
            <input type="text" id="nom" v-model="candidature.nom" required class="form-control">
          </div>
          <div class="form-group">
            <label for="prenom">Prénom</label>
            <input type="text" id="prenom" v-model="candidature.prenom" required class="form-control">
          </div>
          <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" v-model="candidature.email" required class="form-control">
          </div>
          <div class="form-group">
            <label for="telephone">Téléphone</label>
            <input type="tel" id="telephone" v-model="candidature.telephone" class="form-control">
          </div>
          <div class="form-group">
            <label for="message">Message / Lettre de motivation</label>
            <textarea id="message" v-model="candidature.message" rows="5" class="form-control"></textarea>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn btn-primary" :disabled="submitting">
              {{ submitting ? 'Envoi en cours...' : 'Envoyer ma candidature' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../services/api'

const route = useRoute()
const router = useRouter()
const offre = ref({})
const loading = ref(true)
const error = ref(null)
const submitting = ref(false)

const candidature = ref({
  nom: '',
  prenom: '',
  email: '',
  telephone: '',
  message: '',
  dateCandidat: new Date().toISOString(),
  offre: null
})

const loadOffre = async () => {
  try {
    loading.value = true
    const response = await api.get(`/api/offres/${route.params.id}`)
    offre.value = response.data
    // Préparer la référence à l'offre pour la candidature
    candidature.value.offre = { id: offre.value.id }
  } catch (err) {
    error.value = "Impossible de charger l'offre. Veuillez réessayer plus tard."
    console.error("Erreur lors du chargement de l'offre:", err)
  } finally {
    loading.value = false
  }
}

const submitCandidature = async () => {
  try {
    submitting.value = true
    await api.post('/api/candidatures', candidature.value)
    alert('Votre candidature a été envoyée avec succès!')
    
    // Rediriger vers la liste des offres ou rester sur la page avec formulaire vidé
    // Option 1: redirection
    // router.push('/offres')
    
    // Option 2: vider le formulaire pour nouvelle candidature
    candidature.value = {
      nom: '',
      prenom: '',
      email: '',
      telephone: '',
      message: '',
      dateCandidat: new Date().toISOString(),
      offre: { id: offre.value.id }
    }
  } catch (err) {
    alert("Une erreur s'est produite lors de l'envoi de votre candidature. Veuillez réessayer.")
    console.error("Erreur lors de l'envoi de la candidature:", err)
  } finally {
    submitting.value = false
  }
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString()
}

onMounted(loadOffre)
</script>

<style scoped>
.offre-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 1rem 0;
}

.loading, .error {
  text-align: center;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.error {
  color: #dc3545;
}

.offre-card {
  background: white;
  border-radius: 8px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}

.header-actions h1 {
  margin: 0;
  color: #2c3e50;
  flex: 1;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.offre-info {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.label {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 0.25rem;
}

.value {
  font-weight: 500;
  color: #2c3e50;
}

.description h2 {
  font-size: 1.2rem;
  margin-bottom: 1rem;
  color: #2c3e50;
}

.description p {
  line-height: 1.6;
  color: #333;
  white-space: pre-line;
}

.candidature-section {
  background: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.candidature-section h2 {
  margin: 0 0 1.5rem 0;
  color: #2c3e50;
  font-size: 1.5rem;
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
  padding: 0.75rem;
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
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
  font-size: 0.9rem;
  transition: all 0.2s;
}

.btn-primary {
  background-color: #42b983;
  color: white;
}

.btn-primary:hover {
  background-color: #3aa876;
}

.btn-primary:disabled {
  background-color: #8bcbad;
  cursor: not-allowed;
}

.btn-secondary {
  background-color: #666;
  color: white;
}

.btn-secondary:hover {
  background-color: #555;
}

.btn-outline {
  background-color: transparent;
  border: 1px solid #ddd;
  color: #666;
}

.btn-outline:hover {
  background-color: #f5f5f5;
}
</style> 