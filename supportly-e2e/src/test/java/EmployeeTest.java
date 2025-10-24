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
import java.util.function.BiConsumer;

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
        // Standardowy wait dla elementów
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // 20s na CI

        // 1️⃣ Otwórz panel z pracownikami
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        panelHeader.click();

        // 2️⃣ Kliknij przycisk "Dodaj nowego pracownika"
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Dodaj nowego pracownika')]")
        ));
        addButton.click();

        // 3️⃣ Poczekaj aż otworzy się dialog
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-dialog-container")));

        // 4️⃣ Funkcja pomocnicza do bezpiecznego wpisywania tekstu
        BiConsumer<WebElement, String> safeType = (element, text) -> {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            element.clear();
            element.sendKeys(text);
            // poczekaj aż pole faktycznie zawiera wpisany tekst
            wait.until(d -> element.getAttribute("value").equals(text));
        };

        // 5️⃣ Wypełnij pola formularza
        safeType.accept(dialog.findElement(By.cssSelector("input[formControlName='firstName']")), "Justyna");
        safeType.accept(dialog.findElement(By.cssSelector("input[formControlName='lastName']")), "Nita");
        safeType.accept(dialog.findElement(By.cssSelector("input[formControlName='phoneNumber']")), "502254567");

        // 6️⃣ Wybierz rolę
        WebElement roleSelect = dialog.findElement(By.cssSelector("mat-select[formcontrolname='role']"));
        wait.until(ExpectedConditions.elementToBeClickable(roleSelect)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.cdk-overlay-pane")));
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("mat-option .mdc-list-item__primary-text")
        ));
        for (WebElement opt : options) {
            if (opt.getText().trim().equals("ADMIN")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);
                break;
            }
        }

        // 7️⃣ Kliknij przycisk "Zapisz" w dialogu
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                dialog.findElement(By.cssSelector("button.mat-mdc-unelevated-button.mat-primary"))
        ));
        saveButton.click();

// 8️⃣ Poczekaj, aż overlay zniknie
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.cdk-overlay-backdrop.cdk-overlay-backdrop-showing")));

// 9️⃣ Teraz można bezpiecznie wprowadzać nazwisko w polu wyszukiwania
        WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[formcontrolname='lastName']")));
        nameInput.clear();
        nameInput.sendKeys("Nita");

// 10️⃣ Kliknij przycisk wyszukiwania bez ryzyka overlay
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));

// Jeśli nadal pojawiają się problemy w CI (overlay czasem nie zniknie w 20s):
// użyj JS click jako ostateczność:
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchButton);

        // 10️⃣ Poczekaj aż wiersz z nowym pracownikiem się pojawi
        boolean found = new WebDriverWait(driver, Duration.ofSeconds(20)) // dłuższy timeout dla CI
                .until(d -> d.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"))
                        .stream()
                        .anyMatch(e -> e.getText().contains("Nita")));

        assertTrue(found, "Nie znaleziono nowego pracownika 'Nita' w tabeli!");
    }



}
