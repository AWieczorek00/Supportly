import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;


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

    @Test
    public void testSearchAgreement() {
        String companyName = "Tech Solutions Sp. z o.o.";

        // ... (kod otwierania panelu i wpisywania tekstu bez zmian) ...
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        panelHeader.click();

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']")));
        nameInput.clear();
        nameInput.sendKeys(companyName);

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        searchButton.click();

        // --- POPRAWKA PONIŻEJ ---

        // Strategia: Czekamy, aż w DOM pojawi się jakikolwiek wiersz tabeli (tr),
        // który zawiera w sobie szukany tekst.
        // Używamy kropki (.) w XPath, co oznacza "tekst wewnątrz tego elementu lub jego dzieci".
        String dynamicXPath = String.format("//tr[contains(., '%s')]", companyName);

        try {
            WebElement foundRow = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath))
            );

            // Opcjonalnie: dodatkowa weryfikacja (np. czy wiersz jest wyświetlony)
            assertTrue(foundRow.isDisplayed(), "Znaleziony wiersz nie jest widoczny!");

        } catch (TimeoutException e) {
            // Debugowanie: Jeśli nadal pada, sprawdź co jest w tabeli w momencie błędu
            System.out.println("Timeout! Nie znaleziono wiersza z tekstem: " + companyName);
            // Możesz tu pobrać tekst całej tabeli, żeby zobaczyć co faktycznie się wczytało
            String tableText = driver.findElement(By.cssSelector("table.mat-mdc-table")).getText();
            System.out.println("Aktualna zawartość tabeli: \n" + tableText);
            throw e; // Rzuć błąd dalej, żeby test był czerwony
        }
    }


//    @Test
//    void shouldDisplayAgreementInWholeTable() throws InterruptedException {
//        // Czekamy aż tabela pojawi się w DOM
////        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.mat-table")));
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
    void routToAdd() {
        WebElement panelHeader = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
        );
        panelHeader.click();

        WebElement addAgreementButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[routerLink='/agreement/add']"))
        );
        addAgreementButton.click();

        // Czekamy na zmianę URL, a nie tylko sprawdzamy go natychmiast
        wait.until(ExpectedConditions.urlContains("/agreement/add"));

        assertTrue(driver.getCurrentUrl().contains("/agreement/add"));
    }

    @Test
    void createAgreement() {
        // Zmienna dla spójności danych (wpisujemy to samo, co potem sprawdzamy)
        String newClientName = "GreenData Sp. z o.o.";
        String autocompletePrefix = "Gr";

        // 1. Wejście na stronę dodawania
        openApp("/agreement/add");

        // 2. Obsługa rozwijanych paneli (jeśli są zwinięte)
        List<WebElement> panelHeaders = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("mat-expansion-panel-header"))
        );

        for (WebElement header : panelHeaders) {
            // Sprawdzamy stan panelu przed kliknięciem
            String expansionState = header.getAttribute("class");
            if (expansionState != null && !expansionState.contains("mat-expanded")) {
                wait.until(ExpectedConditions.elementToBeClickable(header)).click();
            }
        }

        // 3. Wypełnianie pola Klient (Autocomplete)
        WebElement companyInput = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='client']"))
        );
        companyInput.clear();
        companyInput.sendKeys(autocompletePrefix);

        // Czekamy na opcje i wybieramy właściwą
        List<WebElement> options = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("mat-option"))
        );

        options.stream()
                .filter(opt -> opt.getText().contains(autocompletePrefix))
                .findFirst()
                .ifPresent(WebElement::click);

        // 4. Wypełnianie reszty pól (używając metody pomocniczej)
        fillInput("input[formControlName='dateFrom']", "2025-10-01");
        fillInput("input[formControlName='dateTo']", "2025-10-31");
        fillInput("input[formControlName='period']", "3");
        fillInput("input[formControlName='costForServicePerHour']", "150");
        fillInput("input[formControlName='agreementNumber']", "AG-2025-001");
        fillInput("input[formControlName='buildingNumber']", "10");
        fillInput("input[formControlName='apartmentNumber']", "1");

        // 5. Zapis formularza
        WebElement addButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );
        addButton.click();

        // --- KLUCZOWA POPRAWKA ---
        // Czekamy, aż aplikacja przekieruje nas z "/add" z powrotem na listę.
        // Bez tego Selenium szuka pola wyszukiwania będąc wciąż na starym widoku.
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/add")));

        // 6. Wyszukiwanie dodanej umowy na liście
        WebElement searchPanelHeader = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
        );
        searchPanelHeader.click();

        // Czekamy na input w panelu wyszukiwania
        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']"))
        );
        nameInput.clear();
        nameInput.sendKeys(newClientName);

        WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );
        searchButton.click();

        // 7. Weryfikacja (Czekamy na konkretny wiersz w tabeli)
        String rowXpath = String.format("//tr[contains(., '%s')]", newClientName);

        boolean found = wait.until(ExpectedConditions.and(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath(rowXpath))
        ));

        assertTrue(found, "Nie znaleziono nowo dodanej firmy '" + newClientName + "' w tabeli!");
    }


    @Test
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
