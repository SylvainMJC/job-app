<template>
  <div class="admin-container">
    <h1 class="page-title">Administration</h1>
    
    <div class="admin-tabs">
      <button 
        class="tab-btn" 
        :class="{ active: activeTab === 'users' }" 
        @click="activeTab = 'users'"
      >
        Utilisateurs
      </button>
      <button 
        class="tab-btn" 
        :class="{ active: activeTab === 'offers' }" 
        @click="activeTab = 'offers'"
      >
        Offres d'emploi
      </button>
      <button 
        class="tab-btn" 
        :class="{ active: activeTab === 'applications' }" 
        @click="activeTab = 'applications'"
      >
        Candidatures
      </button>
    </div>
    
    <!-- Gestion des utilisateurs -->
    <div v-if="activeTab === 'users'" class="tab-content">
      <div class="filter-bar">
        <input type="text" placeholder="Rechercher un utilisateur..." class="search-input" v-model="userSearchTerm" />
        <select v-model="selectedRole" class="filter-select">
          <option value="">Tous les rôles</option>
          <option value="ADMIN">Admin</option>
          <option value="RECRUTEUR">Recruteur</option>
          <option value="CANDIDAT">Candidat</option>
        </select>
      </div>
      
      <div class="data-table">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nom d'utilisateur</th>
              <th>Email</th>
              <th>Nom</th>
              <th>Prénom</th>
              <th>Rôles</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in filteredUsers" :key="user.id">
              <td>{{ user.id }}</td>
              <td>{{ user.username }}</td>
              <td>{{ user.email }}</td>
              <td>{{ user.nom }}</td>
              <td>{{ user.prenom }}</td>
              <td>
                <span v-for="role in user.roles" :key="role" class="role-badge">
                  {{ role }}
                </span>
              </td>
              <td class="actions-cell">
                <button class="action-btn edit-btn" @click="editUser(user.id)">Modifier</button>
                <button class="action-btn delete-btn" @click="deleteUser(user.id)">Supprimer</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    
    <!-- Gestion des offres -->
    <div v-if="activeTab === 'offers'" class="tab-content">
      <div class="filter-bar">
        <input type="text" placeholder="Rechercher une offre..." class="search-input" v-model="offerSearchTerm" />
        <select v-model="selectedStatus" class="filter-select">
          <option value="">Tous les statuts</option>
          <option value="Active">Active</option>
          <option value="En pause">En pause</option>
          <option value="Expirée">Expirée</option>
        </select>
      </div>
      
      <div class="data-table">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Titre</th>
              <th>Entreprise</th>
              <th>Lieu</th>
              <th>Date de publication</th>
              <th>Statut</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="offer in filteredOffers" :key="offer.id">
              <td>{{ offer.id }}</td>
              <td>{{ offer.titre }}</td>
              <td>{{ offer.entreprise }}</td>
              <td>{{ offer.lieu }}</td>
              <td>{{ formatDate(offer.datePublication) }}</td>
              <td>
                <span class="status-badge" :class="'status-' + offer.statut.toLowerCase().replace(' ', '-')">
                  {{ offer.statut }}
                </span>
              </td>
              <td class="actions-cell">
                <button class="action-btn view-btn" @click="viewOffer(offer.id)">Voir</button>
                <button class="action-btn edit-btn" @click="editOffer(offer.id)">Modifier</button>
                <button class="action-btn delete-btn" @click="deleteOffer(offer.id)">Supprimer</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    
    <!-- Gestion des candidatures -->
    <div v-if="activeTab === 'applications'" class="tab-content">
      <div class="filter-bar">
        <input type="text" placeholder="Rechercher une candidature..." class="search-input" v-model="applicationSearchTerm" />
        <select v-model="selectedApplicationStatus" class="filter-select">
          <option value="">Tous les statuts</option>
          <option value="En attente">En attente</option>
          <option value="Entretien">Entretien</option>
          <option value="Accepté">Accepté</option>
          <option value="Refusé">Refusé</option>
        </select>
      </div>
      
      <div class="data-table">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Candidat</th>
              <th>Offre</th>
              <th>Date de candidature</th>
              <th>Statut</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="application in filteredApplications" :key="application.id">
              <td>{{ application.id }}</td>
              <td>{{ application.candidat }}</td>
              <td>{{ application.offre }}</td>
              <td>{{ formatDate(application.date) }}</td>
              <td>
                <span class="status-badge" :class="'status-' + application.statut.toLowerCase().replace(' ', '-')">
                  {{ application.statut }}
                </span>
              </td>
              <td class="actions-cell">
                <button class="action-btn view-btn" @click="viewApplication(application.id)">Voir</button>
                <button class="action-btn edit-btn" @click="updateApplicationStatus(application.id)">Statut</button>
                <button class="action-btn delete-btn" @click="deleteApplication(application.id)">Supprimer</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import AuthService from '@/services/auth.service';

