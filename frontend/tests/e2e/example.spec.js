import { test, expect } from '@playwright/test';

test('la page d\'accueil devrait être chargée correctement', async ({ page }) => {
  await page.goto('/');
  // Vérifier que le titre de la page contient "Job App"
  await expect(page).toHaveTitle(/Job App/);
});

test('navigation vers la page des offres', async ({ page }) => {
  await page.goto('/');
  // Cliquer sur le lien "Offres" si disponible
  // Si le lien n'existe pas encore, vous devrez adapter ce test
  await page.getByRole('link', { name: 'Offres' }).click();
  await expect(page).toHaveURL(/.*offres/);
}); 