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
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

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

// Poczekaj dodatkowo aż tabela przestanie się zmieniać
        wait.until(driver1 -> {
            List<WebElement> rowsBefore = driver1.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
            int sizeBefore = rowsBefore.size();
            try {
                Thread.sleep(500); // krótka pauza, żeby Angular zdążył dorysować
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            int sizeAfter = driver1.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]")).size();
            return sizeBefore == sizeAfter && sizeAfter > 0;
        });

// Teraz pobieramy aktualne wiersze (świeże referencje)
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

// Sprawdzamy, czy zawierają "SuperAdmin"
        boolean found = rows.stream()
                .map(WebElement::getText)
                .anyMatch(text -> text.contains("SuperAdmin"));

        if (rows.size() > 1) {
//            throw new AssertionError("More than one row found!");
            found = false;
        }
        assertTrue(found, "Nie znaleziono praocwnika 'SuperAdmin' w tabeli!");
    }

    @Test
    public void createEmployee() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Otwórz panel z pracownikami
        WebElement panelHeader = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
        );
        panelHeader.click();

        // Kliknij przycisk "Dodaj nowego pracownika"
        WebElement addButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Dodaj nowego pracownika')]"))
        );
        addButton.click();

        // Poczekaj aż otworzy się dialog
        WebElement dialog = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-dialog-container"))
        );

        // Wypełnij pola formularza
        WebElement firstNameInput = wait.until(
                ExpectedConditions.elementToBeClickable(dialog.findElement(By.cssSelector("input[formControlName='firstName']")))
        );
        firstNameInput.clear();
        firstNameInput.sendKeys("Justyna");

        WebElement lastNameInput = dialog.findElement(By.cssSelector("input[formControlName='lastName']"));
        lastNameInput.clear();
        lastNameInput.sendKeys("Nita");

        WebElement phoneInput = dialog.findElement(By.cssSelector("input[formControlName='phoneNumber']"));
        phoneInput.clear();
        phoneInput.sendKeys("502254567");

        // Otwórz select roli
        WebElement roleSelect = dialog.findElement(By.cssSelector("mat-select[formcontrolname='role']"));
        roleSelect.click();

        // Poczekaj aż pojawi się overlay z opcjami
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.cdk-overlay-pane")));

        // Pobierz opcje i wybierz "ADMIN"
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("mat-option .mdc-list-item__primary-text")
        ));

        for (WebElement opt : options) {
            if (opt.getText().trim().equals("ADMIN")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);
                break;
            }
        }

        // Kliknij przycisk "Zapisz"
        WebElement saveButton = wait.until(
                ExpectedConditions.elementToBeClickable(dialog.findElement(By.cssSelector("button.mat-mdc-unelevated-button.mat-primary")))
        );
        saveButton.click();

        // Poczekaj aż dialog się zamknie
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("mat-dialog-container")));

        // Wprowadź nazwisko do pola wyszukiwania
        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='lastName']"))
        );
        nameInput.clear();
        nameInput.sendKeys("Nita");

        // Kliknij przycisk wyszukiwania
        WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );
        searchButton.click();

        // Poczekaj aż tabela się pojawi
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table tr[mat-row]")));

        // Poczekaj aż tabela się ustabilizuje (ilość wierszy się nie zmienia)
        wait.until(driver1 -> {
            List<WebElement> rowsBefore = driver1.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
            int countBefore = rowsBefore.size();
            try {
                Thread.sleep(300); // mała pauza – Angular potrzebuje chwili
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            int countAfter = driver1.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]")).size();
            return countBefore == countAfter && countAfter > 0;
        });

        // Poczekaj aż wiersz z nowym pracownikiem się pojawi
        boolean found = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"))
                        .stream()
                        .anyMatch(e -> e.getText().contains("Nita")));

        assertTrue(found, "Nie znaleziono nowego pracownika 'Nita' w tabeli!");
    }

}
