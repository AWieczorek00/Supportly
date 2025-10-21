package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;

public class BaseE2ETest {

    protected RemoteWebDriver driver;
    protected WebDriverWait wait;
    private File tempUserDataDir;
    // URL aplikacji, można pobrać z env
    protected final String BASE_URL = System.getenv().getOrDefault("BASE_URL", "http://localhost:4200");

    /**
     * Inicjalizacja drivera
     *
     * @param headless true = w tle, false = normalny tryb
     */
//    protected void initDriver(boolean headless) throws Exception {
//        URL driverUrl = getClass().getClassLoader().getResource("msedgedriver.exe");
//        if (driverUrl == null) throw new RuntimeException("Nie znaleziono msedgedriver.exe w resources!");
//
//        File driverFile = new File(driverUrl.toURI());
//        driverFile.setExecutable(true);
//        System.setProperty("webdriver.edge.driver", driverFile.getAbsolutePath());
//
//        EdgeOptions options = new EdgeOptions();
//        if (headless) {
//            options.addArguments("--headless=new");  // nowy tryb headless
//            options.addArguments("--disable-gpu");
//            options.addArguments("--no-sandbox");
//            options.addArguments("--disable-dev-shm-usage");
//            options.addArguments("--window-size=1920,1080");
//        }
//
//        driver = new EdgeDriver(options);
//        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//    }

    protected void initDriver(boolean headless) throws Exception {
        // Konfiguracja EdgeOptions

        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        // Tworzymy unikalny katalog profilu dla każdej sesji
        tempUserDataDir = Files.createTempDirectory("edge-profile-").toFile();
        tempUserDataDir.deleteOnExit();
        options.addArguments("--user-data-dir=" + tempUserDataDir.getAbsolutePath());

        // Adres hosta, na którym działa EdgeDriver (zmień na swój IP / hostname)
        String remoteUrl = "http://192.168.0.81:9515";

        // Tworzymy RemoteWebDriver zamiast lokalnego EdgeDriver

        driver = new RemoteWebDriver(new URL(remoteUrl), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        if (tempUserDataDir != null && tempUserDataDir.exists()) {
            try {
                deleteDirectoryRecursively(tempUserDataDir);
            } catch (Exception ignored) {}
        }
    }

    private void deleteDirectoryRecursively(File dir) {
        if (dir == null || !dir.exists()) return;

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteDirectoryRecursively(file);
            }
        }

        for (int i = 0; i < 3; i++) { // 3 próby usunięcia
            if (dir.delete()) {
                return;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
        }

        if (dir.exists()) {
            System.err.println("⚠️ Nie udało się usunąć katalogu: " + dir.getAbsolutePath());
        }
    }

    /**
     * Otwiera stronę względem BASE_URL
     */
    protected void openApp(String path) {
        driver.get(BASE_URL + path + "/");
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
