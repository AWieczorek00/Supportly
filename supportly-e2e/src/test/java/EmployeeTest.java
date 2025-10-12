import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeTest extends TestDatabaseSetup {

    @BeforeEach
    void setup() throws Exception {
        initDriver(false);
        loginAs("super.admin@gmail.com", "123456");
        Thread.sleep(500); // czekaj 0.5 sekundy

        openApp("/employee/list");
        Thread.sleep(500); // czekaj 0.5 sekundy// true = headless
    }

    @AfterEach
    void teardown() {
        quitDriver();
    }

    @Test
    void shouldDisplayAgreementInWholeTable() throws InterruptedException {
        // Czekamy aż tabela pojawi się w DOM
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.mat-table")));

        // Pobieramy wszystkie wiersze w tabeli
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        boolean found = false;
        for (WebElement row : rows) {
            System.out.println(row.getText());
            if (row.getText().contains("Kowalski")) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Znaleziono firmy 'Kowalski' w tabeli!");
    }

    @Test
    public void testSearchEmployee() throws InterruptedException {
        WebElement panelHeader = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
        );
        panelHeader.click();

        // Czekamy aż inputy staną się widoczne
        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='lastName']"))
        );
        nameInput.sendKeys("Nowak");


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
                .anyMatch(row -> row.getText().contains("Kowalski"));

        if (rows.size() > 1) {
//            throw new AssertionError("More than one row found!");
            found = false;
        }
        assertTrue(found, "Nie znaleziono praocwnika 'Kowalski' w tabeli!");
    }

    @Test
    public void createEmployee() throws InterruptedException {
        WebElement panelHeader = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
        );
        panelHeader.click();
        Thread.sleep(500);

        List<WebElement> buttons = driver.findElements(By.cssSelector("button.mat-stroked-button"));
        System.out.println("Liczba znalezionych przycisków: " + buttons.size());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(., 'Dodaj nowego pracownika')]")
        ));

        button.click();

        Thread.sleep(1000);

        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement dialog = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-dialog-container")));

        WebElement firstNameInput = dialog.findElement(By.cssSelector("input[formControlName='firstName']"));
        firstNameInput.click();
        firstNameInput.clear();
        firstNameInput.sendKeys("Justyna");


        WebElement lastNameInput = dialog.findElement(By.cssSelector("input[formControlName='lastName']"));
        lastNameInput.click();
        lastNameInput.clear();
        lastNameInput.sendKeys("Nita");

        WebElement phoneInput = dialog.findElement(By.cssSelector("input[formControlName='phoneNumber']"));
        phoneInput.click();
        phoneInput.clear();
        phoneInput.sendKeys("502254567");

        WebElement roleSelect = dialog.findElement(By.cssSelector("mat-select[formControlName='role']"));
        roleSelect.click();

        WebElement roleOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//mat-option[@value='admin']")
        ));
        roleOption.click();

        Thread.sleep(500);


        WebElement saveButton = dialog.findElement(By.cssSelector("button.mat-mdc-unelevated-button.mat-primary"));
        saveButton.click();
        ;

        Thread.sleep(500);


        // Czekamy aż inputy staną się widoczne
        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='lastName']"))
        );
        nameInput.sendKeys("Nita");


        WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".cdk-overlay-backdrop.cdk-overlay-backdrop-showing")
        ));

        searchButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table tr[mat-row]")));

        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        boolean found = rows.stream()
                .anyMatch(row -> row.getText().contains("Nowak"));

        if (rows.size() > 1) {
            throw new AssertionError("More than one row found!");
        }
        assertTrue(found, "Znaleziono firmy 'Nowak' w tabeli!");

    }
}
