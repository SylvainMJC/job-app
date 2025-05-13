import { test, expect } from '@playwright/test';

test('la page d\'accueil devrait être chargée correctement', async ({ page }) => {
  await page.goto('/');
  // Vérifier que le titre de la page contient "Job App"
  await expect(page).toHaveTitle(/Job App/);
});

test('navigation vers la page des offres', async ({ page }) => {
  await page.goto('/');
  // Utiliser un sélecteur plus spécifique pour cibler uniquement le lien de navigation
  await page.locator('.nav-links .nav-item', { hasText: 'Offres' }).click();
  await expect(page).toHaveURL(/.*offres/);
}); 