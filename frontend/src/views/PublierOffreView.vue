<template>
  <div class="publish-container">
    <h1 class="page-title">{{ isEditing ? 'Modifier l\'offre' : 'Publier une offre d\'emploi' }}</h1>
    
    <div class="form-container">
      <form @submit.prevent="submitForm" class="offre-form">
        <div class="form-group">
          <label for="titre">Titre du poste *</label>
          <input 
            type="text" 
            id="titre" 
            v-model="offre.titre" 
            class="form-control" 
            required
            placeholder="Ex: Développeur Full Stack"
          />
        </div>
        
        <div class="form-group">
          <label for="entreprise">Entreprise *</label>
          <input 
            type="text" 
            id="entreprise" 
            v-model="offre.entreprise" 
            class="form-control" 
            required
            placeholder="Nom de votre entreprise"
          />
        </div>
        
        <div class="form-group">
          <label for="lieu">Lieu *</label>
          <input 
            type="text" 
            id="lieu" 
            v-model="offre.lieu" 
            class="form-control" 
            required
            placeholder="Ville, région ou 'Remote'"
          />
        </div>
        
        <div class="form-row">
          <div class="form-group half">
            <label for="type">Type de contrat *</label>
            <select id="type" v-model="offre.type" class="form-control" required>
              <option value="">Sélectionnez</option>
              <option value="CDI">CDI</option>
              <option value="CDD">CDD</option>
              <option value="Stage">Stage</option>
              <option value="Freelance">Freelance</option>
              <option value="Alternance">Alternance</option>
            </select>
          </div>
          
          <div class="form-group half">
            <label for="categorie">Catégorie *</label>
            <select id="categorie" v-model="offre.categorie" class="form-control" required>
              <option value="">Sélectionnez</option>
              <option value="it">Informatique / Tech</option>
              <option value="marketing">Marketing / Communication</option>
              <option value="finance">Finance / Comptabilité</option>
              <option value="rh">Ressources Humaines</option>
              <option value="vente">Vente / Commercial</option>
              <option value="autre">Autre</option>
            </select>
          </div>
        </div>
        
        <div class="form-group">
          <label for="description">Description du poste *</label>
          <textarea 
            id="description" 
            v-model="offre.description" 
            class="form-control" 
            rows="6" 
            required
            placeholder="Décrivez le poste, les missions et responsabilités..."
          ></textarea>
        </div>
        
        <div class="form-group">
          <label for="exigences">Exigences et compétences requises *</label>
          <textarea 
            id="exigences" 
            v-model="offre.exigences" 
            class="form-control" 
            rows="4" 
            required
            placeholder="Compétences techniques, expérience, diplômes nécessaires..."
          ></textarea>
        </div>
        
        <div class="form-group">
          <label for="avantages">Avantages proposés</label>
          <textarea 
            id="avantages" 
            v-model="offre.avantages" 
            class="form-control" 
            rows="3" 
            placeholder="Télétravail, tickets restaurant, RTT, etc."
          ></textarea>
        </div>
        
        <div class="form-actions">
          <button type="button" class="cancel-btn" @click="cancel">Annuler</button>
          <button type="submit" class="submit-btn" :disabled="isSubmitting">
            <span v-if="isSubmitting">Enregistrement...</span>
            <span v-else>{{ isEditing ? 'Mettre à jour' : 'Publier l\'offre' }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import AuthService from '@/services/auth.service';

export default {
  name: 'PublierOffreView',
  setup() {
    const router = useRouter();
    const route = useRoute();
    const isSubmitting = ref(false);
    const currentUser = AuthService.getCurrentUser();
    
    // Récupération de l'ID de l'offre à éditer (si mode édition)
    const offreId = computed(() => route.params.id);
    const isEditing = computed(() => !!offreId.value);
    
    // Initialisation du formulaire
    const offre = ref({
      titre: '',
      entreprise: '',
      lieu: '',
      type: '',
      categorie: '',
      description: '',
      exigences: '',
      avantages: ''
    });
    
    // Charger les détails de l'offre si on est en mode édition
    const loadOffreDetails = async () => {
      if (isEditing.value) {
        try {
          // Dans une application réelle:
          // const response = await apiService.getOffre(offreId.value);
          // offre.value = response.data;
          
          // Simulation pour le prototype
          offre.value = {
            titre: 'Développeur Full Stack',
            entreprise: 'TechCorp',
            lieu: 'Paris',
            type: 'CDI',
            categorie: 'it',
            description: 'Nous recherchons un développeur full stack expérimenté pour rejoindre notre équipe.',
            exigences: 'JavaScript, Vue.js, Node.js, 3 ans d\'expérience minimum',
            avantages: 'Télétravail partiel, tickets restaurant, RTT'
          };
        } catch (error) {
          console.error('Erreur lors du chargement de l\'offre:', error);
          router.push('/mes-offres');
        }
      }
    };
    
    // Soumission du formulaire
    const submitForm = async () => {
      isSubmitting.value = true;
      
      try {
        if (isEditing.value) {
          // Dans une application réelle:
          // await apiService.updateOffre(offreId.value, offre.value);
          console.log('Offre mise à jour:', offre.value);
        } else {
          // Dans une application réelle:
          // await apiService.createOffre(offre.value);
          console.log('Nouvelle offre:', offre.value);
        }
        
        // Redirection après succès
        router.push('/mes-offres');
      } catch (error) {
        console.error('Erreur lors de la soumission:', error);
      } finally {
        isSubmitting.value = false;
      }
    };
    
    // Annulation du formulaire
    const cancel = () => {
      router.push('/mes-offres');
    };
    
    onMounted(() => {
      // Vérifier si l'utilisateur est connecté et a les droits
      if (!currentUser || !(AuthService.isRecruteur() || AuthService.isAdmin())) {
        router.push('/login');
        return;
      }
      
      // Charger les détails de l'offre si on est en mode édition
      loadOffreDetails();
    });
    
    return {
      offre,
      isEditing,
      isSubmitting,
      submitForm,
      cancel
    };
  }
};
</script>

<style scoped>
.publish-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 2rem;
  color: #333;
}

.form-container {
  background: white;
  border-radius: 0.5rem;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.offre-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  width: 100%;
}

.form-row {
  display: flex;
  gap: 1.5rem;
}

.half {
  width: 50%;
}

label {
  font-weight: 500;
  color: #4b5563;
}

.form-control {
  padding: 0.75rem;
  border: 1px solid #e2e8f0;
  border-radius: 0.375rem;
  font-size: 1rem;
}

textarea.form-control {
  resize: vertical;
  min-height: 100px;
}

.form-control:focus {
  outline: none;
  border-color: #a5b4fc;
  box-shadow: 0 0 0 3px rgba(165, 180, 252, 0.3);
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
}

.cancel-btn, .submit-btn {
  padding: 0.75rem 1.5rem;
  border-radius: 0.375rem;
  font-weight: 500;
  cursor: pointer;
  border: none;
}

.cancel-btn {
  background-color: #f3f4f6;
  color: #4b5563;
}

.submit-btn {
  background-color: #4f46e5;
  color: white;
}

.submit-btn:hover {
  background-color: #4338ca;
}

.submit-btn:disabled {
  background-color: #818cf8;
  cursor: not-allowed;
}

@media (max-width: 640px) {
  .form-row {
    flex-direction: column;
    gap: 1.5rem;
  }
  
  .half {
    width: 100%;
  }
}
</style> 