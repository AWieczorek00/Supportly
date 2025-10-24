import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class AgreementTest extends TestDatabaseSetup {

    @BeforeEach
    void setup() throws Exception {
        initDriver(true);
        loginAs("super.admin@gmail.com", "123456");
        Thread.sleep(500); // czekaj 0.5 sekundy
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        openApp("/agreement/list");
        Thread.sleep(500); // czekaj 0.5 sekundy// true = headless
    }

    @AfterEach
    void teardown() {
        quitDriver();
    }

    @Test
    public void testSearchAgreement() throws InterruptedException {
        WebElement panelHeader = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
        );
        panelHeader.click();

        // Czekamy aż inputy staną się widoczne
        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']"))
        );
        nameInput.sendKeys("Tech Solutions Sp. z o.o.");


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
    void shouldDisplayAgreementInWholeTable() throws InterruptedException {
        // Czekamy aż tabela pojawi się w DOM
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.mat-table")));

        // Pobieramy wszystkie wiersze w tabeli
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        boolean found = false;
        for (WebElement row : rows) {
            System.out.println(row.getText());
            if (row.getText().contains("Tech Solutions Sp. z o.o.")) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Nie znaleziono firmy 'Tech Solutions Sp. z o.o.' w tabeli!");
    }

    @Test
    void routToAdd() {

        WebElement panelHeader = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
        );
        panelHeader.click();

        WebElement addAgreementButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[routerLink='/agreement/add']"))
        );
        addAgreementButton.click();

        assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/agreement/add"));

    }

    @Test
    void createAgreement() throws InterruptedException {
        openApp("/agreement/add");
        Thread.sleep(500); // czekaj 0.5 sekundy

        // znajdź wszystkie nagłówki paneli
        List<WebElement> panelHeaders = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("mat-expansion-panel-header"))
        );

        for (WebElement panelHeader : panelHeaders) {
            wait.until(ExpectedConditions.elementToBeClickable(panelHeader)).click();
            Thread.sleep(300); // mała przerwa, żeby UI zdążyło się rozwinąć
        }

        WebElement companyInput = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='client']"))
        );
        companyInput.sendKeys("Gr");

        List<WebElement> options = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("mat-option"))
        );

        for (WebElement option : options) {
            if (option.getText().contains("Gr")) {
                option.click();
                break;
            }
        }

        WebElement startDate = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='dateFrom']"))
        );
        startDate.sendKeys("2025-10-01");

        WebElement endDate = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='dateTo']"))
        );
        endDate.sendKeys("2025-10-31");

        WebElement period = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='period']"))
        );
        period.sendKeys("3");

        WebElement costPerHour = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='costForServicePerHour']"))
        );
        costPerHour.sendKeys("150");

        WebElement agreementNumber = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='agreementNumber']"))
        );
        agreementNumber.sendKeys("AG-2025-001");

        WebElement buildingNumber = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='buildingNumber']"))
        );
        buildingNumber.sendKeys("10");

        WebElement apartmentNumber = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("input[formControlName='apartmentNumber']"))
        );
        apartmentNumber.sendKeys("1");

        WebElement add = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );
        add.click();

        Thread.sleep(500);

        WebElement panelHeader = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header"))
        );
        panelHeader.click();

        // Czekamy aż inputy staną się widoczne
        WebElement nameInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='name']"))
        );
        nameInput.sendKeys("GreenData Sp. z o.o.");

        // Klikamy przycisk Szukaj
        WebElement searchButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
        );
        searchButton.click();


        // Czekamy aż tabela się pojawi
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table tr[mat-row]")));

        // Pobieramy wiersze tabeli
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        Thread.sleep(500);

        boolean found = rows.stream()
                .anyMatch(row -> row.getText().contains("GreenData Sp. z o.o."));

        assertTrue(found, "Znaleziono firmy 'GreenData Sp. z o.o.' w tabeli!");


    }
}
