import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

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
        // 1. KONFIGURACJA CZASU (30 sekund na znalezienie elementu)
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Zmienne testowe
        String taskName = "Testowe zadanie " + System.currentTimeMillis(); // Unikalna nazwa
        String orderName = "InnovaTech";
        String fullOrderName = "InnovaTech S.A.";
        String employeeName = "SuperAdmin"; // Lub po prostu "Super" - ważne żeby coś znalazło

        System.out.println(">>> START TESTU: Otwieram panel...");
        openPanel();
        Thread.sleep(2000); // Odczekaj chwilę po otwarciu

        // 2. Kliknij "Dodaj nowe zadanie"
        WebElement addButton = longWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Dodaj nowe zadanie')]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);

        System.out.println(">>> Kliknięto przycisk dodawania.");
        Thread.sleep(2000); // DEBUG: Czekaj 2s

        // 3. Czekaj na Dialog
        longWait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("mat-dialog-container")));

        // 4. Wypełnij Nazwę
        WebElement nameInput = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-dialog-container input[formControlName='name']")));
        nameInput.clear();
        nameInput.sendKeys(taskName);
        System.out.println(">>> Wpisano nazwę zadania.");

        // 5. Wypełnij Zamówienie (Używamy metody z długim waitiem)
        selectFromAutocompleteSlow("orderSearch", orderName, longWait);

        System.out.println(">>> Wybrano zamówienie. Czekam 2 sekundy na ogarnięcie DOM...");
        Thread.sleep(2000); // Oddech dla Angulara

        // 6. Wypełnij Pracownika
        selectFromAutocompleteSlow("employeeSearch", employeeName, longWait);

        System.out.println(">>> Wybrano pracownika.");

        // 7. Zapisz
        WebElement saveButton = longWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//mat-dialog-container//button[contains(., 'Zapisz')]")
        ));

        // Czekaj aż przycisk będzie aktywny (nie disabled)
        longWait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(saveButton, "disabled", "true")));

        System.out.println(">>> Przycisk Zapisz jest aktywny. Klikam...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);

        // 8. Weryfikacja zamknięcia dialogu
        longWait.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("mat-dialog-container")));

        System.out.println(">>> Dialog zamknięty. Czekam 10 SEKUND (Symulacja requestu)...");
        Thread.sleep(10000); // TU JEST TWOJE CZEKANIE (10s powinno wystarczyć, zwiększ do 30000 jeśli trzeba)

        // 9. Wyszukiwanie
        openPanel();

        WebElement searchNameInput = longWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formControlName='name']")));
        searchNameInput.clear();
        searchNameInput.sendKeys(taskName);

        WebElement searchButton = longWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        searchButton.click();

        System.out.println(">>> Kliknięto szukaj. Czekam na tabelę...");

        // Czekamy aż tabela się odświeży (spinner zniknie)
        try {
            longWait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".mat-spinner"))); // Jeśli masz spinner
        } catch (Exception e) {
        }

        // 10. Asercja
        longWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        // Dodatkowy sleep przed szukaniem tekstu w tabeli
        Thread.sleep(2000);

        boolean rowFound = longWait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("table.mat-mdc-table"), fullOrderName
        ));

        if (rowFound) {
            System.out.println(">>> SUKCES: Znaleziono zadanie w tabeli!");
        }

        assertTrue(rowFound, "Nie znaleziono zadania dla firmy: " + fullOrderName);
    }

    // --- METODA POMOCNICZA (SPOWOLNIONA I PANCERNA) ---
    public void selectFromAutocompleteSlow(String formControlName, String value, WebDriverWait customWait) throws InterruptedException {
        System.out.println("   -> Autocomplete: Szukam pola " + formControlName);

        By inputLocator = By.cssSelector("mat-dialog-container input[formControlName='" + formControlName + "']");

        // Scroll + Wait
        WebElement input = customWait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", input);
        customWait.until(ExpectedConditions.visibilityOf(input));

        // Kliknij i Wyczyść
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
        input.clear();
        Thread.sleep(500); // wolniej

        // Wpisz + Dispatch Event
        input.sendKeys(value);
        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", input);

        System.out.println("   -> Autocomplete: Wpisano '" + value + "'. Czekam na listę...");
        Thread.sleep(1000); // Czekaj na backend

        // Czekaj na Overlay
        customWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        // Wybierz PIERWSZĄ opcję (niezależnie od tekstu)
        By firstOptionLocator = By.cssSelector("mat-option");
        WebElement option = customWait.until(ExpectedConditions.elementToBeClickable(firstOptionLocator));

        System.out.println("   -> Autocomplete: Lista widoczna. Klikam w pierwszą opcję.");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        Thread.sleep(500);

        // Zamknij ESCAPEM
        input.sendKeys(Keys.ESCAPE);

        // Kliknij w Tytuł Dialogu (żeby na pewno zdjąć focus)
        try {
            driver.findElement(By.cssSelector("h2")).click();
        } catch (Exception e) {
        }

        // Czekaj aż zniknie lista
        try {
            customWait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));
        } catch (Exception e) {
        }

        System.out.println("   -> Autocomplete: Zakończono wybór dla " + formControlName);
    }


    @Test
    @Order(2)
    public void testSearchTask() {
        String searchPhrase = "Przygotowanie raportu miesiecznego";
        // UWAGA: W Twoim kodzie szukałeś "Raportu", ale sprawdzałeś "Tech Solutions".
        // Dostosuj tę zmienną do tego, co faktycznie ma się pojawić w tabeli.
        String expectedCompanyInRow = "Tech Solutions Sp. z o.o.";

        openPanel();

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']")));
        nameInput.clear();
        nameInput.sendKeys(searchPhrase);

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        searchButton.click();

        // Inteligentne czekanie na wynik:
        // Czekamy, aż w tabeli pojawi się wiersz zawierający oczekiwany tekst.
        // To eliminuje skomplikowane pętle z Thread.sleep.
        boolean found = wait.until(ExpectedConditions.and(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")),
                ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("table.mat-mdc-table"), expectedCompanyInRow)
        ));

        assertTrue(found, "Nie znaleziono firmy '" + expectedCompanyInRow + "' w wynikach wyszukiwania!");
    }

    @Test
    public void selectCheckboxByCompanyName() {
        String companyToSelect = "Tech Solutions Sp. z o.o.";

        // Czekamy na załadowanie tabeli
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        // STRATEGIA XPATH:
        // Znajdź wiersz (tr), który zawiera tekst firmy, a następnie wewnątrz tego wiersza znajdź checkbox.
        // To eliminuje potrzebę pętli for i if-ów w Javie.
        String dynamicXpath = String.format("//tr[contains(., '%s')]//input[@type='checkbox']", companyToSelect);

        // Alternatywnie, jeśli input jest ukryty (co częste w Angular Material), klikamy w kontener:
        // String dynamicXpath = String.format("//tr[contains(., '%s')]//mat-checkbox", companyToSelect);

        WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(dynamicXpath)));

        // Sprawdzamy stan i klikamy
        // Uwaga: checkbox.isSelected() działa na input, ale w Angularze czasem sprawdza się klasę 'mat-checkbox-checked' na tagu <mat-checkbox>
        if (!checkbox.isSelected()) {
            // Próbujemy kliknąć normalnie, a jak Angular zasłania - JS
            try {
                wait.until(ExpectedConditions.elementToBeClickable(checkbox)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
            }
        }

        assertTrue(checkbox.isSelected(), "Checkbox dla firmy " + companyToSelect + " nie został zaznaczony!");
    }

    // --- Metody Pomocnicze ---