// Mock data for prototype
const mockUsers = [
  { id: 1, username: 'admin', email: 'admin@jobapp.com', nom: 'Admin', prenom: 'System', roles: ['ADMIN'] },
  { id: 2, username: 'recruteur', email: 'recruteur@jobapp.com', nom: 'Dupont', prenom: 'Jean', roles: ['RECRUTEUR'] },
  { id: 3, username: 'candidat', email: 'candidat@jobapp.com', nom: 'Martin', prenom: 'Sophie', roles: ['CANDIDAT'] }
];

const mockOffers = [
  { id: 1, titre: 'Développeur Full Stack', entreprise: 'TechCorp', lieu: 'Paris', datePublication: '2025-04-15T10:30:00', statut: 'Active' },
  { id: 2, titre: 'Chef de Projet Marketing', entreprise: 'MarketPro', lieu: 'Lyon', datePublication: '2025-05-02T14:45:00', statut: 'Active' },
  { id: 3, titre: 'Analyste Financier', entreprise: 'FinanceGroup', lieu: 'Bordeaux', datePublication: '2025-03-10T09:15:00', statut: 'Expirée' }
];

const mockApplications = [
  { id: 1, candidat: 'Sophie Martin', offre: 'Développeur Full Stack - TechCorp', date: '2025-05-10T14:30:00', statut: 'En attente' },
  { id: 2, candidat: 'Pierre Dubois', offre: 'Chef de Projet Marketing - MarketPro', date: '2025-05-08T11:45:00', statut: 'Entretien' },
  { id: 3, candidat: 'Marie Leroy', offre: 'Analyste Financier - FinanceGroup', date: '2025-04-20T09:15:00', statut: 'Refusé' }
];

export default {
  name: 'AdminView',
  setup() {
    const router = useRouter();
    const currentUser = AuthService.getCurrentUser();
    
    // Tab management
    const activeTab = ref('users');
    
    // Users management
    const users = ref([]);
    const userSearchTerm = ref('');
    const selectedRole = ref('');
    
    // Offers management
    const offers = ref([]);
    const offerSearchTerm = ref('');
    const selectedStatus = ref('');
    
    // Applications management
    const applications = ref([]);
    const applicationSearchTerm = ref('');
    const selectedApplicationStatus = ref('');
    
    // Format date
    const formatDate = (dateString) => {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(dateString).toLocaleDateString('fr-FR', options);
    };
    
    // Filtered data
    const filteredUsers = computed(() => {
      return users.value.filter(user => {
        const matchesSearch = userSearchTerm.value === '' || 
          user.username.toLowerCase().includes(userSearchTerm.value.toLowerCase()) ||
          user.email.toLowerCase().includes(userSearchTerm.value.toLowerCase()) ||
          user.nom.toLowerCase().includes(userSearchTerm.value.toLowerCase()) ||
          user.prenom.toLowerCase().includes(userSearchTerm.value.toLowerCase());
        
        const matchesRole = selectedRole.value === '' || 
          user.roles.includes(selectedRole.value);
        
        return matchesSearch && matchesRole;
      });
    });
    
    const filteredOffers = computed(() => {
      return offers.value.filter(offer => {
        const matchesSearch = offerSearchTerm.value === '' || 
          offer.titre.toLowerCase().includes(offerSearchTerm.value.toLowerCase()) ||
          offer.entreprise.toLowerCase().includes(offerSearchTerm.value.toLowerCase());
        
        const matchesStatus = selectedStatus.value === '' || 
          offer.statut === selectedStatus.value;
        
        return matchesSearch && matchesStatus;
      });
    });
    
    const filteredApplications = computed(() => {
      return applications.value.filter(application => {
        const matchesSearch = applicationSearchTerm.value === '' || 
          application.candidat.toLowerCase().includes(applicationSearchTerm.value.toLowerCase()) ||
          application.offre.toLowerCase().includes(applicationSearchTerm.value.toLowerCase());
        
        const matchesStatus = selectedApplicationStatus.value === '' || 
          application.statut === selectedApplicationStatus.value;
        
        return matchesSearch && matchesStatus;
      });
    });
    
    // User actions
    const editUser = (userId) => {
      // In a real app, navigate to user edit page
      console.log('Editing user', userId);
    };
    
    const deleteUser = (userId) => {
      if (confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
        users.value = users.value.filter(user => user.id !== userId);
      }
    };
    
    // Offer actions
    const viewOffer = (offerId) => {
      router.push(`/offres/${offerId}`);
    };
    
    const editOffer = (offerId) => {
      router.push(`/offres/${offerId}/edit`);
    };
    
    const deleteOffer = (offerId) => {
      if (confirm('Êtes-vous sûr de vouloir supprimer cette offre ?')) {
        offers.value = offers.value.filter(offer => offer.id !== offerId);
      }
    };
    
    // Application actions
    const viewApplication = (applicationId) => {
      // In a real app, navigate to application details
      console.log('Viewing application', applicationId);
    };
    
    const updateApplicationStatus = (applicationId) => {
      // In a real app, show a status update modal
      const newStatus = prompt('Nouveau statut (En attente, Entretien, Accepté, Refusé):', '');
      if (newStatus) {
        applications.value = applications.value.map(app => {
          if (app.id === applicationId) {
            return { ...app, statut: newStatus };
          }
          return app;
        });
      }
    };
    
    const deleteApplication = (applicationId) => {
      if (confirm('Êtes-vous sûr de vouloir supprimer cette candidature ?')) {
        applications.value = applications.value.filter(app => app.id !== applicationId);
      }
    };
    
    // Load data
    const loadData = () => {
      // In a real app, these would be API calls
      users.value = mockUsers;
      offers.value = mockOffers;
      applications.value = mockApplications;
    };
    
    onMounted(() => {
      // Check if user is admin
      if (!currentUser || !AuthService.isAdmin()) {
        router.push('/');
        return;
      }
      
      loadData();
    });
    
    return {
      activeTab,
      users,
      offers,
      applications,
      userSearchTerm,
      offerSearchTerm,
      applicationSearchTerm,
      selectedRole,
      selectedStatus,
      selectedApplicationStatus,
      filteredUsers,
      filteredOffers,
      filteredApplications,
      formatDate,
      editUser,
      deleteUser,
      viewOffer,
      editOffer,
      deleteOffer,
      viewApplication,
      updateApplicationStatus,
      deleteApplication
    };
  }
};
</script>

