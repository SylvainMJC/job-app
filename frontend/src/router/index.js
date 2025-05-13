import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import AuthService from '@/services/auth.service'
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
      name: 'home',
      component: HomeView
    },
    {
      path: '/offres',
      name: 'offres',
      component: () => import('@/views/OffresView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/mes-candidatures',
      name: 'mes-candidatures',
      component: () => import('@/views/MesCandidaturesView.vue'),
      meta: { requiresAuth: true, roles: ['CANDIDAT', 'ADMIN'] }
    },
    {
      path: '/mes-offres',
      name: 'mes-offres',
      component: () => import('@/views/MesOffresView.vue'),
      meta: { requiresAuth: true, roles: ['RECRUTEUR', 'ADMIN'] }
    },
    {
      path: '/publier-offre',
      name: 'publier-offre',
      component: () => import('@/views/PublierOffreView.vue'),
      meta: { requiresAuth: true, roles: ['RECRUTEUR', 'ADMIN'] }
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('@/views/AdminView.vue'),
      meta: { requiresAuth: true, roles: ['ADMIN'] }
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

// Navigation guard pour vérifier l'authentification et les rôles
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const currentUser = AuthService.getCurrentUser()
  
  if (requiresAuth && !currentUser) {
    next('/login')
  } else if (requiresAuth && to.meta.roles) {
    // Vérifier les rôles
    const hasRole = to.meta.roles.some(role => 
      currentUser.roles.includes(role)
    )
    
    if (hasRole) {
      next()
    } else {
      next('/')
    }
  } else {
    next()
  }
})

export default router 