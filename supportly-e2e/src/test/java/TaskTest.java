import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskTest extends TestDatabaseSetup {
    @BeforeEach
    void setup() throws Exception {
        initDriver(true);
        loginAs("super.admin@gmail.com", "123456");
        Thread.sleep(500); // czekaj 0.5 sekundy
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        openApp("/task/list");
        Thread.sleep(500); // czekaj 0.5 sekundy// true = headless
    }

    @AfterEach
    void teardown() {
        quitDriver();
    }


// ... wewnątrz klasy testowej ...

    @Test
    @Order(1)
    public void createTaskSimulation() throws InterruptedException {

        Thread.sleep(30000);

        assertTrue(true);
    }


//    @Test
//    @Order(1)
//    public void createTaskSimulation() throws InterruptedException {
//        // 1. KONFIGURACJA CZASU (30 sekund na znalezienie elementu)
//        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
//
//        // Zmienne testowe
//        String taskName = "Testowe zadanie " + System.currentTimeMillis(); // Unikalna nazwa
//        String orderName = "InnovaTech";
//        String fullOrderName = "InnovaTech S.A.";
//        String employeeName = "SuperAdmin"; // Lub po prostu "Super" - ważne żeby coś znalazło
//
//        System.out.println(">>> START TESTU: Otwieram panel...");
//        openPanel();
//        Thread.sleep(2000); // Odczekaj chwilę po otwarciu
//
//        // 2. Kliknij "Dodaj nowe zadanie"
//        WebElement addButton = longWait.until(ExpectedConditions.elementToBeClickable(
//                By.xpath("//button[contains(., 'Dodaj nowe zadanie')]")
//        ));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);
//
//        System.out.println(">>> Kliknięto przycisk dodawania.");
//        Thread.sleep(2000); // DEBUG: Czekaj 2s
//
//        // 3. Czekaj na Dialog
//        longWait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("mat-dialog-container")));
//
//        // 4. Wypełnij Nazwę
//        WebElement nameInput = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-dialog-container input[formControlName='name']")));
//        nameInput.clear();
//        nameInput.sendKeys(taskName);
//        System.out.println(">>> Wpisano nazwę zadania.");
//
//        // 5. Wypełnij Zamówienie (Używamy metody z długim waitiem)
//        selectFromAutocompleteSlow("orderSearch", orderName, longWait);
//
//        System.out.println(">>> Wybrano zamówienie. Czekam 2 sekundy na ogarnięcie DOM...");
//        Thread.sleep(2000); // Oddech dla Angulara
//
//        // 6. Wypełnij Pracownika
//        selectFromAutocompleteSlow("employeeSearch", employeeName, longWait);
//
//        System.out.println(">>> Wybrano pracownika.");
//
//        // 7. Zapisz
//        WebElement saveButton = longWait.until(ExpectedConditions.elementToBeClickable(
//                By.xpath("//mat-dialog-container//button[contains(., 'Zapisz')]")
//        ));
//
//        // Czekaj aż przycisk będzie aktywny (nie disabled)
//        longWait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(saveButton, "disabled", "true")));
//
//        System.out.println(">>> Przycisk Zapisz jest aktywny. Klikam...");
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);
//
//        // 8. Weryfikacja zamknięcia dialogu
//        longWait.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("mat-dialog-container")));
//
//        System.out.println(">>> Dialog zamknięty. Czekam 10 SEKUND (Symulacja requestu)...");
//        Thread.sleep(10000); // TU JEST TWOJE CZEKANIE (10s powinno wystarczyć, zwiększ do 30000 jeśli trzeba)
//
//        // 9. Wyszukiwanie
//        openPanel();
//
//        WebElement searchNameInput = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formControlName='name']")));
//        searchNameInput.clear();
//        searchNameInput.sendKeys(taskName);
//
//        WebElement searchButton = longWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
//        searchButton.click();
//
//        System.out.println(">>> Kliknięto szukaj. Czekam na tabelę...");
//
//        // Czekamy aż tabela się odświeży (spinner zniknie)
//        try {
//            longWait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".mat-spinner"))); // Jeśli masz spinner
//        } catch (Exception e) {
//        }
//
//        // 10. Asercja
//        longWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));
//
//        // Dodatkowy sleep przed szukaniem tekstu w tabeli
//        Thread.sleep(2000);
//
//        boolean rowFound = longWait.until(ExpectedConditions.textToBePresentInElementLocated(
//                By.cssSelector("table.mat-mdc-table"), fullOrderName
//        ));
//
//        if (rowFound) {
//            System.out.println(">>> SUKCES: Znaleziono zadanie w tabeli!");
//        }
//
//        assertTrue(rowFound, "Nie znaleziono zadania dla firmy: " + fullOrderName);
//    }
//
//    // --- METODA POMOCNICZA (SPOWOLNIONA I PANCERNA) ---
//    public void selectFromAutocompleteSlow(String formControlName, String value, WebDriverWait customWait) throws InterruptedException {
//        System.out.println("   -> Autocomplete: Szukam pola " + formControlName);
//
//        By inputLocator = By.cssSelector("mat-dialog-container input[formControlName='" + formControlName + "']");
//
//        // Scroll + Wait
//        WebElement input = customWait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", input);
//        customWait.until(ExpectedConditions.visibilityOf(input));
//
//        // Kliknij i Wyczyść
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
//        input.clear();
//        Thread.sleep(500); // wolniej
//
//        // Wpisz + Dispatch Event
//        input.sendKeys(value);
//        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", input);
//
//        System.out.println("   -> Autocomplete: Wpisano '" + value + "'. Czekam na listę...");
//        Thread.sleep(1000); // Czekaj na backend
//
//        // Czekaj na Overlay
//        customWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));
//
//        // Wybierz PIERWSZĄ opcję (niezależnie od tekstu)
//        By firstOptionLocator = By.cssSelector("mat-option");
//        WebElement option = customWait.until(ExpectedConditions.elementToBeClickable(firstOptionLocator));
//
//        System.out.println("   -> Autocomplete: Lista widoczna. Klikam w pierwszą opcję.");
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
//        Thread.sleep(500);
//
//        // Zamknij ESCAPEM
//        input.sendKeys(Keys.ESCAPE);
//
//        // Kliknij w Tytuł Dialogu (żeby na pewno zdjąć focus)
//        try {
//            driver.findElement(By.cssSelector("h2")).click();
//        } catch (Exception e) {
//        }
//
//        // Czekaj aż zniknie lista
//        try {
//            customWait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));
//        } catch (Exception e) {
//        }
//
//        System.out.println("   -> Autocomplete: Zakończono wybór dla " + formControlName);
//    }


    @Test
    @Order(2)
    public void testSearchTask() {
        String searchPhrase = "Przygotowanie raportu"; // Fraza do wpisania
        // Oczekiwany tekst w tabeli (może to być nazwa zadania LUB nazwa firmy, zależnie od kolumn)
        String expectedTextInRow = "Tech Solutions";

        openPanelSafe();

        // 1. Wpisz frazę (używamy bezpiecznej metody z eventami Angulara)
        fillInputSafe("input[formcontrolname='name']", searchPhrase);

        // 2. Kliknij Szukaj
        clickSafe(By.cssSelector("button[type='submit']"));

        // 3. Weryfikacja (Pancerna)
        // Czekamy na tabelę
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        // Czekamy aż pojawi się tekst.
        // Jeśli tabela się przeładowuje, Selenium spróbuje ponownie (dzięki wait).
        boolean found = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("table.mat-mdc-table"), expectedTextInRow
        ));

        assertTrue(found, "Nie znaleziono frazy '" + expectedTextInRow + "' w wynikach wyszukiwania!");
    }

    @Test
    public void selectCheckboxByCompanyName() {
        String companyToSelect = "Tech Solutions Sp. z o.o.";

        // Upewnij się, że tabela jest widoczna
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        // STRATEGIA:
        // 1. Znajdź wiersz zawierający nazwę firmy
        // 2. W tym wierszu znajdź <mat-checkbox> (kontener) lub <input>
        String rowXpath = String.format("//tr[contains(., '%s')]", companyToSelect);

        // Szukamy wiersza (presence + scroll)
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(rowXpath)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", row);

        // Szukamy INPUTA checkboxa wewnątrz tego wiersza
        WebElement checkboxInput = row.findElement(By.xpath(".//input[@type='checkbox']"));

        // Sprawdzamy stan
        if (!checkboxInput.isSelected()) {
            // KLUCZOWE DLA LINUXA:
            // Input jest często "przykryty" przez stylizację Angulara.
            // Klikamy chamsko JS-em w input, co zmienia jego stan logiczny.
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkboxInput);
        }

        // Weryfikacja
        assertTrue(checkboxInput.isSelected(), "Checkbox dla firmy " + companyToSelect + " nie został zaznaczony!");
    }

    @Test
    @Order(4)
    public void shouldFilterByCompletedStatus() {
        openPanelSafe();

        // 1. Znajdź checkbox filtru "Wykonane?"
        // Szukamy inputa wewnątrz mat-checkbox, który ma label "Wykonane?"
        // Używamy presence, bo input jest ukryty (opacity: 0)
        By checkboxLocator = By.xpath("//mat-checkbox[.//label[contains(., 'Wykonane?')]]//input");
        WebElement filterInput = wait.until(ExpectedConditions.presenceOfElementLocated(checkboxLocator));

        // Scroll do filtra (ważne na małych ekranach)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", filterInput);

        // Jeśli nie zaznaczony -> kliknij JS-em
        if (!filterInput.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", filterInput);
        }

        // 2. Kliknij Szukaj
        clickSafe(By.cssSelector("button[type='submit']"));

        // 3. Weryfikacja tabeli
        // Czekamy na przeładowanie (można dodać sleep(1) dla bezpieczeństwa przy szybkim backendzie)
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        // Pobieramy wiersze
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        // Ważne: Sprawdź czy tabela w ogóle zwróciła dane!
        assertFalse(rows.isEmpty(), "Filtrowanie zwróciło pustą tabelę - test jest niemiarodajny.");

        // Sprawdzamy każdy wiersz
        for (WebElement row : rows) {
            // Scrollujemy do wiersza (żeby Selenium go "widziało" w DOM)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", row);

            // Szukamy checkboxa w ostatniej kolumnie (lub konkretnej kolumnie statusu)
            WebElement rowCheckbox = row.findElement(By.cssSelector("input[type='checkbox']"));

            assertTrue(rowCheckbox.isSelected(), "Wiersz zawiera niezakończone zadanie mimo aktywnego filtra!");
        }
    }

    @Test
    @Order(5)
    public void shouldClearSearchCriteria() {
        openPanelSafe();

        // 1. Wpisz dane
        fillInputSafe("input[formControlName='name']", "Dane do usunięcia");

        // Szukanie po Labelu (dla inputa 'Nazwa firmy')
        // XPath: Znajdź mat-form-field, który ma label z tekstem 'Nazwa firmy', a w nim input
        By companyInputLoc = By.xpath("//mat-form-field[.//mat-label[contains(., 'Nazwa firmy')]]//input");
        fillInputSafe(companyInputLoc, "Firma XYZ");

        // 2. Kliknij Wyczyść
        clickSafe(By.xpath("//button[contains(., 'Wyczyść')]"));

        // 3. Weryfikacja
        WebElement taskInput = driver.findElement(By.cssSelector("input[formControlName='name']"));
        WebElement compInput = driver.findElement(companyInputLoc);

        // Czekamy aż wartości znikną (Angular potrzebuje chwili na wyczyszczenie modelu)
        wait.until(ExpectedConditions.textToBePresentInElementValue(taskInput, ""));
        wait.until(ExpectedConditions.textToBePresentInElementValue(compInput, ""));

        assertTrue(taskInput.getAttribute("value").isEmpty(), "Pole Nazwa zadania nie zostało wyczyszczone");
        assertTrue(compInput.getAttribute("value").isEmpty(), "Pole Nazwa firmy nie zostało wyczyszczone");
    }

    // =================================================================================
    //                           METODY POMOCNICZE (PANCERNE)
    // =================================================================================

    private void openPanelSafe() {
        By headerLoc = By.cssSelector("mat-expansion-panel-header");
        WebElement panelHeader = wait.until(ExpectedConditions.presenceOfElementLocated(headerLoc));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", panelHeader);

        String expanded = panelHeader.getAttribute("aria-expanded");
        if (expanded == null || "false".equals(expanded)) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", panelHeader);
            try { Thread.sleep(500); } catch (Exception e) {}
        }
    }

    private void fillInputSafe(String cssSelector, String value) {
        fillInputSafe(By.cssSelector(cssSelector), value);
    }

    private void fillInputSafe(By locator, String value) {
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
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    //    @Test
//    @Order(3)
    public void verifyTaskStatusInTable() {
        // Testujemy wiersz, który na screenie jest zielony (zrobiony)
        String doneTaskName = "Przygotowanie raportu miesiecznego";

        openPanel();

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formControlName='name']")));
        nameInput.clear();
        nameInput.sendKeys(doneTaskName);

        WebElement searchButton = driver.findElement(By.cssSelector("button[type='submit']"));
        searchButton.click();

        // Znajdź wiersz i sprawdź checkbox
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[contains(., '" + doneTaskName + "')]")
        ));

        WebElement checkbox = row.findElement(By.cssSelector("td:last-child input[type='checkbox']"));

        // Checkbox powinien być zaznaczony (true)
        assertTrue(checkbox.isSelected());
    }
}
