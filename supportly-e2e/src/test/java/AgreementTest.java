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

//    @Test
//    public void testSearchAgreement() {
//        String companyName = "Tech Solutions Sp. z o.o.";
//
//        // ... (kod otwierania panelu i wpisywania tekstu bez zmian) ...
//        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
//        panelHeader.click();
//
//        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']")));
//        nameInput.clear();
//        nameInput.sendKeys(companyName);
//
//        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
//        searchButton.click();
//
//        // --- POPRAWKA PONIŻEJ ---
//
//        // Strategia: Czekamy, aż w DOM pojawi się jakikolwiek wiersz tabeli (tr),
//        // który zawiera w sobie szukany tekst.
//        // Używamy kropki (.) w XPath, co oznacza "tekst wewnątrz tego elementu lub jego dzieci".
//        String dynamicXPath = String.format("//tr[contains(., '%s')]", companyName);
//
//        try {
//            WebElement foundRow = wait.until(
//                    ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath))
//            );
//
//            // Opcjonalnie: dodatkowa weryfikacja (np. czy wiersz jest wyświetlony)
//            assertTrue(foundRow.isDisplayed(), "Znaleziony wiersz nie jest widoczny!");
//
//        } catch (TimeoutException e) {
//            // Debugowanie: Jeśli nadal pada, sprawdź co jest w tabeli w momencie błędu
//            System.out.println("Timeout! Nie znaleziono wiersza z tekstem: " + companyName);
//            // Możesz tu pobrać tekst całej tabeli, żeby zobaczyć co faktycznie się wczytało
//            String tableText = driver.findElement(By.cssSelector("table.mat-mdc-table")).getText();
//            System.out.println("Aktualna zawartość tabeli: \n" + tableText);
//            throw e; // Rzuć błąd dalej, żeby test był czerwony
//        }
//    }

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


//    @Test
//    void shouldDisplayAgreementInWholeTable() throws InterruptedException {
//        // Czekamy aż tabela pojawi się w DOM

    /// /        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.mat-table")));
