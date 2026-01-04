import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AgreementTest extends TestDatabaseSetup {

    // Globalny długi czas oczekiwania dla wolnego CI
    private WebDriverWait longWait;

    @BeforeEach
    void setup() throws Exception {
        initDriver(true);
        loginAs("super.admin@gmail.com", "123456");
        Thread.sleep(500); // czekaj 0.5 sekundy
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        openApp("/agreement/list");
        Thread.sleep(500); // czekaj 0.5 sekundy// true = headless
    }

    @AfterEach
    void teardown() {
        quitDriver();
    }

    @Test
    @Order(1)
    public void testSearchAgreement() {
        String companyName = "Tech Solutions Sp. z o.o.";

        // Otwieramy panel bezpiecznie (z obsługą animacji)
        openPanelSafe();

        // Bezpieczne wpisywanie (Scroll + Clear + SendKeys + Event)
        fillInputSafe("input[formcontrolname='name']", companyName);

        // Kliknięcie Szukaj
        clickSafe(By.cssSelector("button[type='submit']"));

        // Asercja odporna na przeładowanie tabeli (StaleElement)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        boolean isFound = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("table.mat-mdc-table"), companyName
        ));

        assertTrue(isFound, "Nie znaleziono firmy " + companyName + " w tabeli!");
    }

    @Test
    @Order(2)
    void shouldDisplayAgreementInWholeTable() {
        String companyName = "Tech Solutions Sp. z o.o.";

        // FIX DLA LINUXA: Używamy 'presenceOf' + Scroll, zamiast samej widoczności.
        // Jeśli wiersz jest na dole (poza ekranem), zwykły visibilityOf rzuci błąd.

        // 1. Czekamy aż tabela będzie w DOM
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        // 2. Szukamy wiersza
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//tr[contains(., '" + companyName + "')]")
        ));

        // 3. Scrollujemy do niego (to sprawia, że staje się widoczny)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", row);

        // 4. Asercja
        assertTrue(row.isDisplayed(), "Wiersz z firmą '" + companyName + "' jest w DOM, ale nie jest wyświetlany!");
    }

    @Test
    @Order(3)
    void routToAdd() {
        // Otwieramy panel, w którym jest przycisk (jeśli jest zamknięty)
        openPanelSafe();

        // Klikamy przycisk "Dodaj" (JS click przebija animacje)
        clickSafe(By.cssSelector("button[routerLink='/agreement/add']"));

        // Weryfikacja URL
        wait.until(ExpectedConditions.urlContains("/agreement/add"));
        assertTrue(driver.getCurrentUrl().contains("/agreement/add"));
    }

    @Test
    @Order(4)
    void createAgreement() {
        String newClientName = "GreenData Sp. z o.o.";
        // UWAGA: Szukamy po krótkim prefiksie, ale wybierzemy pierwszą opcję
        String autocompletePrefix = "Gr";

        openApp("/agreement/add");
        sleep(1); // Czekamy na załadowanie komponentów Angulara

        // 1. Otwieramy WSZYSTKIE panele (dla pewności, że pola są widoczne)
        expandAllPanels();

        // 2. Autocomplete Klienta (PANCERNA METODA)
        // input[formControlName='client']
        selectFromAutocompleteSafe("client", autocompletePrefix);

        // 3. Reszta pól - używamy bezpiecznej metody fillInputSafe
        fillInputSafe("input[formControlName='dateFrom']", "2025-10-01");
        fillInputSafe("input[formControlName='dateTo']", "2025-10-31");
        fillInputSafe("input[formControlName='period']", "3");

        // Scrollowanie do pól na dole formularza jest kluczowe!
        fillInputSafe("input[formControlName='costForServicePerHour']", "150");
        fillInputSafe("input[formControlName='agreementNumber']", "AG-2025-001");
        fillInputSafe("input[formControlName='buildingNumber']", "10");
        fillInputSafe("input[formControlName='apartmentNumber']", "1");

        // 4. Zapisz
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));

        // Ważne: Czekamy aż przycisk przestanie być disabled (walidacja formularza)
        wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(addButton, "disabled", "true")));

        clickSafe(addButton);

        // 5. Weryfikacja przekierowania
        wait.until(ExpectedConditions.urlContains("/agreement/list"));

        // Dajemy chwilę bazie danych na zapis (Headless CI bywa wolny)
        sleep(2);

        // 6. Wyszukiwanie na liście
        openPanelSafe();
        fillInputSafe("input[formcontrolname='name']", newClientName);
        clickSafe(By.cssSelector("button[type='submit']"));

        // Czekamy na wynik
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));
        boolean found = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("table.mat-mdc-table"), newClientName
        ));

        assertTrue(found, "Nie znaleziono nowo dodanej firmy '" + newClientName + "'!");
    }

    @Test
    @Order(5)
    void shouldShowNoResultsForNonExistentCompany() {
        if (!driver.getCurrentUrl().contains("/agreement/list")) {
            openApp("/agreement/list");
        }

        openPanelSafe();

        String fakeName = "NonExistent_" + System.currentTimeMillis();
        fillInputSafe("input[formcontrolname='name']", fakeName);

        clickSafe(By.cssSelector("button[type='submit']"));

        // Czekamy chwilę na backend
        sleep(2);

        // Pobieramy wiersze
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tbody tr"));

        // Filtrujemy puste wiersze lub wiersze techniczne (np. nagłówki grupujące)
        long visibleRows = rows.stream()
                .filter(WebElement::isDisplayed)
                .count();

        // Jeśli jest wiersz "Brak danych", to count będzie 1, ale treść specyficzna.
        // Zakładamy tutaj, że tabela po prostu jest pusta lub ma 0 wierszy danych.
        if (visibleRows > 0) {
            // Sprawdźmy czy to nie wiersz "No data"
            String rowText = rows.get(0).getText();
            if(!rowText.toLowerCase().contains("no data") && !rowText.toLowerCase().contains("brak danych")) {
                assertTrue(false, "Znaleziono wiersze dla nieistniejącej firmy: " + rowText);
            }
        }
    }

    // =================================================================================
    //                           PANCERNE METODY POMOCNICZE
    // =================================================================================

    /**
     * Bezpieczne wypełnianie inputa.
     * Scrolluje, czyści, wpisuje i wymusza event Angulara.
     */
    private void fillInputSafe(String cssSelector, String value) {
        By locator = By.cssSelector(cssSelector);

        // 1. Znajdź i scrolluj
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", input);

        wait.until(ExpectedConditions.visibilityOf(input));

        // 2. Kliknij i wyczyść
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
        input.clear();

        // 3. Wpisz wartość
        input.sendKeys(value);

        // 4. FIX ANGULAR: Wymuś zdarzenie 'input', żeby formularz wiedział o zmianie
        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", input);
    }

    /**
     * Bezpieczny klik (Scroll + JS).
     * Działa nawet gdy element jest przykryty spinnerem (JS to ignoruje).
     */
    private void clickSafe(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private void clickSafe(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Autocomplete odporny na Linuxa/Headless.
     * Wybiera PIERWSZĄ opcję z listy zamiast szukać konkretnego tekstu.
     */
    private void selectFromAutocompleteSafe(String formControlName, String value) {
        // Znajdź input
        By inputLocator = By.cssSelector("input[formControlName='" + formControlName + "']");
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));

        // Scroll & Focus
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", input);
        wait.until(ExpectedConditions.visibilityOf(input));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
        input.clear();

        // Wpisz + Event Input
        input.sendKeys(value);
        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", input);

        // Czekaj na listę
        try { sleep(1); } catch (Exception e) {} // Debounce time
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        // Wybierz PIERWSZĄ opcję (niezależnie od tekstu - bezpieczniej)
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-option")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);

        // Zamknij Overlay (Esc)
        input.sendKeys(Keys.ESCAPE);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));
        } catch (Exception e) {}
    }

    /**
     * Otwiera panel wyszukiwania (zakładając, że jest to pierwszy mat-expansion-panel).
     */
    private void openPanelSafe() {
        By headerLoc = By.cssSelector("mat-expansion-panel-header");
        // Używamy presence, bo może być poza ekranem
        WebElement panelHeader = wait.until(ExpectedConditions.presenceOfElementLocated(headerLoc));

        // Scrollujemy do niego
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", panelHeader);

        String expanded = panelHeader.getAttribute("aria-expanded");
        if (expanded == null || "false".equals(expanded)) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", panelHeader);
            sleep(1); // Czekamy na animację otwarcia
        }
    }

    /**
     * Otwiera wszystkie panele na stronie (przydatne przy długich formularzach).
     */
    private void expandAllPanels() {
        List<WebElement> headers = driver.findElements(By.cssSelector("mat-expansion-panel-header"));
        for (WebElement header : headers) {
            String expanded = header.getAttribute("aria-expanded");
            if ("false".equals(expanded)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", header);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", header);
                sleep(1);
            }
        }
    }

    /**
     * Pomocniczy Sleep (dla debugowania i stabilizacji na Linuxie).
     */
    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}