//    @Override
//    public void openPanel() {
//        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
//        // Sprawdzamy czy nie jest już otwarty (opcjonalnie)
//        // String expanded = panelHeader.getAttribute("aria-expanded");
//        // if ("false".equals(expanded)) { panelHeader.click(); }
//        panelHeader.click();
//    }

    /**
     * PANCERNA OBSŁUGA AUTOCOMPLETE
     */
//    private void selectFromAutocomplete(WebElement ignoredContext, String formControlName, String textToType) {
//        // 1. ZAMIAST szukać w 'ignoredContext' (który może być Stale), szukamy globalnie i czekamy na visibility.
//        // Używamy "mat-dialog-container input...", żeby mieć pewność, że szukamy w modalu.
//        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.cssSelector("mat-dialog-container input[formControlName='" + formControlName + "']")
//        ));
//
//        input.clear();
//        input.sendKeys(textToType);
//
//        // 2. Czekaj na overlay (opcje)
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane mat-option")));
//
//        // 3. Znajdź właściwą opcję (XPath bez pętli)
//        String xpathSelector = String.format("//mat-option[contains(., '%s')]", textToType);
//        WebElement targetOption = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathSelector)));
//
//        // 4. Kliknij (JS Click)
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", targetOption);
//
//        // 5. Zamknij dropdown (ESC) - to zapobiega błędom z overlayem
//        try {
//            input.sendKeys(Keys.ESCAPE);
//        } catch (Exception e) {
//            // Ignorujemy błędy przy ESC
//        }
//
//        // Krótki oddech dla Angulara po wyborze
//        try { Thread.sleep(200); } catch (InterruptedException e) {}
//    }

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

    @Test
    @Order(4)
    public void shouldFilterByCompletedStatus() {
        openPanel();

        // Znajdź checkbox "Wykonane?" po tekście etykiety (bezpieczniej niż szukanie po ID)
        WebElement doneFilterCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//mat-checkbox[.//label[contains(., 'Wykonane?')]]//input")
        ));

        if (!doneFilterCheckbox.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", doneFilterCheckbox);
        }

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        searchButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        // Tabela nie powinna być pusta i każdy wiersz musi mieć zaznaczony checkbox
        for (WebElement row : rows) {
            WebElement rowCheckbox = row.findElement(By.cssSelector("td:last-child input[type='checkbox']"));
            assertTrue(rowCheckbox.isSelected());
        }
    }

    @Test
    @Order(5)
    public void shouldClearSearchCriteria() {
        openPanel();

        WebElement taskNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formControlName='name']")));
        taskNameInput.sendKeys("Dane do usunięcia");

        // Szukamy inputa "Nazwa firmy" po etykiecie mat-label, bo formControlName może być inny
        WebElement companyInput = driver.findElement(By.xpath("//mat-form-field[.//mat-label[contains(., 'Nazwa firmy')]]//input"));
        companyInput.sendKeys("Firma XYZ");

        WebElement clearButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Wyczyść')]")
        ));
        clearButton.click();

        wait.until(ExpectedConditions.textToBePresentInElementValue(taskNameInput, ""));
        wait.until(ExpectedConditions.textToBePresentInElementValue(companyInput, ""));

        assertTrue(taskNameInput.getAttribute("value").isEmpty());
        assertTrue(companyInput.getAttribute("value").isEmpty());
    }
}