<style scoped>
.admin-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 2rem;
  color: #333;
}

.admin-tabs {
  display: flex;
  border-bottom: 1px solid #e2e8f0;
  margin-bottom: 2rem;
}

.tab-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  background: transparent;
  font-weight: 500;
  color: #6b7280;
  cursor: pointer;
  border-bottom: 2px solid transparent;
}

.tab-btn.active {
  color: #4f46e5;
  border-bottom: 2px solid #4f46e5;
}

.tab-content {
  margin-top: 1.5rem;
}

.filter-bar {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.search-input, .filter-select {
  padding: 0.75rem;
  border: 1px solid #e2e8f0;
  border-radius: 0.375rem;
}

.search-input {
  flex: 1;
}

.data-table {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.875rem;
}

th, td {
  padding: 0.75rem 1rem;
  text-align: left;
}

th {
  background-color: #f9fafb;
  font-weight: 600;
  color: #4b5563;
  border-bottom: 1px solid #e5e7eb;
}

td {
  border-bottom: 1px solid #e5e7eb;
}

tr:hover td {
  background-color: #f8fafc;
}

.role-badge, .status-badge {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
  margin-right: 0.25rem;
}

.role-badge {
  background-color: #e0e7ff;
  color: #4338ca;
}

.status-active {
  background-color: #dcfce7;
  color: #16a34a;
}

.status-en-pause {
  background-color: #fef3c7;
  color: #d97706;
}

.status-expirée {
  background-color: #f3f4f6;
  color: #6b7280;
}

.status-en-attente {
  background-color: #dbeafe;
  color: #2563eb;
}

.status-entretien {
  background-color: #e0e7ff;
  color: #4f46e5;
}

.status-accepté {
  background-color: #dcfce7;
  color: #16a34a;
}

.status-refusé {
  background-color: #fee2e2;
  color: #dc2626;
}

.actions-cell {
  white-space: nowrap;
}

.action-btn {
  padding: 0.25rem 0.5rem;
  margin-right: 0.25rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
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

@media (max-width: 768px) {
  .admin-tabs {
    overflow-x: auto;
    white-space: nowrap;
  }
  
  .filter-bar {
    flex-direction: column;
  }
}
</style> 