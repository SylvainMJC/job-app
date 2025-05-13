<template>
  <div class="offres-container">
    <h1 class="page-title">Offres d'emploi</h1>
    
    <div class="filters">
      <input type="text" placeholder="Rechercher..." class="search-input" v-model="searchTerm" />
      <select class="filter-select" v-model="selectedCategory">
        <option value="">Toutes les catégories</option>
        <option value="it">Informatique</option>
        <option value="marketing">Marketing</option>
        <option value="finance">Finance</option>
      </select>
    </div>
    
    <div class="offres-list" v-if="offres.length > 0">
      <div class="offre-card" v-for="offre in filteredOffres" :key="offre.id">
        <h2 class="offre-title">{{ offre.titre }}</h2>
        <div class="offre-company">{{ offre.entreprise }}</div>
        <div class="offre-location">{{ offre.lieu }}</div>
        <p class="offre-description">{{ offre.description }}</p>
        <div class="offre-footer">
          <span class="offre-type">{{ offre.type }}</span>
          <button class="details-btn" @click="viewDetails(offre.id)">Voir détails</button>
        </div>
      </div>
    </div>
    
    <div class="empty-state" v-else>
      <p>Aucune offre d'emploi disponible pour le moment.</p>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';

// Données temporaires pour le prototype
const mockOffres = [
  {
    id: 1,
    titre: 'Développeur Full Stack',
    entreprise: 'TechCorp',
    lieu: 'Paris',
    description: 'Nous recherchons un développeur full stack expérimenté pour rejoindre notre équipe.',
    type: 'CDI',
    categorie: 'it'
  },
  {
    id: 2,
    titre: 'Chef de Projet Marketing',
    entreprise: 'MarketPro',
    lieu: 'Lyon',
    description: 'Poste de chef de projet marketing digital pour une société en pleine croissance.',
    type: 'CDI',
    categorie: 'marketing'
  },
  {
    id: 3,
    titre: 'Analyste Financier',
    entreprise: 'FinanceGroup',
    lieu: 'Bordeaux',
    description: 'Analyste financier pour accompagner notre développement international.',
    type: 'CDD',
    categorie: 'finance'
  }
];

export default {
  name: 'OffresView',
  setup() {
    const router = useRouter();
    const offres = ref(mockOffres);
    const searchTerm = ref('');
    const selectedCategory = ref('');
    
    // Filtrer les offres selon les critères
    const filteredOffres = computed(() => {
      return offres.value.filter(offre => {
        const matchesSearch = searchTerm.value === '' || 
          offre.titre.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
          offre.description.toLowerCase().includes(searchTerm.value.toLowerCase());
        
        const matchesCategory = selectedCategory.value === '' || 
          offre.categorie === selectedCategory.value;
        
        return matchesSearch && matchesCategory;
      });
    });
    
    // Dans une application réelle, nous chargerions les données depuis l'API
    const loadOffres = async () => {
      // Simulation d'un appel API
      // Dans une application réelle:
      // const response = await apiService.getOffres();
      // offres.value = response.data;
      offres.value = mockOffres;
    };
    
    const viewDetails = (id) => {
      router.push(`/offres/${id}`);
    };
    
    onMounted(() => {
      loadOffres();
    });
    
    return {
      offres,
      searchTerm,
      selectedCategory,
      filteredOffres,
      viewDetails
    };
  }
};
</script>

<style scoped>
.offres-container {
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

.filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}

.search-input, .filter-select {
  padding: 0.75rem;
  border: 1px solid #e2e8f0;
  border-radius: 0.375rem;
  flex: 1;
}

.offres-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
}

.offre-card {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 0.5rem;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s, box-shadow 0.2s;
}

.offre-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.offre-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #2d3748;
}

.offre-company {
  font-weight: 500;
  color: #4a5568;
  margin-bottom: 0.25rem;
}

.offre-location {
  color: #718096;
  margin-bottom: 1rem;
}

.offre-description {
  color: #4a5568;
  margin-bottom: 1.5rem;
  line-height: 1.5;
}

.offre-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.offre-type {
  background-color: #ebf4ff;
  color: #3182ce;
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  font-size: 0.875rem;
}

.details-btn {
  background-color: #4f46e5;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.details-btn:hover {
  background-color: #4338ca;
}

.empty-state {
  text-align: center;
  padding: 3rem;
  color: #718096;
}
</style> 