//
//        // Pobieramy wszystkie wiersze w tabeli
//        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
//
//        boolean found = false;
//        for (WebElement row : rows) {
//            System.out.println(row.getText());
//            if (row.getText().contains("Tech Solutions Sp. z o.o.")) {
//                found = true;
//                break;
//            }
//        }
//
//        assertTrue(found, "Nie znaleziono firmy 'Tech Solutions Sp. z o.o.' w tabeli!");
//    }
//
//    @Test
//    void routToAdd() {
//
//        WebElement panelHeader = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
//        );
//        panelHeader.click();
//
//        WebElement addAgreementButton = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("button[routerLink='/agreement/add']"))
//        );
//        addAgreementButton.click();
//
//        assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/agreement/add"));
//
//    }
//
//    @Test
//    void createAgreement() throws InterruptedException {
//        openApp("/agreement/add");
//        Thread.sleep(500); // czekaj 0.5 sekundy
//
//        // znajdź wszystkie nagłówki paneli
//        List<WebElement> panelHeaders = wait.until(
//                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("mat-expansion-panel-header"))
//        );
//
//        for (WebElement panelHeader : panelHeaders) {
//            wait.until(ExpectedConditions.elementToBeClickable(panelHeader)).click();
//            Thread.sleep(300); // mała przerwa, żeby UI zdążyło się rozwinąć
//        }
//
//        WebElement companyInput = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='client']"))
//        );
//        companyInput.sendKeys("Gr");
//
//        List<WebElement> options = wait.until(
//                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("mat-option"))
//        );
//
//        for (WebElement option : options) {
//            if (option.getText().contains("Gr")) {
//                option.click();
//                break;
//            }
//        }
//
//        WebElement startDate = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='dateFrom']"))
//        );
//        startDate.sendKeys("2025-10-01");
//
//        WebElement endDate = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='dateTo']"))
//        );
//        endDate.sendKeys("2025-10-31");
//
//        WebElement period = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='period']"))
//        );
//        period.sendKeys("3");
//
//        WebElement costPerHour = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='costForServicePerHour']"))
//        );
//        costPerHour.sendKeys("150");
//
//        WebElement agreementNumber = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='agreementNumber']"))
//        );
//        agreementNumber.sendKeys("AG-2025-001");
//
//        WebElement buildingNumber = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='buildingNumber']"))
//        );
//        buildingNumber.sendKeys("10");
//
//        WebElement apartmentNumber = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='apartmentNumber']"))
//        );
//        apartmentNumber.sendKeys("1");
//
//        WebElement add = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
//        );
//        add.click();
//
//        Thread.sleep(500);
//
//        WebElement panelHeader = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
//        );
//        panelHeader.click();
//
//        // Czekamy aż inputy staną się widoczne
//        WebElement nameInput = wait.until(
//                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']"))
//        );
//        nameInput.sendKeys("GreenData Sp. z o.o.");
//
//        // Klikamy przycisk Szukaj
//        WebElement searchButton = wait.until(
//                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
//        );
//        searchButton.click();
//
//
//        // Czekamy aż tabela się pojawi
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table tr[mat-row]")));
//
//        // Pobieramy wiersze tabeli
//        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
//
//        Thread.sleep(500);
//
//        boolean found = rows.stream()
//                .anyMatch(row -> row.getText().contains("GreenData Sp. z o.o."));
//
//        assertTrue(found, "Znaleziono firmy 'GreenData Sp. z o.o.' w tabeli!");
//
//
//    }
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

    @Test
    @Order(4)
    void createAgreement() {
        String newClientName = "GreenData Sp. z o.o.";
        String autocompletePrefix = "Gr";

        // 1. Wejście na stronę
        openApp("/agreement/add");

        // 2. Otwieranie WSZYSTKICH paneli na stronie (Dane, Finanse itp.)
        // Angular Material często chowa pola w zwiniętych akordeonach.
        List<WebElement> panelHeaders = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("mat-expansion-panel-header"))
        );

        for (WebElement header : panelHeaders) {
            // FIX: Sprawdzamy po atrybucie aria-expanded (to najpewniejszy sposób w Angularze)
            if (!"true".equals(header.getAttribute("aria-expanded"))) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", header);
                // Krótki oddech dla silnika renderującego przeglądarki
                try { Thread.sleep(200); } catch (InterruptedException e) {}
            }
        }

        // 3. Autocomplete Klienta
        WebElement companyInput = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='client']"))
        );
        companyInput.clear();
        companyInput.sendKeys(autocompletePrefix);

        // Czekamy na kontener opcji
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        // FIX: Zamiast strumienia, szukamy konkretnego elementu XPath-em i klikamy JS-em
        WebElement targetOption = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//mat-option[contains(., '" + autocompletePrefix + "')]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetOption);

        // Ważne: Czekamy aż lista zniknie, żeby nie zasłaniała pól poniżej!
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        // 4. Reszta pól
        fillInput("input[formControlName='dateFrom']", "2025-10-01");
        fillInput("input[formControlName='dateTo']", "2025-10-31");
        fillInput("input[formControlName='period']", "3");
        fillInput("input[formControlName='costForServicePerHour']", "150");
        fillInput("input[formControlName='agreementNumber']", "AG-2025-001");
        fillInput("input[formControlName='buildingNumber']", "10");
        fillInput("input[formControlName='apartmentNumber']", "1");

        // 5. Zapis (JS Click)
        WebElement addButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit']"))
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);

        // Czekamy na przekierowanie
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/add")));

        // 6. Wyszukiwanie na liście
        // FIX: Otwieramy panel wyszukiwania bezpiecznie (bez toggle)
        WebElement searchPanelHeader = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("mat-expansion-panel-header"))
        );

        if (!"true".equals(searchPanelHeader.getAttribute("aria-expanded"))) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchPanelHeader);
        }

        // Czekamy na input (musi być visible, żeby wpisać tekst)
        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']"))
        );
        nameInput.clear();
        nameInput.sendKeys(newClientName);

        WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );
        searchButton.click(); // Tu zwykły click zazwyczaj działa, ale można zmienić na JS

        // 7. Weryfikacja
        // Używamy textToBePresentInElementLocated - jest bardziej odporne na odświeżanie tabeli
        boolean found = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("table.mat-mdc-table"), newClientName
        ));

        assertTrue(found, "Nie znaleziono nowo dodanej firmy '" + newClientName + "' w tabeli!");
    }


    @Test
    @Order(5)
    void shouldShowNoResultsForNonExistentCompany() {
        // 1. Otwórz panel wyszukiwania
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        panelHeader.click();

        // 2. Wpisz bzdury
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']")));
        nameInput.clear();
        nameInput.sendKeys("Firma Która Nie Istnieje 12345XYZ");

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        searchButton.click();

        // 3. Weryfikacja
        // Opcja A: Tabela jest pusta
        // Czekamy chwilę, żeby upewnić się, że stary wynik zniknął (jeśli był)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-spinner"))); // Opcjonalne, jeśli masz loader

        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        // Opcja B: Sprawdzamy czy wyświetla się komunikat "Brak danych" (częste w Angular Material)
        // WebElement noDataRow = driver.findElement(By.cssSelector(".no-data-row"));

        assertTrue(rows.isEmpty(), "Tabela powinna być pusta dla nieistniejącej firmy!");
    }

}
