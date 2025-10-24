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


    @Test
    @Order(1)
    public void createTask() throws InterruptedException {
        WebElement panelHeader = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
        );
        panelHeader.click();
        Thread.sleep(500);


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(., 'Dodaj nowe zadanie')]")
        ));

        button.click();

        Thread.sleep(1000);

        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-dialog-container")));


        WebElement nameWrapper = dialog.findElement(By.cssSelector("mat-form-field[appearance='outline'] input[formControlName='name']")).findElement(By.xpath(".."));
        nameWrapper.click();

        WebElement nameInput = dialog.findElement(By.cssSelector("input[formControlName='name']"));
        nameInput.clear();
        nameInput.sendKeys("Testowe zadanie");

        // Wybierz zamówienie z autocomplete
        WebElement orderInput = driver.findElement(By.cssSelector("input[formControlName='orderSearch']"));
        orderInput.sendKeys("InnovaTech");
        Thread.sleep(500); // małe opóźnienie, żeby autocomplete się załadowało
        orderInput.sendKeys(Keys.ARROW_DOWN); // wybierz pierwszą opcję
        orderInput.sendKeys(Keys.ENTER);
        Thread.sleep(500); // małe opóźnienie, żeby autocomplete się załadowało

        // Wybierz pracownika z autocomplete
        WebElement empInput = driver.findElement(By.cssSelector("input[formControlName='employeeSearch']"));
        empInput.sendKeys("SuperAdmin");
        Thread.sleep(500);
        empInput.sendKeys(Keys.ARROW_DOWN);
        empInput.sendKeys(Keys.ENTER);
        Thread.sleep(500);


        WebElement saveButton = dialog.findElement(
                By.xpath(".//button[contains(., 'Zapisz')]")
        );
        saveButton.click();

        WebElement nameInput2 = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']"))
        );
        nameInput2.sendKeys("Testowe zadanie");

        // Klikamy przycisk Szukaj
        WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );
        searchButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table tr[mat-row]")));

        Thread.sleep(500);

        // Pobieramy wiersze tabeli
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        boolean found = rows.stream()
                .anyMatch(row -> row.getText().contains("InnovaTech S.A."));

        if (rows.size() > 1) {
            throw new AssertionError("More than one row found!");
        }
        assertTrue(found, "Nie znaleziono firmy 'InnovaTech S.A.' w tabeli!");

    }


    @Test
    @Order(2)
    public void testSearchTask() throws InterruptedException {
        WebElement panelHeader = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
        );
        panelHeader.click();

        // Czekamy aż inputy staną się widoczne
        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']"))
        );
        nameInput.sendKeys("Przygotowanie raportu miesięcznego");


        // Klikamy przycisk Szukaj
        WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );
        searchButton.click();
        wait.until(driver1 -> {
            List<WebElement> rowsBefore = driver1.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
            int countBefore = rowsBefore.size();
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            int countAfter = driver1.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]")).size();
            return countBefore == countAfter && countAfter > 0;
        });
// Poczekaj aż tabela się ustabilizuje (ilość wierszy się nie zmienia)
        wait.until(driver1 -> {
            List<WebElement> rowsBefore = driver1.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
            int countBefore = rowsBefore.size();
            try {
                Thread.sleep(300); // mała pauza - Angular potrzebuje chwili
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            int countAfter = driver1.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]")).size();
            return countBefore == countAfter && countAfter > 0;
        });

// Teraz bezpośrednio czekaj, aż któryś wiersz zawiera poszukiwany tekst
        boolean found = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"))
                        .stream()
                        .anyMatch(e -> e.getText().contains("Tech Solutions Sp. z o.o.")));

        assertTrue(found, "Nie znaleziono firmy 'Tech Solutions Sp. z o.o.' w tabeli!");
    }

    @Test
    public void selectCheckboxByCompanyName() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody tr")));

        // Znajdź wszystkie wiersze tabeli
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));

        boolean checkboxClicked = false;

        // Iteruj po wierszach, szukając firmy
        for (WebElement row : rows) {
            String companyName = row.findElement(By.cssSelector("td:nth-child(3)")).getText(); // trzecia kolumna
            if (companyName.equals("Tech Solutions Sp. z o.o.")) {
                WebElement checkbox = row.findElement(By.cssSelector("td:last-child input[type='checkbox']"));

                // Sprawdź, czy nie jest już zaznaczony
                if (!checkbox.isSelected()) {
                    try {
                        // Najpierw zwykły click
                        checkbox.click();
                    } catch (Exception e) {
                        // Jeśli click() nie działa, klik przez JS
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("arguments[0].click();", checkbox);
                    }
                }
                checkboxClicked = true;
                break;
            }
        }

        // Weryfikacja, że checkbox został zaznaczony
        assertTrue(checkboxClicked, "Checkbox nie został zaznaczony!");

    }
}
