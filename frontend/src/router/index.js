import { createRouter, createWebHistory } from 'vue-router'
import CandidatureForm from '../views/CandidatureForm.vue'
import CandidaturesList from '../views/CandidaturesList.vue'
import OffreDetails from '../views/OffreDetails.vue'
import OffreForm from '../views/OffreForm.vue'
import OffresList from '../views/OffresList.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/offres'
    },
    {
      path: '/offres',
      name: 'offres',
      component: OffresList
    },
    {
      path: '/offres/new',
      name: 'offre-new',
      component: OffreForm
    },
    {
      path: '/offres/:id',
      name: 'offre-details',
      component: OffreDetails,
      props: true
    },
    {
      path: '/offres/:id/edit',
      name: 'offre-edit',
      component: OffreForm,
      props: true
    },
    {
      path: '/candidatures',
      name: 'candidatures',
      component: CandidaturesList
    },
    {
      path: '/candidatures/new',
      name: 'candidature-new',
      component: CandidatureForm
    },
    {
      path: '/candidatures/:id/edit',
      name: 'candidature-edit',
      component: CandidatureForm,
      props: true
    }
  ]
})

export default router 