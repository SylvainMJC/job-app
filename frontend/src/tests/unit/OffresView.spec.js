import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import OffresView from '@/views/OffresView.vue'

// Mock vue-router
const mockRouter = {
  push: vi.fn()
}

vi.mock('vue-router', () => ({
  useRouter: () => mockRouter
}))

describe('OffresView', () => {
  let wrapper

  beforeEach(() => {
    // Reset mocks
    vi.clearAllMocks()
    
    wrapper = mount(OffresView)
  })

  it('renders properly', () => {
    expect(wrapper.find('.page-title').text()).toBe('Offres d\'emploi')
    expect(wrapper.find('.search-input').exists()).toBe(true)
    expect(wrapper.find('.filter-select').exists()).toBe(true)
  })

  it('filters offres based on search term', async () => {
    // Initially should show all mock offres
    expect(wrapper.findAll('.offre-card').length).toBe(3)
    
    // Set search term
    await wrapper.find('.search-input').setValue('Full Stack')
    
    // Should now only show matching offres
    expect(wrapper.findAll('.offre-card').length).toBe(1)
    expect(wrapper.find('.offre-title').text()).toContain('Full Stack')
  })

  it('filters offres based on category', async () => {
    await wrapper.find('.filter-select').setValue('it')
    
    // Should only show IT category offres
    expect(wrapper.findAll('.offre-card').length).toBe(1)
    expect(wrapper.find('.offre-title').text()).toContain('Full Stack')
  })

  it('navigates to details page when clicking view details', async () => {
    await wrapper.find('.details-btn').trigger('click')
    
    // Should call router.push with the correct route
    expect(mockRouter.push).toHaveBeenCalledWith('/offres/1')
  })
}) 