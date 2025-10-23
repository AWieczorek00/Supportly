import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeTest extends TestDatabaseSetup {

    @BeforeEach
    void setup() throws Exception {
        initDriver(true);
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
            if (row.getText().contains("SuperAdmin")) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Nie znaleziono pracownika 'SuperAdmin' w tabeli!");
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
        nameInput.sendKeys("SuperAdmin");


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
                .anyMatch(row -> row.getText().contains("SuperAdmin"));

        if (rows.size() > 1) {
//            throw new AssertionError("More than one row found!");
            found = false;
        }
        assertTrue(found, "Nie znaleziono praocwnika 'SuperAdmin' w tabeli!");
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


        // Kliknij select, żeby otworzyć overlay
        WebElement roleSelect = driver.findElement(By.cssSelector("mat-select[formcontrolname='role']"));
        roleSelect.click();

// Poczekaj aż pojawi się overlay z opcjami
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.cdk-overlay-pane")));

// Poczekaj aż opcje będą widoczne
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("mat-option .mdc-list-item__primary-text")
        ));

// Dla debugowania: wypisz dostępne role
        for (WebElement opt : options) {
            System.out.println("Option: " + opt.getText());
        }

// Znajdź opcję z tekstem ADMIN i kliknij (przez JS, bo zwykły click może być zignorowany)
        for (WebElement opt : options) {
            if (opt.getText().trim().equals("ADMIN")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);
                break;
            }
        }

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
                .anyMatch(row -> row.getText().contains("Nita"));

        if (rows.size() > 1) {
            throw new AssertionError("More than one row found!");
        }
        assertTrue(found, "Nie znaleziono nowego pracownika 'Nita' w tabeli!");

    }
}
