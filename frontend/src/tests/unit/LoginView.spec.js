import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import LoginView from '@/views/LoginView.vue'

// Mock the auth service
vi.mock('@/services/auth.service', () => ({
  default: {
    login: vi.fn()
  }
}))

describe('LoginView', () => {
  let wrapper

  beforeEach(() => {
    wrapper = mount(LoginView, {
      global: {
        stubs: ['router-link']
      }
    })
  })

  it('renders properly', () => {
    expect(wrapper.find('h1').text()).toBe('Connexion')
    expect(wrapper.find('label[for="username"]').exists()).toBe(true)
    expect(wrapper.find('label[for="password"]').exists()).toBe(true)
    expect(wrapper.find('button').text()).toContain('Se connecter')
  })

  it('updates username on input', async () => {
    const input = wrapper.find('#username')
    await input.setValue('testuser')
    expect(wrapper.vm.user.username).toBe('testuser')
  })

  it('updates password on input', async () => {
    const input = wrapper.find('#password')
    await input.setValue('password123')
    expect(wrapper.vm.user.password).toBe('password123')
  })

  it('shows error message when fields are empty', async () => {
    await wrapper.find('button').trigger('click')
    expect(wrapper.vm.error).toBe(true)
    expect(wrapper.vm.message).toContain('Veuillez remplir tous les champs')
  })
}) 