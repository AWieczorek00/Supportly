import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeTest extends TestDatabaseSetup {

    @BeforeEach
    void setup() throws Exception {
        initDriver(true);
        loginAs("super.admin@gmail.com", "123456");
        Thread.sleep(500); // czekaj 0.5 sekundy
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        openApp("/employee/list");
        Thread.sleep(500); // czekaj 0.5 sekundy// true = headless
    }



    @AfterEach
    void teardown() {
        try {
            // Zrzut ekranu tylko w przypadku błędu (opcjonalne)
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.createDirectories(Paths.get("target/screenshots"));
            Files.copy(screenshot.toPath(), Paths.get("target/screenshots/last_run.png"));
        } catch (Exception ignored) {
        }
        quitDriver();
    }

    @Test
    void shouldDisplayAgreementInWholeTable() {
        String employeeName = "SuperAdmin";

        // Czekamy na tabelę + wiersz (z obsługą Scrolla w tle przez Selenium 4)
        // Jeśli tabela jest długa, wiersz może być na dole - presence + scroll to naprawia.
        WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        // Szukamy wiersza
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//tr[contains(., '" + employeeName + "')]")
        ));

        // Scrollujemy do niego, żeby stał się widoczny
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", row);

        assertTrue(row.isDisplayed(), "Nie znaleziono pracownika '" + employeeName + "' w tabeli!");
    }

    @Test
    public void testSearchEmployee() {
        String searchLastName = "SuperAdmin";

        openPanelSafe();

        // Bezpieczne wpisywanie (scroll + event input)
        fillInputSafe("input[formcontrolname='lastName']", searchLastName);

        clickSafe(By.cssSelector("button[type='submit']"));

        // Czekamy na filtrację (liczba wierszy = 1)
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("table.mat-mdc-table tr[mat-row]"), 1));

        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
        assertTrue(rows.get(0).getText().contains(searchLastName), "Wiersz nie zawiera szukanego nazwiska!");
    }

//    @Test
    public void createEmployee() throws InterruptedException {

        // 1️⃣ Otwórz panel z pracownikami
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        panelHeader.click();

        // 2️⃣ Kliknij przycisk "Dodaj nowego pracownika"
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Dodaj nowego pracownika')]")));
        addButton.click();

        // 3️⃣ Poczekaj aż otworzy się dialog
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-dialog-container")));

        // 4️⃣ Funkcja pomocnicza do bezpiecznego wpisywania tekstu
        BiConsumer<WebElement, String> safeType = (element, text) -> {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            element.clear();
            element.sendKeys(text);
            wait.until(d -> element.getAttribute("value").equals(text));
        };

        // 5️⃣ Wypełnij pola formularza
        safeType.accept(dialog.findElement(By.cssSelector("input[formControlName='firstName']")), "Justyna");
        safeType.accept(dialog.findElement(By.cssSelector("input[formControlName='lastName']")), "Nita");
        safeType.accept(dialog.findElement(By.cssSelector("input[formControlName='phoneNumber']")), "502254567");

        // 6️⃣ Wybierz rolę
        WebElement roleSelect = dialog.findElement(By.cssSelector("mat-select[formcontrolname='role']"));
        wait.until(ExpectedConditions.elementToBeClickable(roleSelect)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.cdk-overlay-pane")));
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("mat-option .mdc-list-item__primary-text")));
        for (WebElement opt : options) {
            if (opt.getText().trim().equals("ADMIN")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);
                break;
            }
        }

        // 7️⃣ Kliknij przycisk "Zapisz" w dialogu
        WebElement saveButton = dialog.findElement(By.cssSelector("button.mat-mdc-unelevated-button.mat-primary"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);

        // 8️⃣ Poczekaj, aż tabela pracowników się pojawi
        FluentWait<RemoteWebDriver> fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofSeconds(1)).ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class);

        boolean found = fluentWait.until(d -> {
            List<WebElement> rows = d.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
            if (rows.isEmpty()) return false;

            for (WebElement row : rows) {
                // tylko text, bez sprawdzania isDisplayed (dla wirtualizowanej tabeli)
                if (row.getText().contains("Nita")) return true;
            }
            return false;
        });

        assertTrue(found, "Nie znaleziono nowego pracownika 'Nita' w tabeli!");
    }

