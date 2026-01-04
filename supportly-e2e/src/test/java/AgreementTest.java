import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AgreementTest extends TestDatabaseSetup {

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

//

    @Test
    @Order(1)
    public void testSearchAgreement() {
        String companyName = "Tech Solutions Sp. z o.o.";

        // Otwarcie panelu (użyj nowej, bezpiecznej metody openPanel() jeśli ją masz)
        openPanel();

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']")));
        nameInput.clear();
        nameInput.sendKeys(companyName);

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        searchButton.click();

        // --- FIX STALE ELEMENT ---
        // Zamiast szukać konkretnego wiersza (który znika i pojawia się przy renderowaniu),
        // czekamy, aż kontener tabeli będzie stabilnie zawierał tekst.

        // 1. Czekamy na widoczność tabeli
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        // 2. Czekamy na obecność tekstu w tabeli (Selenium samo ponawia próbę przy StaleElement)
        boolean isFound = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("table.mat-mdc-table"), companyName
        ));

        assertTrue(isFound, "Nie znaleziono firmy " + companyName + " w tabeli!");
    }

    @Test
    @Order(2)
    void shouldDisplayAgreementInWholeTable() {
        String companyName = "Tech Solutions Sp. z o.o.";

        // ZAMIAST pętli po wierszach i sleepów:
        // Czekamy, aż w DOM pojawi się wiersz (tr), który zawiera szukany tekst.
        // To jedno polecenie załatwia czekanie na tabelę ORAZ szukanie tekstu.
        boolean isRowVisible = wait.until(ExpectedConditions.and(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[contains(., '" + companyName + "')]"))
        ));

        assertTrue(isRowVisible, "Nie znaleziono firmy '" + companyName + "' w tabeli!");
    }

    @Test
    @Order(3)
    void routToAdd() {
        // 1. Otwórz panel (np. wyszukiwania/akcji), w którym jest przycisk
        WebElement panelHeader = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("mat-expansion-panel-header"))
        );

        // FIX: Klikamy tylko jeśli jest zamknięty + używamy JS
        if (!"true".equals(panelHeader.getAttribute("aria-expanded"))) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", panelHeader);
            // Czekamy chwilę na animację (pojawienie się przycisku w DOM)
            try { Thread.sleep(300); } catch (InterruptedException e) {}
        }

        // 2. Kliknij przycisk "Dodaj"
        // Używamy presenceOfElementLocated, bo przycisk może być w DOM, ale "nieklikalny" dla Selenium przez animację
        WebElement addAgreementButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[routerLink='/agreement/add']"))
        );

        // FIX: JS Click przebija się przez spinnery i overlaye
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addAgreementButton);

        // 3. Weryfikacja
        wait.until(ExpectedConditions.urlContains("/agreement/add"));
        assertTrue(driver.getCurrentUrl().contains("/agreement/add"));
    }
    // --- METODA POMOCNICZA (Dodaj ją do klasy) ---
    // Bezpiecznie otwiera panel tylko wtedy, gdy jest zamknięty
    private void ensurePanelIsOpen(By headerSelector) {
        WebElement panelHeader = wait.until(ExpectedConditions.presenceOfElementLocated(headerSelector));

        // Sprawdzamy stan panelu przed kliknięciem
        // Angular Material ustawia klasę "mat-expanded" lub atrybut "aria-expanded"
        String expansionState = panelHeader.getAttribute("aria-expanded");

        if (expansionState == null || "false".equals(expansionState)) {
            // Panel zamknięty -> klikamy (JS jest pewniejszy przy animacjach)
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", panelHeader);

            // Czekamy chwilę na animację otwarcia (Angular animations)
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }
    }

    @Test
    @Order(4)
    void createAgreement() {
        String newClientName = "GreenData Sp. z o.o.";
        String autocompletePrefix = "Gr"; // Wpisujemy tylko początek

        // 1. Wejście na stronę
        openApp("/agreement/add");

        // 2. Otwieranie WSZYSTKICH paneli (używamy nowej metody pomocniczej)
        List<WebElement> panelHeaders = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("mat-expansion-panel-header"))
        );
        // Iterujemy po indeksach, bo referencje do elementów mogą się zgubić przy zmianach DOM
        for (int i = 0; i < panelHeaders.size(); i++) {
            // Pobieramy listę od nowa, żeby uniknąć StaleElementReferenceException
            List<WebElement> headers = driver.findElements(By.cssSelector("mat-expansion-panel-header"));
            if (i < headers.size()) {
                String isExpanded = headers.get(i).getAttribute("aria-expanded");
                if (!"true".equals(isExpanded)) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", headers.get(i));
                    try { Thread.sleep(300); } catch (InterruptedException e) {}
                }
            }
        }

        // 3. Autocomplete Klienta - POPRAWKA GŁÓWNA
        WebElement companyInput = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='client']"))
        );
        companyInput.clear();
        companyInput.sendKeys(autocompletePrefix);

        // KLUCZOWE: Angular ma debounceTime (np. 300ms) zanim wyśle request do backendu.
        // Selenium działa w 1ms. Musimy poczekać, aż Angular "zrozumie", że przestaliśmy pisać.
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        // Czekamy na pojawienie się kontenera opcji (overlay)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        // Czekamy na konkretną opcję. Używamy 'contains' ale czekamy na VISIBILITY, nie tylko presence.
        // Jeśli backend nie zwróci "Gr...", ten wait rzuci czytelny wyjątek.
        WebElement targetOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//mat-option//span[contains(text(), '" + autocompletePrefix + "')] | //mat-option[contains(., '" + autocompletePrefix + "')]")
        ));

        // Klikamy JS-em, żeby uniknąć problemów z przysłanianiem elementu
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetOption);

        // Upewniamy się, że dropdown zniknął
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        // 4. Reszta pól
        fillInput("input[formControlName='dateFrom']", "2025-10-01");
        fillInput("input[formControlName='dateTo']", "2025-10-31");
        fillInput("input[formControlName='period']", "3");
        fillInput("input[formControlName='costForServicePerHour']", "150");
        fillInput("input[formControlName='agreementNumber']", "AG-2025-001");
        fillInput("input[formControlName='buildingNumber']", "10");
        fillInput("input[formControlName='apartmentNumber']", "1");

        // 5. Zapis
        WebElement addButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit']"))
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);

        // Czekamy na przekierowanie (zmianę URL)
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/add")));

        // Opcjonalnie: Dajmy chwilę na załadowanie listy
        wait.until(ExpectedConditions.urlContains("/agreement/list"));

        // 6. Wyszukiwanie na liście
        // Otwieramy panel bezpiecznie
        ensurePanelIsOpen(By.cssSelector("mat-expansion-panel-header"));

        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']"))
        );
        nameInput.clear();
        nameInput.sendKeys(newClientName);

        WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );
        searchButton.click();

        // 7. Weryfikacja
        boolean found = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("table.mat-mdc-table"), newClientName
        ));

        assertTrue(found, "Nie znaleziono nowo dodanej firmy '" + newClientName + "' w tabeli!");
    }

    @Test
    @Order(5)
    void shouldShowNoResultsForNonExistentCompany() {
        // Upewniamy się, że jesteśmy na liście (jeśli poprzedni test się wywalił, to nas ratuje)
        if (!driver.getCurrentUrl().contains("/agreement/list")) {
            openApp("/agreement/list");
        }

        // 1. Otwórz panel wyszukiwania (BEZPIECZNIE)
        // Błąd w logach sugerował, że input nie był widoczny -> panel był zamknięty
        ensurePanelIsOpen(By.cssSelector("mat-expansion-panel-header"));

        // 2. Wpisz bzdury
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']")));
        nameInput.clear();
        nameInput.sendKeys("Firma Która Nie Istnieje " + System.currentTimeMillis()); // Unikalna nazwa

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        searchButton.click();

        // 3. Weryfikacja
        // Musimy poczekać aż tabela się przeładuje.
        // Najlepszy sposób to poczekać, aż spinner zniknie (jeśli jest) lub sprawdzić brak wierszy

        try {
            // Dajemy chwilę na request do backendu
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tbody tr"));

        // Filtrujemy, żeby ignorować nagłówki lub puste wiersze techniczne Angulara
        long dataRows = rows.stream()
                .filter(r -> r.getAttribute("mat-row") != null || r.getText().length() > 0)
                .count();

        // Jeśli Angular wyświetla specjalny wiersz "No data matching the filter", musisz to uwzględnić
        // Ale zazwyczaj tabela po prostu jest pusta.
        assertTrue(dataRows == 0, "Tabela powinna być pusta dla nieistniejącej firmy! Znaleziono wierszy: " + dataRows);
    }

}
