package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class BaseE2ETest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // URL aplikacji, można pobrać z env
    protected final String BASE_URL = System.getenv().getOrDefault("BASE_URL", "http://localhost:4200");

    /**
     * Inicjalizacja drivera
     *
     * @param headless true = w tle, false = normalny tryb
     */
    protected void initDriver(boolean headless) throws Exception {
        URL driverUrl = getClass().getClassLoader().getResource("msedgedriver.exe");
        if (driverUrl == null) throw new RuntimeException("Nie znaleziono msedgedriver.exe w resources!");

        File driverFile = new File(driverUrl.toURI());
        System.setProperty("webdriver.edge.driver", driverFile.getAbsolutePath());

        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new EdgeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected void quitDriver() {
        if (driver != null) driver.quit();
    }

    /**
     * Otwiera stronę względem BASE_URL
     */
    protected void openApp(String path) {
        driver.get(BASE_URL + path);
    }

    /**
     * Kliknięcie elementu bezpieczne w headless
     */
    protected void click(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
        el.click();
    }

    /**
     * Pobranie tekstu elementu po lokatorze
     */
    protected String getText(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return el.getText();
    }

    protected void type(By by, String text) {
        driver.findElement(by).sendKeys(text);
    }

    protected void loginAs(String email, String password) {
        openApp("/login");
        type(By.id("email"), email);
        type(By.id("password"), password);
        click(By.cssSelector("button[type='submit']"));
    }
}