//    @Test
//    public void createEmployee() {
//        String uniqueLastName = "Nita_" + System.currentTimeMillis();
//        String uniqueEmail = "jan.nita." + System.currentTimeMillis() + "@test.pl";
//
//        // 1. Otwórz panel
//        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
//        if (!"true".equals(panelHeader.getAttribute("aria-expanded"))) {
//            panelHeader.click();
//        }
//
//        // 2. Kliknij "Dodaj"
//        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Dodaj nowego pracownika')]")));
//        addButton.click();
//
//        // 3. Czekaj na Dialog
//        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("mat-dialog-container")));
//
//        // 4. Wypełnij WSZYSTKIE wymagane pola
//        // Używamy wait.until przy każdym polu dla stabilności
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formControlName='firstName']"))).sendKeys("Justyna");
//        dialog.findElement(By.cssSelector("input[formControlName='lastName']")).sendKeys(uniqueLastName);
//        dialog.findElement(By.cssSelector("input[formControlName='phoneNumber']")).sendKeys("502254567");
//
//        // --- NOWOŚĆ: Wypełnij Email i Hasło (często wymagane!) ---
//        // Jeśli w Twoim formularzu nie ma pola password, usuń tę linię, ale email na pewno jest potrzebny.
//        try {
//            dialog.findElement(By.cssSelector("input[formControlName='email']")).sendKeys(uniqueEmail);
//            dialog.findElement(By.cssSelector("input[formControlName='password']")).sendKeys("Haslo123!");
//        } catch (Exception e) {
//            // Ignorujemy, jeśli pól nie ma, ale zazwyczaj są wymagane
//            System.out.println("Nie znaleziono pola email lub hasło - pomijam.");
//        }
//
//        // 5. Rola (Select)
//        WebElement roleSelect = dialog.findElement(By.cssSelector("mat-select[formcontrolname='role']"));
//        roleSelect.click();
//
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));
//        driver.findElement(By.xpath("//mat-option[contains(., 'ADMIN')]")).click();
//
//        // Czekamy chwilę, aż overlay zniknie, żeby nie zasłonił przycisku Zapisz
//        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));
//
//        // 6. Zapisz
//        WebElement saveButton = dialog.findElement(By.xpath(".//button[contains(., 'Zapisz')]"));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);
//
//        // 7. Czekaj aż dialog zniknie (potwierdzenie sukcesu)
//        try {
//            wait.until(ExpectedConditions.invisibilityOf(dialog));
//        } catch (TimeoutException e) {
//            // --- DEBUGOWANIE BŁĘDU ---
//            // Jeśli tu trafiliśmy, to znaczy, że okno się nie zamknęło.
//            // Pobieramy komunikaty błędów z formularza i drukujemy je w logach Jenkinsa.
//
//            System.err.println("!!! BŁĄD WALIDACJI FORMULARZA !!!");
//            List<WebElement> errors = dialog.findElements(By.cssSelector("mat-error, .mat-error, .text-danger"));
//            for (WebElement error : errors) {
//                System.err.println("Treść błędu widoczna na ekranie: " + error.getText());
//            }
//
//            // Zrób screenshot (opcjonalnie, jeśli masz mechanizm w BaseTest)
//            // takeScreenshot("createEmployee_fail");
//
//            throw new RuntimeException("Dialog nie zamknął się po kliknięciu Zapisz. Prawdopodobnie błąd walidacji. Sprawdź logi powyżej.");
//        }
//
//        // 8. Opcjonalne odświeżenie listy
//        try {
//            WebElement searchBtn = driver.findElement(By.xpath("//button[contains(., 'Szukaj')]"));
//            searchBtn.click();
//        } catch (Exception ignored) {}
//
//        // 9. Weryfikacja
//        boolean isFound = wait.until(ExpectedConditions.textToBePresentInElementLocated(
//                By.cssSelector("table.mat-mdc-table"), uniqueLastName
//        ));
//
//        assertTrue(isFound, "Nie znaleziono pracownika: " + uniqueLastName);
//    }

    @Test
    public void testClearSearchCriteria() {
        openPanelSafe();

        // Wpisz dane
        fillInputSafe("input[formcontrolname='firstName']", "TestImię");
        fillInputSafe("input[formcontrolname='lastName']", "TestNazwisko");
        fillInputSafe("input[formcontrolname='phoneNumber']", "123456789");

        // Kliknij Wyczyść
        clickSafe(By.xpath("//button[contains(., 'Wyczyść')]"));

        // Weryfikacja pustych pól
        WebElement nameInput = driver.findElement(By.cssSelector("input[formcontrolname='firstName']"));
        wait.until(ExpectedConditions.textToBePresentInElementValue(nameInput, ""));

        assertTrue(nameInput.getAttribute("value").isEmpty(), "Pole Imię nie wyczyszczone!");
        assertTrue(driver.findElement(By.cssSelector("input[formcontrolname='lastName']")).getAttribute("value").isEmpty());
    }

    @Test
    public void testSearchByPhoneNumber() {
        String phoneNumberToSearch = "000000000";

        openPanelSafe();

        fillInputSafe("input[formcontrolname='phoneNumber']", phoneNumberToSearch);
        clickSafe(By.xpath("//button[contains(., 'Szukaj')]"));

        // Czekamy na 1 wiersz
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("table.mat-mdc-table tr[mat-row]"), 1));

        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
        assertTrue(rows.get(0).getText().contains(phoneNumberToSearch),
                "Znaleziony wiersz nie zawiera numeru: " + phoneNumberToSearch);
    }

    // =================================================================================
    //                           METODY PANCERNE (LINUX READY)
    // =================================================================================

    private void loginSafe(String email, String password) {
        // initDriver() już otworzył stronę, więc czekamy na inputy
        fillInputSafe("input[type='email']", email); // lub formControlName='email'
        fillInputSafe("input[type='password']", password);

        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        clickSafe(loginBtn);

        // Czekaj na zmianę URL (sukces)
        try {
            wait.until(ExpectedConditions.urlContains("/employee")); // lub /dashboard
        } catch (TimeoutException e) {
            System.out.println("! URL się nie zmienił, ale próbuję kontynuować.");
        }
    }

    private void fillInputSafe(String cssSelector, String value) {
        By locator = By.cssSelector(cssSelector);
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", input);
        wait.until(ExpectedConditions.visibilityOf(input));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
        input.clear();
        input.sendKeys(value);
        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", input);
    }

    private void clickSafe(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        clickSafe(element);
    }

    private void clickSafe(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private void openPanelSafe() {
        By headerLoc = By.cssSelector("mat-expansion-panel-header");
        WebElement panelHeader = wait.until(ExpectedConditions.presenceOfElementLocated(headerLoc));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", panelHeader);

        String expanded = panelHeader.getAttribute("aria-expanded");
        if (expanded == null || "false".equals(expanded)) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", panelHeader);
            sleep(1);
        }
    }

    private void selectFromDropdownSafe(String formControlName, String optionText) {
        // Kliknij w select
        WebElement select = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("mat-select[formControlName='" + formControlName + "']")));
        clickSafe(select);

        // Czekaj na overlay
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        // Wybierz opcję (szukamy po tekście)
        By optionLoc = By.xpath("//mat-option[contains(., '" + optionText + "')]");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLoc));
        clickSafe(option);

        // Czekaj na zamknięcie
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
