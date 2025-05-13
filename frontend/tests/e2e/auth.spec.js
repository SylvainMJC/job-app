import { test, expect } from '@playwright/test';

test('formulaire de connexion accessible', async ({ page }) => {
  await page.goto('/login');
  
  // Vérifier que le formulaire de connexion est présent
  await expect(page.getByRole('heading', { name: 'Connexion' })).toBeVisible();
  await expect(page.getByLabel('Email')).toBeVisible();
  await expect(page.getByLabel('Mot de passe')).toBeVisible();
  await expect(page.getByRole('button', { name: 'Se connecter' })).toBeVisible();
});

test('tentative de connexion avec informations incorrectes', async ({ page }) => {
  await page.goto('/login');
  
  // Remplir le formulaire avec de mauvaises informations
  await page.getByLabel('Email').fill('test@example.com');
  await page.getByLabel('Mot de passe').fill('wrong_password');
  await page.getByRole('button', { name: 'Se connecter' }).click();
  
  // Vérifier qu'un message d'erreur apparaît
  await expect(page.getByText('Identifiants invalides')).toBeVisible({ timeout: 10000 });
}); 