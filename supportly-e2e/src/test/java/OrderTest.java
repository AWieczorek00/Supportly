import org.example.BaseE2ETest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderTest extends TestDatabaseSetup {

    @BeforeEach
    void setup() throws Exception {
        initDriver(true);
        loginAs("super.admin@gmail.com", "123456");
        Thread.sleep(500); // czekaj 0.5 sekundy
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        openApp("/order/list");
        Thread.sleep(500); // czekaj 0.5 sekundy// true = headless
    }

    @AfterEach
    void teardown() {
        quitDriver();
    }

    @Test
    @Order(1)
    public void searchOrderByCompanyName() {
        String companyName = "Tech Solutions Sp. z o.o.";

        openPanel();

        WebElement companyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//mat-form-field[.//mat-label[contains(., 'Nazwa firmy')]]//input")
        ));
        companyInput.clear();
        companyInput.sendKeys(companyName);

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        searchButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        String xpathRow = String.format("//tr[contains(., '%s')]", companyName);
        boolean found = wait.until(ExpectedConditions.and(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathRow)),
                ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("table"), companyName)
        ));

        assertTrue(found);
    }



    @Test
    @Order(3)
    public void filterByCreationDateRange() {
        String dateFrom = java.time.LocalDate.now().toString(); // Data widoczna na screenie
        String dateTo = java.time.LocalDate.now().plusDays(1).toString();

        openPanel();

        // Wpisywanie daty "Data założenia od"
        WebElement dateFromInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//mat-form-field[.//mat-label[contains(., 'Data założenia od')]]//input")
        ));
        dateFromInput.clear();
        dateFromInput.sendKeys(dateFrom);

        // Wpisywanie daty "Data założenia do"
        WebElement dateToInput = driver.findElement(
                By.xpath("//mat-form-field[.//mat-label[contains(., 'Data założenia do')]]//input")
        );
        dateToInput.clear();
        dateToInput.sendKeys(dateTo);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        // Sprawdź czy tabela zawiera wiersz z tą datą (2025-12-27)
        boolean dateFound = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("table.mat-mdc-table"), dateFrom
        ));

        assertTrue(dateFound);
    }

    @Test
    @Order(4)
    public void shouldClearAllFilters() {
        openPanel();

        WebElement companyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//mat-form-field[.//mat-label[contains(., 'Nazwa firmy')]]//input")
        ));
        companyInput.sendKeys("DaneDoUsuniecia");

        WebElement emailInput = driver.findElement(
                By.xpath("//mat-form-field[.//mat-label[contains(., 'Email')]]//input")
        );
        emailInput.sendKeys("test@example.com");

        WebElement clearButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Wyczyść')]")
        ));
        clearButton.click();

        wait.until(ExpectedConditions.textToBePresentInElementValue(companyInput, ""));
        wait.until(ExpectedConditions.textToBePresentInElementValue(emailInput, ""));

        assertTrue(companyInput.getAttribute("value").isEmpty());
        assertTrue(emailInput.getAttribute("value").isEmpty());
    }

    @Override
    public void openPanel() {
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        if (panelHeader.getAttribute("aria-expanded") == null || "false".equals(panelHeader.getAttribute("aria-expanded"))) {
            panelHeader.click();
        }
        // Czekamy aż panel się ustabilizuje (animacja)
        try {
            wait.until(ExpectedConditions.attributeToBe(panelHeader, "aria-expanded", "true"));
        } catch (Exception e) {
            // Ignorujemy timeout jeśli panel jest już otwarty, ale atrybut wolno się odświeża
        }
    }

    @Test
    public void createOrderFullProcess() {
        // Dane testowe
        String companyName = "Tech Solutions Sp. z o.o.";
        String partName = "Część A";
        String clientEmail = "jan.kowalski@example.com";
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // -----------------------------------------------------------
        // KROK 1: Rozwinięcie panelu i kliknięcie "Dodaj nowe zlecenie"
        // -----------------------------------------------------------

        // Używamy Twojej metody do otwarcia panelu "Kryteria wyszukiwania"
        openPanel();

        // Teraz przycisk powinien być widoczny (jest wewnątrz panelu na dole)
        WebElement addOrderButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Dodaj nowe zlecenie')]")
        ));
        addOrderButton.click();

        // -----------------------------------------------------------
        // KROK 2: Zakładka "Klient" (image_749181.png)
        // -----------------------------------------------------------
        // Czekamy na załadowanie formularza (szukamy etykiety Imię)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//mat-label[contains(., 'Imię')]")));

        fillInputByLabel("Imię", "Jan");
        fillInputByLabel("Nazwisko", "Kowalski");
        fillInputByLabel("Telefon", "501234567");
        fillInputByLabel("Email", clientEmail);

        // Autocomplete dla Firmy
        WebElement companyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//mat-form-field[.//mat-label[contains(., 'Nazwa firmy')]]//input")
        ));
        companyInput.clear();
        companyInput.sendKeys("Tech"); // Wpisujemy fragment

        // Czekamy na listę i wybieramy firmę
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane mat-option")));
        driver.findElement(By.xpath("//mat-option[contains(., '" + companyName + "')]")).click();

        // -----------------------------------------------------------
        // KROK 3: Zakładka "Części" (image_7494c9.png)
        // -----------------------------------------------------------
        // Przełączamy zakładkę
        WebElement partsTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='tab'][contains(., 'Części')]")
        ));
        partsTab.click();

        // Dodajemy "Część A" (klikamy przycisk "Dodaj" w wierszu z tą częścią)
        WebElement addPartBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tr[contains(., '" + partName + "')]//button[contains(., 'Dodaj')]")
        ));
        addPartBtn.click();

        // --- Obsługa Modala Ilości (image_7491fb.png) ---
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("mat-dialog-container")));

        WebElement quantityInput = dialog.findElement(By.cssSelector("input[type='number']"));
        quantityInput.clear();
        quantityInput.sendKeys("2"); // Dodajemy 2 sztuki

        dialog.findElement(By.xpath(".//button[contains(., 'Dodaj')]")).click();

        // Czekamy aż modal zniknie
        wait.until(ExpectedConditions.invisibilityOf(dialog));
