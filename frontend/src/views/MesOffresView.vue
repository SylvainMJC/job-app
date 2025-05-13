<template>
  <div class="mes-offres-container">
    <div class="header-section">
      <h1 class="page-title">Mes offres d'emploi</h1>
      <router-link to="/publier-offre" class="add-btn">Publier une nouvelle offre</router-link>
    </div>
    
    <div class="offres-list" v-if="offres.length > 0">
      <div class="offre-card" v-for="offre in offres" :key="offre.id">
        <div class="offre-header">
          <h2 class="offre-title">{{ offre.titre }}</h2>
          <div class="offre-status" :class="getStatusClass(offre.statut)">
            {{ offre.statut }}
          </div>
        </div>
        
        <div class="offre-details">
          <div class="offre-location">{{ offre.lieu }}</div>
          <div class="offre-date">Publiée le {{ formatDate(offre.datePublication) }}</div>
        </div>
        
        <p class="offre-description">{{ offre.description }}</p>
        
        <div class="offre-stats">
          <div class="stat-item">
            <span class="stat-value">{{ offre.candidatures }}</span>
            <span class="stat-label">Candidatures</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ offre.vues }}</span>
            <span class="stat-label">Vues</span>
          </div>
        </div>
        
        <div class="offre-actions">
          <button class="action-btn view-btn" @click="viewDetails(offre.id)">
            Voir détails
          </button>
          <button class="action-btn edit-btn" @click="editOffer(offre.id)">
            Modifier
          </button>
          <button class="action-btn delete-btn" @click="deleteOffer(offre.id)">
            Supprimer
          </button>
        </div>
      </div>
    </div>
    
    <div class="empty-state" v-else>
      <p>Vous n'avez pas encore publié d'offres d'emploi.</p>
      <router-link to="/publier-offre" class="create-link">Créer une offre</router-link>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import AuthService from '@/services/auth.service';

// Données temporaires pour le prototype
const mockOffres = [
  {
    id: 1,
    titre: 'Développeur Full Stack',
    statut: 'Active',
    lieu: 'Paris',
    datePublication: '2025-04-15T10:30:00',
    description: 'Nous recherchons un développeur full stack expérimenté pour rejoindre notre équipe.',
    candidatures: 12,
    vues: 145
  },
  {
    id: 2,
    titre: 'Chef de Projet Marketing',
    statut: 'Active',
    lieu: 'Lyon',
    datePublication: '2025-05-02T14:45:00',
    description: 'Poste de chef de projet marketing digital pour une société en pleine croissance.',
    candidatures: 8,
    vues: 98
  },
  {
    id: 3,
    titre: 'Analyste Financier',
    statut: 'Expirée',
    lieu: 'Bordeaux',
    datePublication: '2025-03-10T09:15:00',
    description: 'Analyste financier pour accompagner notre développement international.',
    candidatures: 5,
    vues: 72
  }
];

export default {
  name: 'MesOffresView',
  setup() {
    const router = useRouter();
    const offres = ref([]);
    const currentUser = AuthService.getCurrentUser();
    
    const formatDate = (dateString) => {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(dateString).toLocaleDateString('fr-FR', options);
    };
    
    const getStatusClass = (status) => {
      switch(status) {
        case 'Active': return 'status-active';
        case 'En pause': return 'status-paused';
        case 'Expirée': return 'status-expired';
        default: return '';
      }
    };
    
    const viewDetails = (offreId) => {
      router.push(`/offres/${offreId}`);
    };
    
    const editOffer = (offreId) => {
      router.push(`/offres/${offreId}/edit`);
    };
    
    const deleteOffer = (offreId) => {
      if (confirm('Êtes-vous sûr de vouloir supprimer cette offre d\'emploi ?')) {
        // Dans une application réelle:
        // await apiService.deleteOffre(offreId);
        // loadOffres();
        offres.value = offres.value.filter(o => o.id !== offreId);
      }
    };
    
    const loadOffres = async () => {
      // Simulation d'un appel API
      // Dans une application réelle:
      // const response = await apiService.getMesOffres();
      // offres.value = response.data;
      offres.value = mockOffres;
    };
    
    onMounted(() => {
      if (currentUser && (AuthService.isRecruteur() || AuthService.isAdmin())) {
        loadOffres();
      } else {
        router.push('/login');
      }
    });
    
    return {
      offres,
      formatDate,
      getStatusClass,
      viewDetails,
      editOffer,
      deleteOffer
    };
  }
};
</script>

<style scoped>
.mes-offres-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  color: #333;
  margin: 0;
}

.add-btn {
  background-color: #4f46e5;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 0.375rem;
  font-weight: 500;
  text-decoration: none;
  transition: background-color 0.2s;
}

.add-btn:hover {
  background-color: #4338ca;
}

.offres-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.offre-card {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 0.5rem;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.offre-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.offre-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #2d3748;
  margin: 0;
}

.offre-status {
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  font-size: 0.875rem;
  font-weight: 500;
}

.status-active {
  background-color: #dcfce7;
  color: #16a34a;
}

.status-paused {
  background-color: #fef3c7;
  color: #d97706;
}

.status-expired {
  background-color: #f3f4f6;
  color: #6b7280;
}

.offre-details {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1rem;
  color: #6b7280;
  font-size: 0.875rem;
}

.offre-description {
  color: #4b5563;
  margin-bottom: 1.5rem;
  line-height: 1.5;
}

.offre-stats {
  display: flex;
  gap: 2rem;
  margin-bottom: 1.5rem;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 600;
  color: #4f46e5;
}

.stat-label {
  font-size: 0.875rem;
  color: #6b7280;
}

.offre-actions {
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

.create-link {
  display: inline-block;
  margin-top: 1rem;
  color: #4f46e5;
  font-weight: 500;
  text-decoration: none;
}

.create-link:hover {
  text-decoration: underline;
}
</style> 