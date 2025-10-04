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
        initDriver(false);
        loginAs("super.admin@gmail.com", "123456");
        Thread.sleep(500); // czekaj 0.5 sekundy

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
        orderInput.sendKeys("Tech Solutions");
        Thread.sleep(500); // małe opóźnienie, żeby autocomplete się załadowało
        orderInput.sendKeys(Keys.ARROW_DOWN); // wybierz pierwszą opcję
        orderInput.sendKeys(Keys.ENTER);
        Thread.sleep(500); // małe opóźnienie, żeby autocomplete się załadowało

        // Wybierz pracownika z autocomplete
        WebElement empInput = driver.findElement(By.cssSelector("input[formControlName='employeeSearch']"));
        empInput.sendKeys("Kowalski");
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

        // Pobieramy wiersze tabeli
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        boolean found = rows.stream()
                .anyMatch(row -> row.getText().contains("Tech Solutions Sp. z o.o."));

        if (rows.size() > 1) {
            throw new AssertionError("More than one row found!");
        }
        assertTrue(found, "Nie znaleziono firmy 'Tech Solutions Sp. z o.o.' w tabeli!");

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

        // Czekamy aż tabela się pojawi
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table tr[mat-row]")));

        // Pobieramy wiersze tabeli
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        boolean found = rows.stream()
                .anyMatch(row -> row.getText().contains("Tech Solutions Sp. z o.o."));

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
