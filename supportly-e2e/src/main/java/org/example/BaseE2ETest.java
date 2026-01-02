package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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

//    protected void initDriver(boolean headless) {
//
//        ChromeOptions options = new ChromeOptions();
//
//        // -- BLOKOWANIE POP-UPÓW Z HASŁAMI --
//        // Tworzymy mapę preferencji, żeby wyłączyć menedżera haseł i detekcję wycieków
//        Map<String, Object> prefs = new HashMap<>();
//        prefs.put("credentials_enable_service", false);  // Wyłącza pytanie "Czy zapisać hasło?"
//        prefs.put("profile.password_manager_enabled", false); // Wyłącza menedżera haseł całkowicie
//        prefs.put("profile.password_manager_leak_detection", false); // Wyłącza to konkretne okno ze zdjęcia (wyciek haseł)
//
//        // Dodajemy preferencje do opcji
//        options.setExperimentalOption("prefs", prefs);
//        // ------------------------------------
//
//        options.addArguments("--remote-allow-origins=*");
//
//        // Opcjonalnie: Dodatkowa flaga wyłączająca funkcje bezpieczeństwa haseł
//        options.addArguments("--disable-features=PasswordLeakDetection");
//
//        if (headless) {
//            options.addArguments("--headless=new");
//            options.addArguments("--disable-gpu");
//            options.addArguments("--no-sandbox");
//            options.addArguments("--disable-dev-shm-usage");
//            options.addArguments("--window-size=1920,1080");
//        } else {
//            options.addArguments("--start-maximized");
//        }
//
//        driver = new ChromeDriver(options);
//        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//    }

//    protected void initDriver(boolean headless) throws Exception {
//        // Konfiguracja EdgeOptions
//
//        EdgeOptions options = new EdgeOptions();
//        if (headless) {
//            options.addArguments("--headless=new");
//            options.addArguments("--disable-gpu");
//            options.addArguments("--no-sandbox");
//            options.addArguments("--disable-dev-shm-usage");
//            options.addArguments("--window-size=1920,1080");
//        }
//
//        // Tworzymy unikalny katalog profilu dla każdej sesji
//        tempUserDataDir = Files.createTempDirectory("edge-profile-").toFile();
//        tempUserDataDir.deleteOnExit();
//        options.addArguments("--user-data-dir=" + tempUserDataDir.getAbsolutePath());
//
//        // Adres hosta, na którym działa EdgeDriver (zmień na swój IP / hostname)
//        String remoteUrl = "http://192.168.0.81:9515";
//
//        // Tworzymy RemoteWebDriver zamiast lokalnego EdgeDriver
//
//        driver = new RemoteWebDriver(new URL(remoteUrl), options);
//        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//    }
    protected void initDriver(boolean headless) throws Exception {
        ChromeOptions options = new ChromeOptions();

        // 1. Pobieramy nazwę klasy, która aktualnie uruchamia test (np. "TaskTest")
        String className = this.getClass().getSimpleName();

        // 2. Tworzymy unikalną ścieżkę: /tmp/chrome_NazwaKlasy_LosowyID
        // Dodajemy UUID, żeby przy kolejnym uruchomieniu Jenkinsa nie było konfliktu "Directory in use"
        String uniqueDirName = "chrome_" + className + "_" + java.util.UUID.randomUUID().toString();

        // Tworzenie katalogu w systemowym folderze tymczasowym (działa na Linux i Windows)
        File tempUserDataDir = Files.createTempDirectory(uniqueDirName).toFile();

        // Ustawienie flagi dla Chrome
        options.addArguments("--user-data-dir=" + tempUserDataDir.getAbsolutePath());

        // --- Reszta Twoich ustawień (bez zmian) ---
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }


        // Konfiguracja Timeoutów klienta
        ClientConfig config = ClientConfig.defaultConfig()
                .readTimeout(Duration.ofMinutes(2))
                .connectionTimeout(Duration.ofSeconds(10));

        // Tworzenie Drivera (z rzutowaniem, o którym mówiliśmy wcześniej)
        driver = (RemoteWebDriver) RemoteWebDriver.builder()
                .oneOf(options)
                .address(new URL("http://192.168.0.81:9515"))
                .config(config)
                .build();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Opcjonalnie: Wypisz w logach, gdzie utworzono profil (dla łatwiejszego debugowania)
        System.out.println("DEBUG: Utworzono profil przeglądarki dla klasy " + className + " w: " + tempUserDataDir.getAbsolutePath());
    }

    protected void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        if (tempUserDataDir != null && tempUserDataDir.exists()) {
            try {
                deleteDirectoryRecursively(tempUserDataDir);
            } catch (Exception ignored) {
            }
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
            } catch (InterruptedException ignored) {
            }
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

    // Pomocnicza metoda, żeby nie powielać kodu wait/clear/sendKeys
    protected void fillInput(String cssSelector, String value) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssSelector)));
        input.clear();
        input.sendKeys(value);
    }

    protected void openPanel() {
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        // Sprawdzamy czy nie jest już otwarty (opcjonalnie)
        // String expanded = panelHeader.getAttribute("aria-expanded");
        // if ("false".equals(expanded)) { panelHeader.click(); }
        panelHeader.click();
    }
}
