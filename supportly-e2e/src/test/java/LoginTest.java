import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends TestDatabaseSetup {

    @BeforeEach
    void setUp() throws Exception {
        initDriver(false); // false = normalny tryb (headless = true jeśli chcesz w tle)
        openApp("/login"); // otworzy http://localhost:4200/login jeśli BASE_URL= http://localhost:4200
    }

    @AfterEach
    void tearDown() {
        quitDriver();
    }

    @Test
    void shouldLoginWithCorrectCredentials() {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='email']")));
        emailInput.clear();
        emailInput.sendKeys("super.admin@gmail.com");

        WebElement passwordInput = driver.findElement(By.cssSelector("input[formcontrolname='password']"));
        passwordInput.clear();
        passwordInput.sendKeys("123456");

        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        loginBtn.click();

        boolean isLoginSuccessful = wait.until(ExpectedConditions.or(ExpectedConditions.urlContains("dashboard"), ExpectedConditions.visibilityOfElementLocated(By.tagName("app-navbar"))));

        assertTrue(isLoginSuccessful);
    }

    @Test
    void shouldDisplayLoginViewCorrectly() {
        WebElement welcomeHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Witaj')]")));
        assertTrue(welcomeHeader.isDisplayed());

        WebElement emailInput = driver.findElement(By.cssSelector("input[formcontrolname='email']"));
        assertTrue(emailInput.isDisplayed());

        WebElement passwordInput = driver.findElement(By.cssSelector("input[formcontrolname='password']"));
        assertTrue(passwordInput.isDisplayed());
        assertEquals("password", passwordInput.getAttribute("type"));

        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        assertTrue(loginButton.isDisplayed());
        assertEquals("Zaloguj", loginButton.getText().trim());
    }

    @Test
    void shouldHighlightFieldsAsInvalidOnEmptySubmit() {
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        loginBtn.click();

        WebElement emailInput = driver.findElement(By.cssSelector("input[formcontrolname='email']"));
        emailInput.click();

        WebElement passwordInput = driver.findElement(By.cssSelector("input[formcontrolname='password']"));
        passwordInput.click();

        loginBtn.click();

        boolean isEmailInvalid = wait.until(ExpectedConditions.attributeContains(emailInput, "class", "ng-invalid"));
        boolean isPasswordInvalid = wait.until(ExpectedConditions.attributeContains(passwordInput, "class", "ng-invalid"));

        assertTrue(isEmailInvalid);
        assertTrue(isPasswordInvalid);
    }

//    @Test
//    void shouldShowErrorOnInvalidPassword() {
//        driver.findElement(By.name("email")).sendKeys("test@example.com");
//        driver.findElement(By.name("password")).sendKeys("badpass");
//        click(By.cssSelector("button[type='submit']"));
//
//        String errorMsg = getText(By.cssSelector(".error-message"));
//        assertEquals("Nieprawidłowy login lub hasło", errorMsg);
//    }
//
//    @Test
//    void shouldShowValidationErrorsOnEmptyFields() {
//        click(By.cssSelector("button[type='submit']"));
//
//        String emailError = getText(By.id("email-error"));
//        String passwordError = getText(By.id("password-error"));
//
//        assertEquals("Email jest wymagany", emailError);
//        assertEquals("Hasło jest wymagane", passwordError);
//    }
}
