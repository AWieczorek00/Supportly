import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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


    @Test
    @Order(1)
    public void createTask() {
        String taskName = "Testowe zadanie";
        String orderName = "InnovaTech";
        String fullOrderName = "InnovaTech S.A.";
        String employeeName = "SuperAdmin";

        openPanel();

        // 1. Kliknij "Dodaj nowe zadanie"
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Dodaj nowe zadanie')]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);

        // 2. Czekaj na Dialog
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("mat-dialog-container")));

        // 3. Wypełnij Nazwę
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-dialog-container input[formControlName='name']")));
        nameInput.clear();
        nameInput.sendKeys(taskName);

        // 4. Wypełnij Zamówienie (Autocomplete 1)
        selectFromAutocomplete("orderSearch", orderName);

        // --- Czekaj aż zniknie lista podpowiedzi ---
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        // 5. Wypełnij Pracownika (Autocomplete 2)
        selectFromAutocomplete("employeeSearch", employeeName);

        // 6. Zapisz
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//mat-dialog-container//button[contains(., 'Zapisz')]")
        ));

        // Upewniamy się, że przycisk jest aktywny (formularz waliduje się poprawnie)
        wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(saveButton, "disabled", "true")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);

        // 7. Weryfikacja zamknięcia dialogu
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("mat-dialog-container")));

        // --- PRZYWRÓCONA LOGIKA WYSZUKIWANIA ---
        // Musimy znaleźć to zadanie, żeby potwierdzić utworzenie

        // Otwórz panel wyszukiwania (chyba że openPanel() sam sprawdza czy jest otwarty)
        openPanel();

        // Wpisz nazwę zadania w filtry
        WebElement searchNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formControlName='name']")));
        searchNameInput.clear();
        searchNameInput.sendKeys(taskName);

        // Kliknij Szukaj
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        // lub inny selektor przycisku szukania, np. button z lupką
        searchButton.click();

        // Czekaj chwilę na przeładowanie tabeli (częsty błąd: test sprawdza tabelę zanim ta się odświeży)
        // Jeśli masz spinner ładowania, warto na niego poczekać:
        // wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".mat-spinner")));

        // 8. Asercja w tabeli
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")));

        boolean rowFound = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("table.mat-mdc-table"), fullOrderName
        ));

        assertTrue(rowFound, "Nie znaleziono zadania dla firmy: " + fullOrderName);
    }

    // --- METODA POMOCNICZA DO AUTOCOMPLETE (Dodaj do klasy) ---
    // Zmień sygnaturę metody - usuń "WebElement container"
    public void selectFromAutocomplete(String formControlName, String value) {
        // 1. Znajdź selektor
        By inputLocator = By.cssSelector("mat-dialog-container input[formControlName='" + formControlName + "']");

        // ZMIANA: Czekamy na OBECNOŚĆ w kodzie (nie musi być widoczny na ekranie)
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));

        // ZMIANA: Scrollujemy do elementu (to naprawia problem małego ekranu na Linuxie)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", input);

        // Teraz czekamy aż faktycznie będzie widoczny i klikalny
        wait.until(ExpectedConditions.visibilityOf(input));

        // 2. Kliknij i wyczyść
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
        input.clear();

        // 3. Wpisz wartość + Event input
        input.sendKeys(value);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                input
        );

        try { Thread.sleep(500); } catch (Exception e) {}

        // 4. Czekaj na listę
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));

        // 5. Wybierz pierwszą opcję
        By firstOptionLocator = By.cssSelector("mat-option");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(firstOptionLocator));

        // Scroll do opcji też się przydaje
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", option);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);

        // 6. Zamknij overlay i przenieś focus
        input.sendKeys(Keys.ESCAPE);

        // DODATKOWE ZABEZPIECZENIE:
        // Klikamy w tło dialogu (np. w tytuł), żeby na pewno zdjąć focus z inputa.
        // To definitywnie zamyka wszystkie overlaye.
        try {
            WebElement dialogTitle = driver.findElement(By.cssSelector("h2[mat-dialog-title]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dialogTitle);
        } catch (Exception e) {
            // Ignorujemy jeśli nie ma tytułu
        }

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));
        } catch (Exception e) {}
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
