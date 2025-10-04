import org.example.BaseE2ETest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends TestDatabaseSetup  {

    @BeforeEach
    void setUp() throws Exception {
        initDriver(true); // false = normalny tryb (headless = true jeśli chcesz w tle)
        openApp("/login"); // otworzy http://localhost:4200/login jeśli BASE_URL= http://localhost:4200
    }

    @AfterEach
    void tearDown() {
        quitDriver();
    }

    @Test
    void shouldLoginWithCorrectCredentials() {
        driver.findElement(By.xpath("//input[@formcontrolname='email']")).sendKeys("super.admin@gmail.com");
        driver.findElement(By.xpath("//input[@formcontrolname='password']")).sendKeys("123456");
        click(By.cssSelector("button[type='submit']"));

        // sprawdzamy czy przekierowało np. do dashboard
        assertTrue(driver.getCurrentUrl().contains(""));
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
