# Frontend Application

## Technologies

- Vue.js 3.5.x
- Vue Router
- Axios
- Vite
- Vitest
- Playwright
- Tailwind CSS

## Development

### Setup

```bash
npm install
```

### Run Development Server

```bash
npm run dev
```

### Build for Production

```bash
npm run build
```

### Preview Production Build

```bash
npm run preview
```

## Testing

### Unit Tests

Unit tests are implemented using Vitest and Vue Test Utils. These tests verify the functionality of individual components.

To run unit tests:
```bash
npm run test:unit
```

To run unit tests with hot reloading:
```bash
npm run test:unit:watch
```

To generate a coverage report:
```bash
npm run test:unit:coverage
```

### End-to-End Tests

End-to-end tests are implemented using Playwright. These tests verify user flows through the application.

To run E2E tests:
```bash
npm run test:e2e
```

To run E2E tests with a UI:
```bash
npm run test:e2e:ui
```

To debug E2E tests:
```bash
npm run test:e2e:debug
```

## Docker Support

The frontend application can be containerized using the provided Dockerfile. The Docker image uses Nginx to serve the built application.
