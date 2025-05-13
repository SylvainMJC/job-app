// Vitest setup file
import { vi } from 'vitest'

// Mock the router globally
vi.mock('vue-router', () => ({
  useRouter: () => ({
    push: vi.fn(),
    replace: vi.fn()
  }),
  useRoute: () => ({
    params: {},
    query: {},
    path: '/'
  })
}))

// Add any other global test setup here 