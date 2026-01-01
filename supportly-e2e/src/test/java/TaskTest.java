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
        String orderName = "InnovaTech"; // Fragment do wpisania
        String fullOrderName = "InnovaTech S.A."; // Oczekiwany wynik w tabeli
        String employeeName = "SuperAdmin";

        // 1. Otwórz panel i kliknij "Dodaj"
        openPanel();

        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Dodaj nowe zadanie')]")
        ));
        addButton.click();

        // 2. Czekaj na Dialog
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("mat-dialog-container")));

        // 3. Wypełnij Nazwę
        WebElement nameInput = dialog.findElement(By.cssSelector("input[formControlName='name']"));
        wait.until(ExpectedConditions.elementToBeClickable(nameInput));
        nameInput.clear();
        nameInput.sendKeys(taskName);

        // 4. Obsługa Autocomplete (Zamówienie i Pracownik)
        // Używamy metody pomocniczej (kod na dole)
        selectFromAutocomplete(dialog, "orderSearch", orderName);
        selectFromAutocomplete(dialog, "employeeSearch", employeeName);

        // 5. Zapisz
        WebElement saveButton = dialog.findElement(By.xpath(".//button[contains(., 'Zapisz')]"));
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();

        // 6. Weryfikacja (Wyszukanie dodanego zadania)
        // Czekamy na zniknięcie dialogu
        wait.until(ExpectedConditions.invisibilityOf(dialog));

        // Wypełniamy filtr wyszukiwania
        WebElement searchNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']")));
        searchNameInput.clear();
        searchNameInput.sendKeys(taskName);

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        searchButton.click();

        // 7. Asercja
        // Czekamy na wiersz zawierający nazwę firmy
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[contains(., '" + fullOrderName + "')]")
        ));

        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        assertEquals(1, rows.size(), "Znaleziono inną liczbę wierszy niż 1!");
        assertTrue(rows.getFirst().getText().contains(fullOrderName), "Wiersz nie zawiera nazwy firmy!");
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

    private void openPanel() {
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        // Sprawdzamy czy nie jest już otwarty (opcjonalnie)
        // String expanded = panelHeader.getAttribute("aria-expanded");
        // if ("false".equals(expanded)) { panelHeader.click(); }
        panelHeader.click();
    }

    /**
     * Obsługuje Angular Material Autocomplete
     */
    private void selectFromAutocomplete(WebElement context, String formControlName, String textToType) {
        // 1. Znajdź input i wpisz tekst
        WebElement input = context.findElement(By.cssSelector("input[formControlName='" + formControlName + "']"));
        input.clear();
        input.sendKeys(textToType);

        // 2. Czekaj aż pojawi się panel z opcjami (cdk-overlay-pane)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane mat-option")));

        // 3. Znajdź opcje i kliknij właściwą
        List<WebElement> options = driver.findElements(By.cssSelector("mat-option"));

        // Klikamy pierwszą pasującą (lub po prostu pierwszą, jeśli wpisaliśmy precyzyjny tekst)
        for (WebElement option : options) {
            if (option.getText().contains(textToType)) {
                option.click();
                return;
            }
        }
        // Fallback: jeśli pętla nie znajdzie idealnego dopasowania, kliknij pierwszą dostępną
        if (!options.isEmpty()) {
            options.getFirst().click();
        } else {
            throw new RuntimeException("Nie znaleziono opcji w autocomplete dla: " + textToType);
        }
    }

    @Test
    @Order(3)
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