// ... (Kod przechodzenia przez zakładki Klient i Części bez zmian) ...

        // -----------------------------------------------------------
        // KROK 4: Zakładka "Dane operacyjne" (Naprawiona obsługa Selecta)
        // -----------------------------------------------------------
        WebElement opsTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='tab'][contains(., 'Dane operacyjne')]")
        ));
        opsTab.click();

        // 1. Daty (Używamy helpera)
        fillInputByLabel("Data założenia", "2025-12-27");
        fillInputByLabel("Data wykonania", "2025-12-28");

        // 2. Status (Zwykły input tekstowy - widoczny na image_80058a.png)
        // Jeśli pole jest edytowalne:
        fillInputByLabel("Status", "W toku");

        // 3. Priorytet (Lista rozwijana - image_8005ca.png)
        // KROK A: Kliknij w pole selecta
        // Szukamy mat-select, który znajduje się wewnątrz pola z etykietą "Priorytet"
        WebElement prioritySelect = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//mat-form-field[.//mat-label[contains(., 'Priorytet')]]//mat-select")
        ));
        prioritySelect.click();

        // KROK B: Czekaj na pojawienie się opcji (Overlay)
        // Opcje w Angularze renderują się poza głównym drzewem, w div.cdk-overlay-pane
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.cdk-overlay-pane")));

        // KROK C: Wybierz opcję "Wysoki" (widoczna na image_8005ca.png)
        WebElement priorityOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//mat-option[contains(., 'Wysoki')]")
        ));
        priorityOption.click();

        // Czekamy chwilę, aż overlay zniknie (dla stabilności)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.cdk-overlay-pane")));

        // 4. Pola liczbowe (Dystans, Godziny - image_80058a.png)
        fillInputByLabel("Dystans", "150");
        fillInputByLabel("Przepracowane godziny", "8");

        // 5. Notatka
        WebElement noteArea = driver.findElement(By.tagName("textarea"));
        noteArea.sendKeys("Zlecenie priorytetowe - sprawdzić pilnie.");

        // -----------------------------------------------------------
        // KROK 5: Zapis
        // -----------------------------------------------------------
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Zapisz')]")
        ));
        saveButton.click();

        // -----------------------------------------------------------
        // KROK 6: Weryfikacja na liście (image_74a804.png)
        // -----------------------------------------------------------
        // Czekamy na powrót do tabeli (URL lub widoczność tabeli)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        // Sprawdzamy, czy w tabeli pojawił się wiersz z naszym mailem (jest unikalny)
        boolean orderExists = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("table.mat-mdc-table"), clientEmail
        ));

        assertTrue(orderExists, "Nowe zlecenie nie pojawiło się w tabeli!");
    }

    private void fillInputByLabel(String labelText, String value) {
        // Znajdź input, który znajduje się wewnątrz mat-form-field posiadającego label z danym tekstem
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//mat-form-field[.//mat-label[contains(., '" + labelText + "')]]//input")
        ));
        input.clear();
        input.sendKeys(value);
    }

    private void openPanel(String panelTitle) {
        // XPath szuka nagłówka, który zawiera w sobie (lub w dziecku) podany tekst
        By headerLocator = By.xpath("//mat-expansion-panel-header[.//mat-panel-title[contains(., '" + panelTitle + "')] or .//span[contains(., '" + panelTitle + "')]]");

        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(headerLocator));

        if (!"true".equals(panelHeader.getAttribute("aria-expanded"))) {
            panelHeader.click();

            // Czekamy, aż panel zgłosi, że jest otwarty
            wait.until(ExpectedConditions.attributeToBe(panelHeader, "aria-expanded", "true"));

            // Ważne w Angularze: czekamy na widoczność treści wewnątrz tego konkretnego panelu
            // To gwarantuje, że animacja otwierania się zakończyła i można klikać w inputy
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//mat-expansion-panel-header[contains(., '" + panelTitle + "')]/following-sibling::div")
            ));
        }
    }

}
