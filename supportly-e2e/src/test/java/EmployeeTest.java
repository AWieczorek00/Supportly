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

import static org.junit.jupiter.api.Assertions.assertFalse;
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
        // ... kod klikania Szukaj ...
        searchButton.click();

// 4. POPRAWKA: Czekaj, aż liczba wierszy w tabeli będzie wynosić dokładnie 1
        By rowSelector = By.cssSelector("table.mat-mdc-table tr[mat-row]");

// To załatwia sprawę "odświeżania" - Selenium będzie czekać, aż stare znikną
        wait.until(ExpectedConditions.numberOfElementsToBe(rowSelector, 1));

// 5. Pobranie i weryfikacja (teraz masz pewność, że jest 1)
        List<WebElement> rows = driver.findElements(rowSelector);
        assertTrue(rows.getFirst().getText().contains(searchLastName), "Wiersz nie zawiera szukanego nazwiska!");
    }

//    @Test
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

//    @Test
//    public void createEmployee() {
//        String uniqueLastName = "Nita_" + System.currentTimeMillis();
//        String uniqueEmail = "jan.nita." + System.currentTimeMillis() + "@test.pl";
//
//        // 1. Otwórz panel
//        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
//        if (!"true".equals(panelHeader.getAttribute("aria-expanded"))) {
//            panelHeader.click();
//        }
//
//        // 2. Kliknij "Dodaj"
//        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Dodaj nowego pracownika')]")));
//        addButton.click();
//
//        // 3. Czekaj na Dialog
//        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("mat-dialog-container")));
//
//        // 4. Wypełnij WSZYSTKIE wymagane pola
//        // Używamy wait.until przy każdym polu dla stabilności
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formControlName='firstName']"))).sendKeys("Justyna");
//        dialog.findElement(By.cssSelector("input[formControlName='lastName']")).sendKeys(uniqueLastName);
//        dialog.findElement(By.cssSelector("input[formControlName='phoneNumber']")).sendKeys("502254567");
//
//        // --- NOWOŚĆ: Wypełnij Email i Hasło (często wymagane!) ---
//        // Jeśli w Twoim formularzu nie ma pola password, usuń tę linię, ale email na pewno jest potrzebny.
//        try {
//            dialog.findElement(By.cssSelector("input[formControlName='email']")).sendKeys(uniqueEmail);
//            dialog.findElement(By.cssSelector("input[formControlName='password']")).sendKeys("Haslo123!");
//        } catch (Exception e) {
//            // Ignorujemy, jeśli pól nie ma, ale zazwyczaj są wymagane
//            System.out.println("Nie znaleziono pola email lub hasło - pomijam.");
//        }
//
//        // 5. Rola (Select)
//        WebElement roleSelect = dialog.findElement(By.cssSelector("mat-select[formcontrolname='role']"));
//        roleSelect.click();
//
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));
//        driver.findElement(By.xpath("//mat-option[contains(., 'ADMIN')]")).click();
//
//        // Czekamy chwilę, aż overlay zniknie, żeby nie zasłonił przycisku Zapisz
//        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".cdk-overlay-pane")));
//
//        // 6. Zapisz
//        WebElement saveButton = dialog.findElement(By.xpath(".//button[contains(., 'Zapisz')]"));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);
//
//        // 7. Czekaj aż dialog zniknie (potwierdzenie sukcesu)
//        try {
//            wait.until(ExpectedConditions.invisibilityOf(dialog));
//        } catch (TimeoutException e) {
//            // --- DEBUGOWANIE BŁĘDU ---
//            // Jeśli tu trafiliśmy, to znaczy, że okno się nie zamknęło.
//            // Pobieramy komunikaty błędów z formularza i drukujemy je w logach Jenkinsa.
//
//            System.err.println("!!! BŁĄD WALIDACJI FORMULARZA !!!");
//            List<WebElement> errors = dialog.findElements(By.cssSelector("mat-error, .mat-error, .text-danger"));
//            for (WebElement error : errors) {
//                System.err.println("Treść błędu widoczna na ekranie: " + error.getText());
//            }
//
//            // Zrób screenshot (opcjonalnie, jeśli masz mechanizm w BaseTest)
//            // takeScreenshot("createEmployee_fail");
//
//            throw new RuntimeException("Dialog nie zamknął się po kliknięciu Zapisz. Prawdopodobnie błąd walidacji. Sprawdź logi powyżej.");
//        }
//
//        // 8. Opcjonalne odświeżenie listy
//        try {
//            WebElement searchBtn = driver.findElement(By.xpath("//button[contains(., 'Szukaj')]"));
//            searchBtn.click();
//        } catch (Exception ignored) {}
//
//        // 9. Weryfikacja
//        boolean isFound = wait.until(ExpectedConditions.textToBePresentInElementLocated(
//                By.cssSelector("table.mat-mdc-table"), uniqueLastName
//        ));
//
//        assertTrue(isFound, "Nie znaleziono pracownika: " + uniqueLastName);
//    }

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
        String phoneNumberToSearch = "000000000";

        // 1. Otwórz panel
        WebElement panelHeader = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("mat-expansion-panel-header")));
        panelHeader.click();

        // 2. Wpisz numer
        WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[formcontrolname='phoneNumber']")));
        phoneInput.clear();
        phoneInput.sendKeys(phoneNumberToSearch);

        // 3. Kliknij Szukaj
        WebElement searchButton = driver.findElement(By.xpath("//button[contains(., 'Szukaj')]"));
        searchButton.click();

        // 4. POPRAWKA:
        // Definiujemy lokator wierszy tabeli
        By rowSelector = By.cssSelector("table.mat-mdc-table tr[mat-row]");

        // Zamiast czekać na tekst, czekamy aż "śmieci" znikną i zostanie DOKŁADNIE 1 wiersz.
        // To zmusi Selenium do czekania na zakończenie filtrowania.
        wait.until(ExpectedConditions.numberOfElementsToBe(rowSelector, 1));

        // 5. Weryfikacja
        // Skoro wait wyżej przeszedł, to mamy pewność, że jest tylko 1 wiersz
        List<WebElement> rows = driver.findElements(rowSelector);

        // Dla pewności sprawdzamy tekst
        assertTrue(rows.get(0).getText().contains(phoneNumberToSearch),
                "Znaleziony wiersz nie zawiera szukanego numeru: " + phoneNumberToSearch);
    }


}
