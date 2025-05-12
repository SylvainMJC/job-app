<template>
  <div class="offre-details">
    <div v-if="loading">Chargement...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else>
      <!-- Section détails de l'offre -->
      <div class="offre-card">
        <h1>{{ offre.titre }}</h1>
        <div class="offre-metadata">
          <div class="meta-item">
            <i class="icon-building"></i>
            <span>{{ offre.entreprise || 'Entreprise non précisée' }}</span>
          </div>
          <div class="meta-item">
            <i class="icon-location"></i>
            <span>{{ offre.localisation || 'Lieu non précisé' }}</span>
          </div>
          <div class="meta-item">
            <i class="icon-calendar"></i>
            <span>Publiée le {{ formatDate(offre.dateCreation) }}</span>
          </div>
        </div>
        <div class="offre-description">
          <h2>Description du poste</h2>
          <p>{{ offre.description }}</p>
        </div>
        <router-link :to="'/offres'" class="btn btn-secondary">Retour aux offres</router-link>
      </div>

      <!-- Section formulaire de candidature -->
      <div class="application-form">
        <h2>Postuler à cette offre</h2>
        <form @submit.prevent="submitCandidature">
          <div class="form-row">
            <div class="form-group">
              <label for="nom">Nom*</label>
              <input 
                type="text" 
                id="nom" 
                v-model="candidature.nom" 
                required 
                class="form-control"
              >
            </div>
            <div class="form-group">
              <label for="prenom">Prénom*</label>
              <input 
                type="text" 
                id="prenom" 
                v-model="candidature.prenom" 
                required 
                class="form-control"
              >
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="email">Email*</label>
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
          </div>

          <div class="form-group">
            <label for="message">Message / Lettre de motivation*</label>
            <textarea 
              id="message" 
              v-model="candidature.message" 
              rows="5" 
              required 
              class="form-control"
              placeholder="Décrivez votre motivation pour ce poste et pourquoi vous seriez un bon candidat..."
            ></textarea>
          </div>

          <div class="form-actions">
            <div v-if="submitting" class="submitting">Envoi en cours...</div>
            <div v-else-if="success" class="success-message">
              Votre candidature a été envoyée avec succès !
            </div>
            <div v-else-if="submitError" class="error-message">
              {{ submitError }}
            </div>
            <button type="submit" class="btn btn-primary" :disabled="submitting">
              Envoyer ma candidature
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../services/api'

const route = useRoute()
const router = useRouter()
const offreId = route.params.id

// États
const offre = ref({})
const loading = ref(true)
const error = ref(null)
const submitting = ref(false)
const success = ref(false)
const submitError = ref(null)

// Initialisation de la candidature
const candidature = ref({
  nom: '',
  prenom: '',
  email: '',
  telephone: '',
  message: '',
  dateCandidat: new Date().toISOString(),
  offre: { id: parseInt(offreId) }
})

// Chargement des détails de l'offre
const loadOffre = async () => {
  try {
    loading.value = true
    const response = await api.get(`/api/offres/${offreId}`)
    offre.value = response.data
    loading.value = false
  } catch (err) {
    error.value = "Impossible de charger l'offre. Veuillez réessayer."
    loading.value = false
    console.error("Erreur lors du chargement de l'offre:", err)
  }
}

// Soumission du formulaire de candidature
const submitCandidature = async () => {
  try {
    submitting.value = true
    submitError.value = null
    
    // Association de l'offre à la candidature
    candidature.value.offre = { id: parseInt(offreId) }
    
    // Envoi de la candidature à l'API
    await api.post('/api/candidatures', candidature.value)
    
    // Réinitialisation du formulaire et affichage du succès
    success.value = true
    submitting.value = false
    
    // Réinitialisation du formulaire
    candidature.value = {
      nom: '',
      prenom: '',
      email: '',
      telephone: '',
      message: '',
      dateCandidat: new Date().toISOString(),
      offre: { id: parseInt(offreId) }
    }
    
    // Redirection vers la liste des offres après 3 secondes
    setTimeout(() => {
      router.push('/offres')
    }, 3000)
  } catch (err) {
    submitError.value = "Erreur lors de l'envoi de votre candidature. Veuillez réessayer."
    submitting.value = false
    console.error("Erreur lors de l'envoi de la candidature:", err)
  }
}

// Formatage de la date
const formatDate = (dateString) => {
  if (!dateString) return 'Date inconnue'
  const date = new Date(dateString)
  return date.toLocaleDateString('fr-FR', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  })
}

// Chargement de l'offre au montage du composant
onMounted(loadOffre)
</script>

<style scoped>
.offre-details {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem 0;
  width: 100%;
}

.offre-card {
  background: white;
  border-radius: 8px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.offre-card h1 {
  margin-top: 0;
  color: #2c3e50;
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
}

.offre-metadata {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.meta-item {
  display: flex;
  align-items: center;
  color: #666;
}

.meta-item i {
  margin-right: 0.5rem;
  color: #42b983;
}

.offre-description {
  margin-bottom: 1.5rem;
}

.offre-description h2 {
  font-size: 1.3rem;
  margin-bottom: 1rem;
  color: #2c3e50;
}

.application-form {
  background: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.application-form h2 {
  margin-top: 0;
  color: #2c3e50;
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.5rem;
}

.form-row {
  display: flex;
  gap: 1rem;
}

.form-row .form-group {
  flex: 1;
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
  flex-direction: column;
  align-items: flex-end;
  margin-top: 1rem;
}

.success-message {
  color: #28a745;
  margin-bottom: 1rem;
  padding: 0.5rem 1rem;
  background-color: rgba(40, 167, 69, 0.1);
  border-radius: 4px;
  width: 100%;
}

.error-message {
  color: #dc3545;
  margin-bottom: 1rem;
  padding: 0.5rem 1rem;
  background-color: rgba(220, 53, 69, 0.1);
  border-radius: 4px;
  width: 100%;
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

.btn-primary:disabled {
  background-color: #95d5b8;
  cursor: not-allowed;
}

.btn-secondary {
  background-color: #666;
  color: white;
}

.btn-secondary:hover {
  background-color: #555;
}

.error {
  color: #dc3545;
  background-color: rgba(220, 53, 69, 0.1);
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1rem;
}

/* Ajout d'icônes simples avec CSS */
.icon-building::before {
  content: "🏢";
  margin-right: 5px;
}

.icon-location::before {
  content: "📍";
  margin-right: 5px;
}

.icon-calendar::before {
  content: "📅";
  margin-right: 5px;
}

/* Responsive */
@media (max-width: 768px) {
  .form-row {
    flex-direction: column;
    gap: 0;
  }
}
</style> 