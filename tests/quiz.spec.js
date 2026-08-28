// @ts-check
import { test, expect } from '@playwright/test';

test('complete quiz flow', async ({ page }) => {
  const appUrl = process.env.APPZILLON_URL || 'http://localhost:8086/quizzapp/';

  await page.goto(appUrl);
  await expect(page).toHaveURL(/quizzapp/);

  await page.getByRole('button', { name: 'Get Start' }).click();
  await page.locator('#quizza__GetQuizz__el_btn_1_0').click();

  await page.locator('#quizza__SubmitQuizz__i__answers__selectedAnswer_0').fill('B');
  await page.locator('#quizza__SubmitQuizz__i__answers__selectedAnswer_1').fill('C');
  await page.getByRole('button', { name: 'Submit' }).click();

  await page.getByRole('button', { name: 'Ok' }).click();
  await page.getByRole('button', { name: 'Result' }).click();
});