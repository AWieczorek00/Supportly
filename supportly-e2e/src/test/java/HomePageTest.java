import org.example.BaseE2ETest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class HomePageTest extends BaseE2ETest {

    @BeforeEach
    void setup() throws Exception {
        initDriver(true); // true = headless, false = normalny tryb
    }

    @AfterEach
    void teardown() {
        quitDriver();
    }


    @Test
    void shouldDisplayMainPageAfterLogin() {
        // 🔑 najpierw logowanie
        loginAs("super.admin@gmail.com", "123456");

        // teraz jesteśmy na stronie głównej
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Tablica linków do sprawdzenia
        String[] links = {"Zlecenia", "Zadania", "Pracownicy", "Umowy"};

        for (String linkText : links) {
            WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(linkText)));
            assertTrue(link.isDisplayed(), linkText + " powinien być widoczny");
        }
    }
}