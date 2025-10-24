import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), Paths.get("target/screenshots/fail.png"));
        } catch (Exception ignored) {}
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

//    @Test
//    public void createEmployee() throws InterruptedException {
//
//        // 1️⃣ Otwórz panel z pracownikami
//        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
//        panelHeader.click();
//
//        // 2️⃣ Kliknij przycisk "Dodaj nowego pracownika"
//        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
//                By.xpath("//button[contains(., 'Dodaj nowego pracownika')]")
//        ));
//        addButton.click();
//
//        // 3️⃣ Poczekaj aż otworzy się dialog
//        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-dialog-container")));
//
//        // 4️⃣ Funkcja pomocnicza do bezpiecznego wpisywania tekstu
//        BiConsumer<WebElement, String> safeType = (element, text) -> {
//            wait.until(ExpectedConditions.elementToBeClickable(element));
//            element.click();
//            element.clear();
//            element.sendKeys(text);
//            wait.until(d -> element.getAttribute("value").equals(text));
//        };
//
//        // 5️⃣ Wypełnij pola formularza
//        safeType.accept(dialog.findElement(By.cssSelector("input[formControlName='firstName']")), "Justyna");
//        safeType.accept(dialog.findElement(By.cssSelector("input[formControlName='lastName']")), "Nita");
//        safeType.accept(dialog.findElement(By.cssSelector("input[formControlName='phoneNumber']")), "502254567");
//
//        // 6️⃣ Wybierz rolę
//        WebElement roleSelect = dialog.findElement(By.cssSelector("mat-select[formcontrolname='role']"));
//        wait.until(ExpectedConditions.elementToBeClickable(roleSelect)).click();
//
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.cdk-overlay-pane")));
//        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
//                By.cssSelector("mat-option .mdc-list-item__primary-text")
//        ));
//        for (WebElement opt : options) {
//            if (opt.getText().trim().equals("ADMIN")) {
//                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);
//                break;
//            }
//        }
//
//        // 7️⃣ Kliknij przycisk "Zapisz" w dialogu
//        WebElement saveButton = dialog.findElement(By.cssSelector("button.mat-mdc-unelevated-button.mat-primary"));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);
//
//        // 8️⃣ Poczekaj, aż tabela pracowników się pojawi
//        FluentWait<RemoteWebDriver> fluentWait = new FluentWait<>(driver)
//                .withTimeout(Duration.ofSeconds(60))
//                .pollingEvery(Duration.ofSeconds(1))
//                .ignoring(StaleElementReferenceException.class)
//                .ignoring(NoSuchElementException.class);
//
//        boolean found = fluentWait.until(d -> {
//            List<WebElement> rows = d.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
//            if (rows.isEmpty()) return false;
//
//            for (WebElement row : rows) {
//                // tylko text, bez sprawdzania isDisplayed (dla wirtualizowanej tabeli)
//                if (row.getText().contains("Nita")) return true;
//            }
//            return false;
//        });
//
//        assertTrue(found, "Nie znaleziono nowego pracownika 'Nita' w tabeli!");
//    }


    @Test
    public void createEmployee2() throws InterruptedException {
        WebElement panelHeader = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
        );
        panelHeader.click();
        Thread.sleep(500);

        List<WebElement> buttons = driver.findElements(By.cssSelector("button.mat-stroked-button"));

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

        searchButton.click();

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

// Teraz czekaj, aż któryś wiersz zawiera poszukiwany tekst
        boolean found = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"))
                        .stream()
                        .anyMatch(e -> e.getText().contains("Nita")));

// Sprawdzenie, czy nie ma więcej niż jednego wiersza
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
        if (rows.size() > 1) {
            throw new AssertionError("More than one row found!");
        }

        assertTrue(found, "Nie znaleziono nowego pracownika 'Nita' w tabeli!");

    }

}
