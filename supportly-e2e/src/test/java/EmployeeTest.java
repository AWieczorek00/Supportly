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

import static org.junit.jupiter.api.Assertions.*;

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
        } catch (Exception ignored) {
        }
        quitDriver();
    }

    @Test
    void shouldDisplayAgreementInWholeTable() {
        String employeeName = "SuperAdmin";

        // ZAMIAST pętli for i if-ów:
        // Czekamy na widoczność tabeli ORAZ na obecność wiersza z tekstem "SuperAdmin".
        // XPath `//tr[contains(., 'Tekst')]` szuka wiersza zawierającego tekst głęboko w strukturze.
        boolean isRowVisible = wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table")), ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[contains(., '" + employeeName + "')]"))));

        assertTrue(isRowVisible, "Nie znaleziono pracownika '" + employeeName + "' w tabeli!");
    }

    @Test
    public void testSearchEmployee() {
        String searchLastName = "SuperAdmin";

        // 1. Otwieranie panelu
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        panelHeader.click();

        // 2. Wpisanie nazwiska
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='lastName']")));
        nameInput.clear(); // Dobra praktyka
        nameInput.sendKeys(searchLastName);

        // 3. Kliknięcie Szukaj
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        searchButton.click();

        // 4. Weryfikacja
        // Najpierw czekamy, aż wiersz z szukanym nazwiskiem się pojawi.
        // To eliminuje potrzebę skomplikowanego czekania na "ustabilizowanie się" tabeli.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[contains(., '" + searchLastName + "')]")));

        // 5. Sprawdzenie unikalności (czy jest tylko 1 wynik)
        // Skoro wait wyżej przeszedł, to wiemy, że tabela jest załadowana i ma wynik.
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        // Zamiast if (rows.size() > 1) found = false -> Używamy konkretnej asercji.
        // Jeśli będzie 2 adminów, test powie: "Expected: 1, Actual: 2"
        assertEquals(1, rows.size(), "Znaleziono więcej niż jednego pracownika o nazwisku " + searchLastName);

        // Dodatkowe potwierdzenie tekstu (choć wait wyżej już to częściowo zrobił)
        assertTrue(rows.getFirst().getText().contains(searchLastName), "Wiersz nie zawiera szukanego nazwiska!");
    }

    @Test
    public void createEmployee() throws InterruptedException {

        // 1️⃣ Otwórz panel z pracownikami
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        panelHeader.click();

        // 2️⃣ Kliknij przycisk "Dodaj nowego pracownika"
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Dodaj nowego pracownika')]")));
        addButton.click();

        // 3️⃣ Poczekaj aż otworzy się dialog
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("mat-dialog-container")));

        // 4️⃣ Funkcja pomocnicza do bezpiecznego wpisywania tekstu
        BiConsumer<WebElement, String> safeType = (element, text) -> {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            element.clear();
            element.sendKeys(text);
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
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("mat-option .mdc-list-item__primary-text")));
        for (WebElement opt : options) {
            if (opt.getText().trim().equals("ADMIN")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);
                break;
            }
        }

        // 7️⃣ Kliknij przycisk "Zapisz" w dialogu
        WebElement saveButton = dialog.findElement(By.cssSelector("button.mat-mdc-unelevated-button.mat-primary"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);

        // 8️⃣ Poczekaj, aż tabela pracowników się pojawi
        FluentWait<RemoteWebDriver> fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofSeconds(1)).ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class);

        boolean found = fluentWait.until(d -> {
            List<WebElement> rows = d.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));
            if (rows.isEmpty()) return false;

            for (WebElement row : rows) {
                // tylko text, bez sprawdzania isDisplayed (dla wirtualizowanej tabeli)
                if (row.getText().contains("Nita")) return true;
            }
            return false;
        });

        assertTrue(found, "Nie znaleziono nowego pracownika 'Nita' w tabeli!");
    }

    @Test
    public void testClearSearchCriteria() {
        // 1. Otwórz panel (jeśli zwinięty)
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        // Sprawdzamy czy panel jest rozwinięty, jeśli nie - klikamy (można poznać po wysokości lub klasie)
        if (panelHeader.getAttribute("class") != null && !panelHeader.getAttribute("class").contains("mat-expanded")) {
            panelHeader.click();
        }

        // 2. Wpisz dane w pola (używam prawdopodobnych nazw formControlName na bazie etykiet)
        // Zakładam: firstName, lastName, phoneNumber
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='firstName']")));
        nameInput.sendKeys("TestImię");

        WebElement surnameInput = driver.findElement(By.cssSelector("input[formcontrolname='lastName']"));
        surnameInput.sendKeys("TestNazwisko");

        WebElement phoneInput = driver.findElement(By.cssSelector("input[formcontrolname='phoneNumber']"));
        phoneInput.sendKeys("123456789");

        // 3. Kliknij "Wyczyść"
        WebElement clearButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Wyczyść')]")));
        clearButton.click();

        // 4. Weryfikacja: Pola powinny być puste
        wait.until(ExpectedConditions.textToBePresentInElementValue(nameInput, ""));

        // Dodatkowe sprawdzenie, czy faktycznie są puste
        assertTrue(nameInput.getAttribute("value").isEmpty(), "Pole Imię nie zostało wyczyszczone!");
        assertTrue(surnameInput.getAttribute("value").isEmpty(), "Pole Nazwisko nie zostało wyczyszczone!");
        assertTrue(phoneInput.getAttribute("value").isEmpty(), "Pole Telefon nie zostało wyczyszczone!");
    }

    @Test
    public void testSearchByPhoneNumber() {
        String phoneNumberToSearch = "000000000"; // Numer widoczny na Twoim screenie (Justyna Nita)

        // 1. Otwórz panel
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        panelHeader.click();

        // 2. Wpisz numer
        WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='phoneNumber']")));
        phoneInput.clear();
        phoneInput.sendKeys(phoneNumberToSearch);

        // 3. Kliknij Szukaj (przycisk po prawej na screenie)
        WebElement searchButton = driver.findElement(By.xpath("//button[contains(., 'Szukaj')]"));
        searchButton.click();

        // 4. Weryfikacja
        // Czekamy aż w tabeli pojawi się wiersz z tym numerem
        // XPath szuka wiersza (tr), który zawiera w sobie (kropka) ten numer
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[contains(., '" + phoneNumberToSearch + "')]")));

        // Pobierz wiersze i sprawdź czy nie ma śmieci
        List<WebElement> rows = driver.findElements(By.cssSelector("table.mat-mdc-table tr[mat-row]"));

        // Asercja: Powinien być co najmniej jeden wiersz i każdy widoczny wiersz powinien zawierać ten numer
        assertFalse(rows.isEmpty(), "Nie znaleziono żadnego wiersza!");
        rows.forEach(row -> assertTrue(row.getText().contains(phoneNumberToSearch), "Znaleziono wiersz, który nie pasuje do filtra!"));
    }


}
