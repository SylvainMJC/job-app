<template>
  <div class="candidatures-container">
    <h1 class="page-title">Mes candidatures</h1>
    
    <div class="candidatures-list" v-if="candidatures.length > 0">
      <div class="candidature-card" v-for="candidature in candidatures" :key="candidature.id">
        <div class="candidature-header">
          <h2 class="candidature-job-title">{{ candidature.offre.titre }}</h2>
          <span class="candidature-status" :class="getStatusClass(candidature.statut)">
            {{ candidature.statut }}
          </span>
        </div>
        
        <div class="candidature-company">{{ candidature.offre.entreprise }}</div>
        <div class="candidature-date">Postulé le {{ formatDate(candidature.datePostulation) }}</div>
        
        <div class="candidature-details">
          <p class="candidature-motivation">{{ candidature.motivation }}</p>
        </div>
        
        <div class="candidature-actions">
          <button class="action-btn view-btn" @click="viewOffer(candidature.offre.id)">
            Voir l'offre
          </button>
          <button class="action-btn edit-btn" @click="editApplication(candidature.id)" 
                  v-if="canEdit(candidature)">
            Modifier
          </button>
          <button class="action-btn delete-btn" @click="deleteApplication(candidature.id)"
                  v-if="canDelete(candidature)">
            Annuler
          </button>
        </div>
      </div>
    </div>
    
    <div class="empty-state" v-else>
      <p>Vous n'avez pas encore de candidatures.</p>
      <router-link to="/offres" class="browse-link">Parcourir les offres</router-link>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import AuthService from '@/services/auth.service';

// Données temporaires pour le prototype
const mockCandidatures = [
  {
    id: 1,
    offre: {
      id: 1,
      titre: 'Développeur Full Stack',
      entreprise: 'TechCorp'
    },
    statut: 'En attente',
    datePostulation: '2025-05-10T14:30:00',
    motivation: 'Je suis très intéressé par ce poste qui correspond parfaitement à mes compétences et aspirations professionnelles.'
  },
  {
    id: 2,
    offre: {
      id: 3,
      titre: 'Analyste Financier',
      entreprise: 'FinanceGroup'
    },
    statut: 'Entretien',
    datePostulation: '2025-05-05T09:15:00',
    motivation: 'Avec mon expérience dans le secteur financier, je pense pouvoir apporter une vraie valeur ajoutée à votre équipe.'
  },
  {
    id: 3,
    offre: {
      id: 2,
      titre: 'Chef de Projet Marketing',
      entreprise: 'MarketPro'
    },
    statut: 'Refusé',
    datePostulation: '2025-04-28T11:45:00',
    motivation: 'Mon expérience en gestion de projets marketing me qualifie pour ce poste exigeant mais passionnant.'
  }
];

export default {
  name: 'MesCandidaturesView',
  setup() {
    const router = useRouter();
    const candidatures = ref([]);
    const currentUser = AuthService.getCurrentUser();
    
    const formatDate = (dateString) => {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(dateString).toLocaleDateString('fr-FR', options);
    };
    
    const getStatusClass = (status) => {
      switch(status) {
        case 'En attente': return 'status-pending';
        case 'Entretien': return 'status-interview';
        case 'Accepté': return 'status-accepted';
        case 'Refusé': return 'status-rejected';
        default: return '';
      }
    };
    
    const canEdit = (candidature) => {
      return candidature.statut === 'En attente';
    };
    
    const canDelete = (candidature) => {
      return candidature.statut === 'En attente' || candidature.statut === 'Entretien';
    };
    
    const viewOffer = (offreId) => {
      router.push(`/offres/${offreId}`);
    };
    
    const editApplication = (candidatureId) => {
      router.push(`/candidatures/${candidatureId}/edit`);
    };
    
    const deleteApplication = (candidatureId) => {
      if (confirm('Êtes-vous sûr de vouloir annuler cette candidature ?')) {
        // Dans une application réelle:
        // await apiService.deleteCandidature(candidatureId);
        // loadCandidatures();
        candidatures.value = candidatures.value.filter(c => c.id !== candidatureId);
      }
    };
    
    const loadCandidatures = async () => {
      // Simulation d'un appel API
      // Dans une application réelle:
      // const response = await apiService.getMesCandidatures();
      // candidatures.value = response.data;
      candidatures.value = mockCandidatures;
    };
    
    onMounted(() => {
      if (currentUser) {
        loadCandidatures();
      } else {
        router.push('/login');
      }
    });
    
    return {
      candidatures,
      formatDate,
      getStatusClass,
      canEdit,
      canDelete,
      viewOffer,
      editApplication,
      deleteApplication
    };
  }
};
</script>

<style scoped>
.candidatures-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 2rem;
  color: #333;
}

.candidatures-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.candidature-card {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 0.5rem;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.candidature-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.candidature-job-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #2d3748;
}

.candidature-status {
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  font-size: 0.875rem;
  font-weight: 500;
}

.status-pending {
  background-color: #fef3c7;
  color: #d97706;
}

.status-interview {
  background-color: #dbeafe;
  color: #3b82f6;
}

.status-accepted {
  background-color: #dcfce7;
  color: #16a34a;
}

.status-rejected {
  background-color: #fee2e2;
  color: #dc2626;
}

.candidature-company {
  font-weight: 500;
  color: #4a5568;
  margin-bottom: 0.25rem;
}

.candidature-date {
  color: #718096;
  font-size: 0.875rem;
  margin-bottom: 1rem;
}

.candidature-details {
  margin-bottom: 1.5rem;
}

.candidature-motivation {
  color: #4a5568;
  line-height: 1.5;
}

.candidature-actions {
  display: flex;
  gap: 0.75rem;
}

.action-btn {
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  font-weight: 500;
  cursor: pointer;
  border: none;
}

.view-btn {
  background-color: #f3f4f6;
  color: #4b5563;
}

.edit-btn {
  background-color: #dbeafe;
  color: #3b82f6;
}

.delete-btn {
  background-color: #fee2e2;
  color: #dc2626;
}

.empty-state {
  text-align: center;
  padding: 3rem;
  color: #718096;
}

.browse-link {
  display: inline-block;
  margin-top: 1rem;
  color: #4f46e5;
  font-weight: 500;
  text-decoration: none;
}

.browse-link:hover {
  text-decoration: underline;
}
</style> 