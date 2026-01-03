import org.example.BaseE2ETest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v127.network.Network;
import org.openqa.selenium.devtools.v127.network.model.ConnectionType;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HomePageTest extends BaseE2ETest {

    @BeforeEach
    void setup() throws Exception {
        initDriver(true); // true = headless, false = normalny tryb
    }

    @AfterEach
    void teardown() {
        quitDriver();
    }


//    @Test
//    void shouldDisplayMainPageAfterLogin() {
//        // 🔑 najpierw logowanie
//        loginAs("super.admin@gmail.com", "123456");
//
//        // teraz jesteśmy na stronie głównej
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//        // Tablica linków do sprawdzenia
//        String[] links = {"Zlecenia", "Zadania", "Pracownicy", "Umowy"};
//
//        for (String linkText : links) {
//            WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(linkText)));
//            assertTrue(link.isDisplayed(), linkText + " powinien być widoczny");
//        }
//    }

    @Test
    void shouldDisplayMainPageAfterLogin() {
        // 1. Logowanie
        loginAs("super.admin@gmail.com", "123456");

        // 2. Czekamy na załadowanie paska nawigacji (widocznego na screenie <app-navbar>)
        // To kluczowy wait - upewniamy się, że header jest gotowy.
        WebElement navbar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("app-navbar")));

        // 3. Sprawdzamy, czy logo/brand "Furnace Light" jest widoczne (ze screena: class="navbar-brand")
        WebElement brand = navbar.findElement(By.className("navbar-brand"));
        assertTrue(brand.isDisplayed(), "Nazwa aplikacji 'Furnace Light' powinna być widoczna");

        // 4. Definiujemy oczekiwane zakładki
        List<String> expectedTabs = List.of("Zlecenia", "Zadania", "Pracownicy", "Umowy");

        // 5. Asercja Grupowa (assertAll)
        // Szukamy linków TYLKO wewnątrz <nav class="menu">, żeby nie pomylić ich z innymi linkami na stronie
        assertAll("Weryfikacja Menu Głównego",
                expectedTabs.stream()
                        .map(tabName -> () -> {
                            // XPath: Szukamy wewnątrz <nav class="menu"> elementu, który zawiera dany tekst.
                            // Używamy "contains", bo Angular czasem dodaje spacje dookoła tekstu.
                            String xpathSelector = String.format("//nav[contains(@class, 'menu')]//a[contains(., '%s')]", tabName);

                            WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathSelector)));
                            assertTrue(tab.isDisplayed(), "Zakładka '" + tabName + "' nie jest widoczna w menu!");
                        })
        );
    }

    @Test
    void shouldDisplayDashboardWidgets() {
        loginAs("super.admin@gmail.com", "123456");

        // 1. Sprawdź czy widoczne są nagłówki sekcji "Zaplanowane serwisy"
        List<WebElement> headers = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//h2[contains(., 'Zaplanowane serwisy')] | //h3[contains(., 'Zaplanowane serwisy')]")
        ));

        // Na screenie widać DWA takie nagłówki (jeden po lewej, drugi po prawej)
        assertTrue(headers.size() >= 2, "Powinny być widoczne co najmniej dwie sekcje 'Zaplanowane serwisy'");

        // 2. Sprawdź czy tabela po prawej (z pierwiastkami) jest załadowana
        // Szukamy po unikalnych kolumnach widocznych na screenie: Weight, Symbol
        WebElement chemistryTable = driver.findElement(By.xpath("//table[.//th[contains(., 'Weight')] and .//th[contains(., 'Symbol')]]"));
        assertTrue(chemistryTable.isDisplayed(), "Tabela z danymi (Hydrogen, Helium...) nie jest widoczna!");

        // 3. Sprawdź czy w tabeli są dane (nie jest pusta)
        List<WebElement> rows = chemistryTable.findElements(By.cssSelector("tbody tr"));
        assertFalse(rows.isEmpty(), "Tabela na dashboardzie nie powinna być pusta!");
    }

    @Test
    void shouldNavigateFromDashboardToEmployees() {
        loginAs("super.admin@gmail.com", "123456");

        // 1. Kliknij w zakładkę "Pracownicy" w menu
        // Używamy bezpiecznego XPatha szukającego w nawigacji
        WebElement employeesLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//nav[contains(@class, 'menu')]//a[contains(., 'Pracownicy')]")
        ));
        employeesLink.click();

        // 2. Weryfikacja URL
        wait.until(ExpectedConditions.urlContains("/employee")); // lub "/pracownicy" zależnie od routingu

        // 3. Weryfikacja elementu unikalnego dla strony pracowników
        // Np. tabela z nagłówkiem "Rola" (widoczna na Twoim poprzednim screenie)
        boolean isTableVisible = wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[contains(., 'Rola')]")),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table"))
        ));

        assertTrue(isTableVisible, "Nie załadowano widoku listy pracowników!");
    }

    @Test
    void shouldLogoutSuccessfully() {
        // 1. Zaloguj się i wejdź na stronę główną
        loginAs("super.admin@gmail.com", "123456");

        // 2. Kliknij ikonę profilu (widoczna w prawym górnym rogu)
        // Na podstawie Twojego DOM (image_49d625.png) wiemy, że ma klasę 'profile-button'
        WebElement profileButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.profile-button, button[aria-haspopup='menu']")
        ));
        profileButton.click();

        // 3. Kliknij opcję "Wyloguj" w rozwiniętym menu
        // Na screenie 'image_49ecc3.png' widać wyraźnie tekst "Wyloguj"
        WebElement logoutOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(., 'Wyloguj')] | //a[contains(., 'Wyloguj')]")
        ));
        logoutOption.click();

        // 4. Weryfikacja: Powrót do ekranu logowania
        // Sprawdzamy, czy widoczny jest nagłówek "Witaj" (z formularza logowania)
        boolean isLoginPageVisible = wait.until(ExpectedConditions.and(
                ExpectedConditions.urlContains("login"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Witaj')]"))
        ));

        assertTrue(isLoginPageVisible, "Po wylogowaniu użytkownik powinien trafić na stronę logowania!");
    }

    @Test
    public void ensureSessionIsActiveAfterOneMinuteIdle() throws InterruptedException {
        // 1. Logowanie
        loginAs("super.admin@gmail.com", "123456");

        // 2. Używamy sprawdzonego waita z Twoich działających testów
        // Zamiast czekać na URL, czekamy aż pojawi się pasek nawigacji
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("app-navbar")));

        System.out.println("--- Rozpoczynamy minutę bezczynności... ---");

        // 3. Symulacja bezczynności (65 sekund)
        Thread.sleep(65000);

        System.out.println("--- Minuta minęła. Odświeżam stronę ---");
        driver.navigate().refresh();

        // 4. Weryfikacja: Czy nadal jesteśmy zalogowani?
        // Sprawdzamy obecność elementu 'app-navbar', który widzi tylko zalogowany użytkownik.
        // Zwiększamy timeout do 20s na wypadek wolnego odświeżania na Jenkinsie.
        boolean isLoggedIn = new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOfElementLocated(By.tagName("app-navbar")))
                .isDisplayed();

        assertTrue(isLoggedIn, "Sesja wygasła po 1 minucie bezczynności, a nie powinna!");
    }

    @Test
    public void shouldLoadDataOnExtremeNetworkDelay() {
        loginAs("super.admin@gmail.com", "123456");

        // 1. Pewny wait na załadowanie dashboardu (tak jak w działających testach)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("app-navbar")));

        // 2. Próba uruchomienia DevTools (Bezpieczna dla Jenkinsa)
        WebDriver augmentedDriver = new Augmenter().augment(driver);
        boolean devToolsActive = false;

        try {
            if (augmentedDriver instanceof HasDevTools) {
                DevTools devTools = ((HasDevTools) augmentedDriver).getDevTools();
                devTools.createSession();
                devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

                // Symulacja 2G
                devTools.send(Network.emulateNetworkConditions(
                        false, 20000, 1000, 1000, Optional.of(ConnectionType.CELLULAR2G),
                        Optional.empty(), Optional.empty(), Optional.empty()
                ));

                devToolsActive = true;
                System.out.println("--- DevTools: Symulacja wolnego łącza WŁĄCZONA ---");
            } else {
                System.err.println("--- DevTools: Driver nie wspiera DevTools (Grid/Jenkins) ---");
            }
        } catch (Exception e) {
            System.err.println("--- WARNING: Nie udało się połączyć z DevTools. Test biegnie bez opóźnienia. ---");
        }

        System.out.println("--- Start: Klikam w zakładkę ---");
        long startTime = System.currentTimeMillis();

        // 3. Klikamy w "Pracownicy" używając Twojego sprawdzonego XPatha
        // XPath: //nav[contains(@class, 'menu')]//a[contains(., 'Pracownicy')]
        WebElement employeesLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//nav[contains(@class, 'menu')]//a[contains(., 'Pracownicy')]")
                ));
        employeesLink.click();

        // 4. Czekamy na tabelę
        // Jeśli DevTools zadziałało -> czekamy 80s. Jeśli nie -> 20s.
        WebDriverWait adaptiveWait = new WebDriverWait(driver, Duration.ofSeconds(devToolsActive ? 80 : 20));

        // Weryfikacja: URL i obecność tabeli (używamy Twojego selektora table.mat-mdc-table)
        boolean isLoaded = adaptiveWait.until(ExpectedConditions.and(
                ExpectedConditions.urlContains("/employee"),
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.mat-mdc-table"))
        ));

        long duration = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("--- Stop: Załadowano po " + duration + " sekundach ---");

        assertTrue(isLoaded, "Tabela pracowników się nie załadowała!");

        // Asercja czasu tylko jeśli DevTools faktycznie działało
        if (devToolsActive) {
            assertTrue(duration > 15, "Test przeszedł za szybko! Symulacja sieci nie zadziałała.");
        }
    }

}