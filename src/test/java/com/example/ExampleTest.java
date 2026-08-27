package com.example;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

public class ExampleTest {

    @Test
    void quizAppFlow() {

        System.out.println("1. Test started");

        Playwright playwright = Playwright.create();

        try {

            System.out.println("2. Playwright created");

            Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                    .setHeadless(false)
            );

            System.out.println("3. Browser launched");

            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            System.out.println("4. Page created");

            // =========================
            // OPEN APPLICATION
            // =========================

            page.navigate("http://localhost:8086/quizzapp/");

            System.out.println("5. Quiz application opened");
            System.out.println("URL: " + page.url());

            page.waitForTimeout(3000);

            // =========================
            // GET START
            // =========================

            page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                    .setName("Get Start")
            ).click();

            System.out.println("6. Get Start clicked");

            page.waitForTimeout(2000);

            // =========================
            // SELECT QUIZ
            // =========================

            page.locator(
                "#quizza__GetQuizz__el_btn_1_0"
            ).click();

            System.out.println("7. Quiz selected");

            page.waitForTimeout(2000);

            // =========================
            // ENTER ANSWER 1
            // =========================

            page.locator(
                "#quizza__SubmitQuizz__i__answers__selectedAnswer_0"
            ).fill("B");

            System.out.println("8. Answer 1 entered");

            // =========================
            // ENTER ANSWER 2
            // =========================

            page.locator(
                "#quizza__SubmitQuizz__i__answers__selectedAnswer_1"
            ).fill("C");

            System.out.println("9. Answer 2 entered");

            // =========================
            // SUBMIT
            // =========================

            page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                    .setName("Submit")
            ).click();

            System.out.println("10. Submit clicked");

            page.waitForTimeout(2000);

            // =========================
            // OK
            // =========================

            page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                    .setName("Ok")
            ).click();

            System.out.println("11. OK clicked");

            page.waitForTimeout(2000);

            // =========================
            // RESULT
            // =========================

            page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                    .setName("Result")
            ).click();

            System.out.println("12. Result clicked");

            page.waitForTimeout(2000);

            System.out.println("13. Quiz flow completed successfully");

            browser.close();

        } finally {

            playwright.close();

            System.out.println("15. Playwright closed");
        }
    }
}