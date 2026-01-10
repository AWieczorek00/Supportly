package supportly.supportlybackend.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.TestPropertySource;
import supportly.supportlybackend.Criteria.OrderSC;
import supportly.supportlybackend.Dto.OrderDto;
import supportly.supportlybackend.Enum.Period;
import supportly.supportlybackend.Enum.Priority;
import supportly.supportlybackend.Model.*;
import supportly.supportlybackend.Repository.AgreementRepository;
import supportly.supportlybackend.Repository.ClientRepository;
import supportly.supportlybackend.Repository.CompanyRepository;
import supportly.supportlybackend.Repository.OrderRepository;
import supportly.supportlybackend.Service.OrderService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")

public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AgreementRepository agreementRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        agreementRepository.deleteAll();
        clientRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    @DisplayName("createNewOrder: Powinien utworzyć nowe zamówienie w bazie")
    void shouldCreateNewOrder() {
        // Given
        OrderDto orderDto = new OrderDto();
        // agreementNumber jest wymagany (nullable = false)
        orderDto.setAgreementNumber("AGR-TEST-001");
        orderDto.setNote("Awaria serwera"); // Używamy notatki jako opisu
        orderDto.setStatus("NEW");
        orderDto.setPriority(Priority.NORMAL.name());
        orderDto.setDateOfAdmission(LocalDate.now());

        // When
        Order result = orderService.createNewOrder(orderDto);

        // Then
        assertThat(result.getId()).isNotNull();

        List<Order> allOrders = orderRepository.findAll();
        assertThat(allOrders).hasSize(1);
        assertThat(allOrders.get(0).getAgreementNumber()).isEqualTo("AGR-TEST-001");
        assertThat(allOrders.get(0).getNote()).isEqualTo("Awaria serwera");
    }

    @Test
    @DisplayName("updateOrder: Powinien zaktualizować istniejące zamówienie")
    void shouldUpdateExistingOrder() {
        // Given
        Order existingOrder = new Order();
        existingOrder.setAgreementNumber("AGR-OLD"); // Wymagane pole
        existingOrder.setNote("Notatka stara");
        existingOrder.setPriority(Priority.NORMAL);
        existingOrder.setStatus("NEW");
        existingOrder.setDateOfAdmission(LocalDate.now());
        existingOrder = orderRepository.save(existingOrder);

        // Obiekt z aktualizacją
        Order updateData = new Order();
        updateData.setId(existingOrder.getId());
        // updateOrder w serwisie przepisuje status, priorytet, notatkę itp.
        updateData.setNote("Notatka nowa - PILNE");
        updateData.setPriority(Priority.HIGH);
        updateData.setStatus("IN_PROGRESS");
        // Musimy podać agreementNumber, jeśli DTO/Encja wejściowa tego wymaga,
        // lub jeśli serwis przepisuje to pole (w Twoim kodzie updateOrder nie przepisuje agreementNumber,
        // więc nie musimy go tu ustawiać, chyba że walidacja encji tego wymaga w obiekcie przejściowym).

        // When
        Order updatedOrder = orderService.updateOrder(updateData);

        // Then
        Order fromDb = orderRepository.findById(existingOrder.getId()).orElseThrow();
        assertThat(fromDb.getNote()).isEqualTo("Notatka nowa - PILNE");
        assertThat(fromDb.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(fromDb.getStatus()).isEqualTo("IN_PROGRESS");
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException dla złego ID")
    void shouldThrowExceptionWhenUpdatingNonExistent() {
        // Given
        Order ghostOrder = new Order();
        ghostOrder.setId(9999L);
        ghostOrder.setAgreementNumber("GHOST"); // Żeby hibernate nie krzyczał przy walidacji przed serwisem

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(ghostOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("nie znaleziono");
    }

    @Test
    @DisplayName("createOrderFromSchedule: Powinien wygenerować zamówienia z umów na podstawie daty")
    void shouldGenerateOrdersFromSchedule3() {
        // Given
        // 1. Firma nr 1 (dla umowy, która MA wygenerować zamówienie)
        Company company1 = createValidCompany("Tech Sp. z o.o.");
        // Musimy upewnić się, że dane są unikalne
        company1.setNip("1111111111111");
        company1.setRegon("111111111");
        company1.setEmail("tech@test.pl");
        companyRepository.save(company1);

        // 2. Klient dla Firmy nr 1
        Client client = new Client();
        client.setCompany(company1);
        client.setFirstName("Adam");
        client.setLastName("Admin");
        client.setPhoneNumber("123456789");
        client.setEmail("admin@tech.pl");
        client.setType("Client");
        clientRepository.save(client);

        // 3. Umowa dla Firmy nr 1 (poprawna data - za 10 dni)
        Agreement agreement = new Agreement();
        agreement.setCompany(company1);
        agreement.setAgreementNumber("AGR-SCHEDULED-1");
        agreement.setNextServiceDate(LocalDate.now().plusDays(10));
        agreement.setPeriod(Period.MONTHLY); // Zakładam, że Period to Enum
        agreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(agreement);

        // 4. Firma nr 2 (dla umowy, która NIE powinna generować zamówienia)
        // Tworzymy nową firmę, aby uniknąć błędu "Unique index on COMPANY_ID" w tabeli Agreement
        Company company2 = createValidCompany("Other Corp.");
        company2.setNip("2222222222222"); // Inny NIP
        company2.setRegon("222222222");   // Inny Regon
        company2.setEmail("other@corp.pl");
        companyRepository.save(company2);

        // 5. Umowa dla Firmy nr 2 (zła data - za 20 dni)
        Agreement otherAgreement = new Agreement();
        otherAgreement.setCompany(company2); // Przypisujemy do DRUGIEJ firmy
        otherAgreement.setAgreementNumber("AGR-FUTURE");
        otherAgreement.setNextServiceDate(LocalDate.now().plusDays(20));
        otherAgreement.setPeriod(Period.YEARLY);
        otherAgreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(otherAgreement);

        // When
        orderService.createOrderFromSchedule();

        // Then
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1); // Powinno być tylko jedno zamówienie (od firmy 1)

        Order generatedOrder = orders.get(0);
        assertThat(generatedOrder.getAgreementNumber()).isEqualTo("AGR-SCHEDULED-1");
        // Upewniamy się, że zamówienie jest przypisane do klienta pierwszej firmy
        assertThat(generatedOrder.getClient().getId()).isEqualTo(client.getId());
        assertThat(generatedOrder.getPriority()).isEqualTo(Priority.NORMAL);
    }
    @Test
    @DisplayName("createOrderFromSchedule: Powinien wygenerować zamówienia z umów na podstawie daty")
    void shouldGenerateOrdersFromSchedule2() {
        // Given
        // 1. Firma nr 1 (dla umowy, która MA wygenerować zamówienie)
        Company company1 = createValidCompany("Tech Sp. z o.o.");
        // Musimy upewnić się, że dane są unikalne
        company1.setNip("1111111111111");
        company1.setRegon("111111111");
        company1.setEmail("tech@test.pl");
        companyRepository.save(company1);

        // 2. Klient dla Firmy nr 1
        Client client = new Client();
        client.setCompany(company1);
        client.setFirstName("Adam");
        client.setLastName("Admin");
        client.setPhoneNumber("123456789");
        client.setEmail("admin@tech.pl");
        client.setType("Client");
        clientRepository.save(client);

        // 3. Umowa dla Firmy nr 1 (poprawna data - za 10 dni)
        Agreement agreement = new Agreement();
        agreement.setCompany(company1);
        agreement.setAgreementNumber("AGR-SCHEDULED-1");
        agreement.setNextServiceDate(LocalDate.now().plusDays(10));
        agreement.setPeriod(Period.MONTHLY); // Zakładam, że Period to Enum
        agreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(agreement);

        // 4. Firma nr 2 (dla umowy, która NIE powinna generować zamówienia)
        // Tworzymy nową firmę, aby uniknąć błędu "Unique index on COMPANY_ID" w tabeli Agreement
        Company company2 = createValidCompany("Other Corp.");
        company2.setNip("2222222222222"); // Inny NIP
        company2.setRegon("222222222");   // Inny Regon
        company2.setEmail("other@corp.pl");
        companyRepository.save(company2);

        // 5. Umowa dla Firmy nr 2 (zła data - za 20 dni)
        Agreement otherAgreement = new Agreement();
        otherAgreement.setCompany(company2); // Przypisujemy do DRUGIEJ firmy
        otherAgreement.setAgreementNumber("AGR-FUTURE");
        otherAgreement.setNextServiceDate(LocalDate.now().plusDays(20));
        otherAgreement.setPeriod(Period.YEARLY);
        otherAgreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(otherAgreement);

        // When
        orderService.createOrderFromSchedule();

        // Then
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1); // Powinno być tylko jedno zamówienie (od firmy 1)

        Order generatedOrder = orders.get(0);
        assertThat(generatedOrder.getAgreementNumber()).isEqualTo("AGR-SCHEDULED-1");
        // Upewniamy się, że zamówienie jest przypisane do klienta pierwszej firmy
        assertThat(generatedOrder.getClient().getId()).isEqualTo(client.getId());
        assertThat(generatedOrder.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    @DisplayName("createOrderFromSchedule: Powinien wygenerować zamówienia z umów na podstawie daty")
    void shouldGenerateOrdersFromSchedule4() {
        // Given
        // 1. Firma nr 1 (dla umowy, która MA wygenerować zamówienie)
        Company company1 = createValidCompany("Tech Sp. z o.o.");
        // Musimy upewnić się, że dane są unikalne
        company1.setNip("1111111111111");
        company1.setRegon("111111111");
        company1.setEmail("tech@test.pl");
        companyRepository.save(company1);

        // 2. Klient dla Firmy nr 1
        Client client = new Client();
        client.setCompany(company1);
        client.setFirstName("Adam");
        client.setLastName("Admin");
        client.setPhoneNumber("123456789");
        client.setEmail("admin@tech.pl");
        client.setType("Client");
        clientRepository.save(client);

        // 3. Umowa dla Firmy nr 1 (poprawna data - za 10 dni)
        Agreement agreement = new Agreement();
        agreement.setCompany(company1);
        agreement.setAgreementNumber("AGR-SCHEDULED-1");
        agreement.setNextServiceDate(LocalDate.now().plusDays(10));
        agreement.setPeriod(Period.MONTHLY); // Zakładam, że Period to Enum
        agreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(agreement);

        // 4. Firma nr 2 (dla umowy, która NIE powinna generować zamówienia)
        // Tworzymy nową firmę, aby uniknąć błędu "Unique index on COMPANY_ID" w tabeli Agreement
        Company company2 = createValidCompany("Other Corp.");
        company2.setNip("2222222222222"); // Inny NIP
        company2.setRegon("222222222");   // Inny Regon
        company2.setEmail("other@corp.pl");
        companyRepository.save(company2);

        // 5. Umowa dla Firmy nr 2 (zła data - za 20 dni)
        Agreement otherAgreement = new Agreement();
        otherAgreement.setCompany(company2); // Przypisujemy do DRUGIEJ firmy
        otherAgreement.setAgreementNumber("AGR-FUTURE");
        otherAgreement.setNextServiceDate(LocalDate.now().plusDays(20));
        otherAgreement.setPeriod(Period.YEARLY);
        otherAgreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(otherAgreement);

        // When
        orderService.createOrderFromSchedule();

        // Then
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1); // Powinno być tylko jedno zamówienie (od firmy 1)

        Order generatedOrder = orders.get(0);
        assertThat(generatedOrder.getAgreementNumber()).isEqualTo("AGR-SCHEDULED-1");
        // Upewniamy się, że zamówienie jest przypisane do klienta pierwszej firmy
        assertThat(generatedOrder.getClient().getId()).isEqualTo(client.getId());
        assertThat(generatedOrder.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    @DisplayName("createOrderFromSchedule: Powinien wygenerować zamówienia z umów na podstawie daty")
    void shouldGenerateOrdersFromSchedule5() {
        // Given
        // 1. Firma nr 1 (dla umowy, która MA wygenerować zamówienie)
        Company company1 = createValidCompany("Tech Sp. z o.o.");
        // Musimy upewnić się, że dane są unikalne
        company1.setNip("1111111111111");
        company1.setRegon("111111111");
        company1.setEmail("tech@test.pl");
        companyRepository.save(company1);

        // 2. Klient dla Firmy nr 1
        Client client = new Client();
        client.setCompany(company1);
        client.setFirstName("Adam");
        client.setLastName("Admin");
        client.setPhoneNumber("123456789");
        client.setEmail("admin@tech.pl");
        client.setType("Client");
        clientRepository.save(client);

        // 3. Umowa dla Firmy nr 1 (poprawna data - za 10 dni)
        Agreement agreement = new Agreement();
        agreement.setCompany(company1);
        agreement.setAgreementNumber("AGR-SCHEDULED-1");
        agreement.setNextServiceDate(LocalDate.now().plusDays(10));
        agreement.setPeriod(Period.MONTHLY); // Zakładam, że Period to Enum
        agreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(agreement);

        // 4. Firma nr 2 (dla umowy, która NIE powinna generować zamówienia)
        // Tworzymy nową firmę, aby uniknąć błędu "Unique index on COMPANY_ID" w tabeli Agreement
        Company company2 = createValidCompany("Other Corp.");
        company2.setNip("2222222222222"); // Inny NIP
        company2.setRegon("222222222");   // Inny Regon
        company2.setEmail("other@corp.pl");
        companyRepository.save(company2);

        // 5. Umowa dla Firmy nr 2 (zła data - za 20 dni)
        Agreement otherAgreement = new Agreement();
        otherAgreement.setCompany(company2); // Przypisujemy do DRUGIEJ firmy
        otherAgreement.setAgreementNumber("AGR-FUTURE");
        otherAgreement.setNextServiceDate(LocalDate.now().plusDays(20));
        otherAgreement.setPeriod(Period.YEARLY);
        otherAgreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(otherAgreement);

        // When
        orderService.createOrderFromSchedule();

        // Then
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1); // Powinno być tylko jedno zamówienie (od firmy 1)

        Order generatedOrder = orders.get(0);
        assertThat(generatedOrder.getAgreementNumber()).isEqualTo("AGR-SCHEDULED-1");
        // Upewniamy się, że zamówienie jest przypisane do klienta pierwszej firmy
        assertThat(generatedOrder.getClient().getId()).isEqualTo(client.getId());
        assertThat(generatedOrder.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    @DisplayName("createOrderFromSchedule: Powinien wygenerować zamówienia z umów na podstawie daty")
    void shouldGenerateOrdersFromSchedule6() {
        // Given
        // 1. Firma nr 1 (dla umowy, która MA wygenerować zamówienie)
        Company company1 = createValidCompany("Tech Sp. z o.o.");
        // Musimy upewnić się, że dane są unikalne
        company1.setNip("1111111111111");
        company1.setRegon("111111111");
        company1.setEmail("tech@test.pl");
        companyRepository.save(company1);

        // 2. Klient dla Firmy nr 1
        Client client = new Client();
        client.setCompany(company1);
        client.setFirstName("Adam");
        client.setLastName("Admin");
        client.setPhoneNumber("123456789");
        client.setEmail("admin@tech.pl");
        client.setType("Client");
        clientRepository.save(client);

        // 3. Umowa dla Firmy nr 1 (poprawna data - za 10 dni)
        Agreement agreement = new Agreement();
        agreement.setCompany(company1);
        agreement.setAgreementNumber("AGR-SCHEDULED-1");
        agreement.setNextServiceDate(LocalDate.now().plusDays(10));
        agreement.setPeriod(Period.MONTHLY); // Zakładam, że Period to Enum
        agreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(agreement);

        // 4. Firma nr 2 (dla umowy, która NIE powinna generować zamówienia)
        // Tworzymy nową firmę, aby uniknąć błędu "Unique index on COMPANY_ID" w tabeli Agreement
        Company company2 = createValidCompany("Other Corp.");
        company2.setNip("2222222222222"); // Inny NIP
        company2.setRegon("222222222");   // Inny Regon
        company2.setEmail("other@corp.pl");
        companyRepository.save(company2);

        // 5. Umowa dla Firmy nr 2 (zła data - za 20 dni)
        Agreement otherAgreement = new Agreement();
        otherAgreement.setCompany(company2); // Przypisujemy do DRUGIEJ firmy
        otherAgreement.setAgreementNumber("AGR-FUTURE");
        otherAgreement.setNextServiceDate(LocalDate.now().plusDays(20));
        otherAgreement.setPeriod(Period.YEARLY);
        otherAgreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(otherAgreement);

        // When
        orderService.createOrderFromSchedule();

        // Then
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1); // Powinno być tylko jedno zamówienie (od firmy 1)

        Order generatedOrder = orders.get(0);
        assertThat(generatedOrder.getAgreementNumber()).isEqualTo("AGR-SCHEDULED-1");
        // Upewniamy się, że zamówienie jest przypisane do klienta pierwszej firmy
        assertThat(generatedOrder.getClient().getId()).isEqualTo(client.getId());
        assertThat(generatedOrder.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    @DisplayName("createOrderFromSchedule: Powinien wygenerować zamówienia z umów na podstawie daty")
    void shouldGenerateOrdersFromSchedule7() {
        // Given
        // 1. Firma nr 1 (dla umowy, która MA wygenerować zamówienie)
        Company company1 = createValidCompany("Tech Sp. z o.o.");
        // Musimy upewnić się, że dane są unikalne
        company1.setNip("1111111111111");
        company1.setRegon("111111111");
        company1.setEmail("tech@test.pl");
        companyRepository.save(company1);

        // 2. Klient dla Firmy nr 1
        Client client = new Client();
        client.setCompany(company1);
        client.setFirstName("Adam");
        client.setLastName("Admin");
        client.setPhoneNumber("123456789");
        client.setEmail("admin@tech.pl");
        client.setType("Client");
        clientRepository.save(client);

        // 3. Umowa dla Firmy nr 1 (poprawna data - za 10 dni)
        Agreement agreement = new Agreement();
        agreement.setCompany(company1);
        agreement.setAgreementNumber("AGR-SCHEDULED-1");
        agreement.setNextServiceDate(LocalDate.now().plusDays(10));
        agreement.setPeriod(Period.MONTHLY); // Zakładam, że Period to Enum
        agreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(agreement);

        // 4. Firma nr 2 (dla umowy, która NIE powinna generować zamówienia)
        // Tworzymy nową firmę, aby uniknąć błędu "Unique index on COMPANY_ID" w tabeli Agreement
        Company company2 = createValidCompany("Other Corp.");
        company2.setNip("2222222222222"); // Inny NIP
        company2.setRegon("222222222");   // Inny Regon
        company2.setEmail("other@corp.pl");
        companyRepository.save(company2);

        // 5. Umowa dla Firmy nr 2 (zła data - za 20 dni)
        Agreement otherAgreement = new Agreement();
        otherAgreement.setCompany(company2); // Przypisujemy do DRUGIEJ firmy
        otherAgreement.setAgreementNumber("AGR-FUTURE");
        otherAgreement.setNextServiceDate(LocalDate.now().plusDays(20));
        otherAgreement.setPeriod(Period.YEARLY);
        otherAgreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(otherAgreement);

        // When
        orderService.createOrderFromSchedule();

        // Then
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1); // Powinno być tylko jedno zamówienie (od firmy 1)

        Order generatedOrder = orders.get(0);
        assertThat(generatedOrder.getAgreementNumber()).isEqualTo("AGR-SCHEDULED-1");
        // Upewniamy się, że zamówienie jest przypisane do klienta pierwszej firmy
        assertThat(generatedOrder.getClient().getId()).isEqualTo(client.getId());
        assertThat(generatedOrder.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    @DisplayName("createOrderFromSchedule: Powinien wygenerować zamówienia z umów na podstawie daty")
    void shouldGenerateOrdersFromSchedule8() {
        // Given
        // 1. Firma nr 1 (dla umowy, która MA wygenerować zamówienie)
        Company company1 = createValidCompany("Tech Sp. z o.o.");
        // Musimy upewnić się, że dane są unikalne
        company1.setNip("1111111111111");
        company1.setRegon("111111111");
        company1.setEmail("tech@test.pl");
        companyRepository.save(company1);

        // 2. Klient dla Firmy nr 1
        Client client = new Client();
        client.setCompany(company1);
        client.setFirstName("Adam");
        client.setLastName("Admin");
        client.setPhoneNumber("123456789");
        client.setEmail("admin@tech.pl");
        client.setType("Client");
        clientRepository.save(client);

        // 3. Umowa dla Firmy nr 1 (poprawna data - za 10 dni)
        Agreement agreement = new Agreement();
        agreement.setCompany(company1);
        agreement.setAgreementNumber("AGR-SCHEDULED-1");
        agreement.setNextServiceDate(LocalDate.now().plusDays(10));
        agreement.setPeriod(Period.MONTHLY); // Zakładam, że Period to Enum
        agreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(agreement);

        // 4. Firma nr 2 (dla umowy, która NIE powinna generować zamówienia)
        // Tworzymy nową firmę, aby uniknąć błędu "Unique index on COMPANY_ID" w tabeli Agreement
        Company company2 = createValidCompany("Other Corp.");
        company2.setNip("2222222222222"); // Inny NIP
        company2.setRegon("222222222");   // Inny Regon
        company2.setEmail("other@corp.pl");
        companyRepository.save(company2);

        // 5. Umowa dla Firmy nr 2 (zła data - za 20 dni)
        Agreement otherAgreement = new Agreement();
        otherAgreement.setCompany(company2); // Przypisujemy do DRUGIEJ firmy
        otherAgreement.setAgreementNumber("AGR-FUTURE");
        otherAgreement.setNextServiceDate(LocalDate.now().plusDays(20));
        otherAgreement.setPeriod(Period.YEARLY);
        otherAgreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(otherAgreement);

        // When
        orderService.createOrderFromSchedule();

        // Then
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1); // Powinno być tylko jedno zamówienie (od firmy 1)

        Order generatedOrder = orders.get(0);
        assertThat(generatedOrder.getAgreementNumber()).isEqualTo("AGR-SCHEDULED-1");
        // Upewniamy się, że zamówienie jest przypisane do klienta pierwszej firmy
        assertThat(generatedOrder.getClient().getId()).isEqualTo(client.getId());
        assertThat(generatedOrder.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    @DisplayName("createOrderFromSchedule: Powinien wygenerować zamówienia z umów na podstawie daty")
    void shouldGenerateOrdersFromSchedule9() {
        // Given
        // 1. Firma nr 1 (dla umowy, która MA wygenerować zamówienie)
        Company company1 = createValidCompany("Tech Sp. z o.o.");
        // Musimy upewnić się, że dane są unikalne
        company1.setNip("1111111111111");
        company1.setRegon("111111111");
        company1.setEmail("tech@test.pl");
        companyRepository.save(company1);

        // 2. Klient dla Firmy nr 1
        Client client = new Client();
        client.setCompany(company1);
        client.setFirstName("Adam");
        client.setLastName("Admin");
        client.setPhoneNumber("123456789");
        client.setEmail("admin@tech.pl");
        client.setType("Client");
        clientRepository.save(client);

        // 3. Umowa dla Firmy nr 1 (poprawna data - za 10 dni)
        Agreement agreement = new Agreement();
        agreement.setCompany(company1);
        agreement.setAgreementNumber("AGR-SCHEDULED-1");
        agreement.setNextServiceDate(LocalDate.now().plusDays(10));
        agreement.setPeriod(Period.MONTHLY); // Zakładam, że Period to Enum
        agreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(agreement);

        // 4. Firma nr 2 (dla umowy, która NIE powinna generować zamówienia)
        // Tworzymy nową firmę, aby uniknąć błędu "Unique index on COMPANY_ID" w tabeli Agreement
        Company company2 = createValidCompany("Other Corp.");
        company2.setNip("2222222222222"); // Inny NIP
        company2.setRegon("222222222");   // Inny Regon
        company2.setEmail("other@corp.pl");
        companyRepository.save(company2);

        // 5. Umowa dla Firmy nr 2 (zła data - za 20 dni)
        Agreement otherAgreement = new Agreement();
        otherAgreement.setCompany(company2); // Przypisujemy do DRUGIEJ firmy
        otherAgreement.setAgreementNumber("AGR-FUTURE");
        otherAgreement.setNextServiceDate(LocalDate.now().plusDays(20));
        otherAgreement.setPeriod(Period.YEARLY);
        otherAgreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(otherAgreement);

        // When
        orderService.createOrderFromSchedule();

        // Then
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1); // Powinno być tylko jedno zamówienie (od firmy 1)

        Order generatedOrder = orders.get(0);
        assertThat(generatedOrder.getAgreementNumber()).isEqualTo("AGR-SCHEDULED-1");
        // Upewniamy się, że zamówienie jest przypisane do klienta pierwszej firmy
        assertThat(generatedOrder.getClient().getId()).isEqualTo(client.getId());
        assertThat(generatedOrder.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    @DisplayName("createOrderFromSchedule: Powinien wygenerować zamówienia z umów na podstawie daty")
    void shouldGenerateOrdersFromSchedule10() {
        // Given
        // 1. Firma nr 1 (dla umowy, która MA wygenerować zamówienie)
        Company company1 = createValidCompany("Tech Sp. z o.o.");
        // Musimy upewnić się, że dane są unikalne
        company1.setNip("1111111111111");
        company1.setRegon("111111111");
        company1.setEmail("tech@test.pl");
        companyRepository.save(company1);

        // 2. Klient dla Firmy nr 1
        Client client = new Client();
        client.setCompany(company1);
        client.setFirstName("Adam");
        client.setLastName("Admin");
        client.setPhoneNumber("123456789");
        client.setEmail("admin@tech.pl");
        client.setType("Client");
        clientRepository.save(client);

        // 3. Umowa dla Firmy nr 1 (poprawna data - za 10 dni)
        Agreement agreement = new Agreement();
        agreement.setCompany(company1);
        agreement.setAgreementNumber("AGR-SCHEDULED-1");
        agreement.setNextServiceDate(LocalDate.now().plusDays(10));
        agreement.setPeriod(Period.MONTHLY); // Zakładam, że Period to Enum
        agreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(agreement);

        // 4. Firma nr 2 (dla umowy, która NIE powinna generować zamówienia)
        // Tworzymy nową firmę, aby uniknąć błędu "Unique index on COMPANY_ID" w tabeli Agreement
        Company company2 = createValidCompany("Other Corp.");
        company2.setNip("2222222222222"); // Inny NIP
        company2.setRegon("222222222");   // Inny Regon
        company2.setEmail("other@corp.pl");
        companyRepository.save(company2);

        // 5. Umowa dla Firmy nr 2 (zła data - za 20 dni)
        Agreement otherAgreement = new Agreement();
        otherAgreement.setCompany(company2); // Przypisujemy do DRUGIEJ firmy
        otherAgreement.setAgreementNumber("AGR-FUTURE");
        otherAgreement.setNextServiceDate(LocalDate.now().plusDays(20));
        otherAgreement.setPeriod(Period.YEARLY);
        otherAgreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(otherAgreement);

        // When
        orderService.createOrderFromSchedule();

        // Then
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1); // Powinno być tylko jedno zamówienie (od firmy 1)

        Order generatedOrder = orders.get(0);
        assertThat(generatedOrder.getAgreementNumber()).isEqualTo("AGR-SCHEDULED-1");
        // Upewniamy się, że zamówienie jest przypisane do klienta pierwszej firmy
        assertThat(generatedOrder.getClient().getId()).isEqualTo(client.getId());
        assertThat(generatedOrder.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    @DisplayName("createOrderFromSchedule: Powinien wygenerować zamówienia z umów na podstawie daty")
    void shouldGenerateOrdersFromSchedule11() {
        // Given
        // 1. Firma nr 1 (dla umowy, która MA wygenerować zamówienie)
        Company company1 = createValidCompany("Tech Sp. z o.o.");
        // Musimy upewnić się, że dane są unikalne
        company1.setNip("1111111111111");
        company1.setRegon("111111111");
        company1.setEmail("tech@test.pl");
        companyRepository.save(company1);

        // 2. Klient dla Firmy nr 1
        Client client = new Client();
        client.setCompany(company1);
        client.setFirstName("Adam");
        client.setLastName("Admin");
        client.setPhoneNumber("123456789");
        client.setEmail("admin@tech.pl");
        client.setType("Client");
        clientRepository.save(client);

        // 3. Umowa dla Firmy nr 1 (poprawna data - za 10 dni)
        Agreement agreement = new Agreement();
        agreement.setCompany(company1);
        agreement.setAgreementNumber("AGR-SCHEDULED-1");
        agreement.setNextServiceDate(LocalDate.now().plusDays(10));
        agreement.setPeriod(Period.MONTHLY); // Zakładam, że Period to Enum
        agreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(agreement);

        // 4. Firma nr 2 (dla umowy, która NIE powinna generować zamówienia)
        // Tworzymy nową firmę, aby uniknąć błędu "Unique index on COMPANY_ID" w tabeli Agreement
        Company company2 = createValidCompany("Other Corp.");
        company2.setNip("2222222222222"); // Inny NIP
        company2.setRegon("222222222");   // Inny Regon
        company2.setEmail("other@corp.pl");
        companyRepository.save(company2);

        // 5. Umowa dla Firmy nr 2 (zła data - za 20 dni)
        Agreement otherAgreement = new Agreement();
        otherAgreement.setCompany(company2); // Przypisujemy do DRUGIEJ firmy
        otherAgreement.setAgreementNumber("AGR-FUTURE");
        otherAgreement.setNextServiceDate(LocalDate.now().plusDays(20));
        otherAgreement.setPeriod(Period.YEARLY);
        otherAgreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(otherAgreement);

        // When
        orderService.createOrderFromSchedule();

        // Then
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1); // Powinno być tylko jedno zamówienie (od firmy 1)

        Order generatedOrder = orders.get(0);
        assertThat(generatedOrder.getAgreementNumber()).isEqualTo("AGR-SCHEDULED-1");
        // Upewniamy się, że zamówienie jest przypisane do klienta pierwszej firmy
        assertThat(generatedOrder.getClient().getId()).isEqualTo(client.getId());
        assertThat(generatedOrder.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    @DisplayName("createOrderFromSchedule: Powinien wygenerować zamówienia z umów na podstawie daty")
    void shouldGenerateOrdersFromSchedule() {
        // Given
        // 1. Firma nr 1 (dla umowy, która MA wygenerować zamówienie)
        Company company1 = createValidCompany("Tech Sp. z o.o.");
        // Musimy upewnić się, że dane są unikalne
        company1.setNip("1111111111111");
        company1.setRegon("111111111");
        company1.setEmail("tech@test.pl");
        companyRepository.save(company1);

        // 2. Klient dla Firmy nr 1
        Client client = new Client();
        client.setCompany(company1);
        client.setFirstName("Adam");
        client.setLastName("Admin");
        client.setPhoneNumber("123456789");
        client.setEmail("admin@tech.pl");
        client.setType("Client");
        clientRepository.save(client);

        // 3. Umowa dla Firmy nr 1 (poprawna data - za 10 dni)
        Agreement agreement = new Agreement();
        agreement.setCompany(company1);
        agreement.setAgreementNumber("AGR-SCHEDULED-1");
        agreement.setNextServiceDate(LocalDate.now().plusDays(10));
        agreement.setPeriod(Period.MONTHLY); // Zakładam, że Period to Enum
        agreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(agreement);

        // 4. Firma nr 2 (dla umowy, która NIE powinna generować zamówienia)
        // Tworzymy nową firmę, aby uniknąć błędu "Unique index on COMPANY_ID" w tabeli Agreement
        Company company2 = createValidCompany("Other Corp.");
        company2.setNip("2222222222222"); // Inny NIP
        company2.setRegon("222222222");   // Inny Regon
        company2.setEmail("other@corp.pl");
        companyRepository.save(company2);

        // 5. Umowa dla Firmy nr 2 (zła data - za 20 dni)
        Agreement otherAgreement = new Agreement();
        otherAgreement.setCompany(company2); // Przypisujemy do DRUGIEJ firmy
        otherAgreement.setAgreementNumber("AGR-FUTURE");
        otherAgreement.setNextServiceDate(LocalDate.now().plusDays(20));
        otherAgreement.setPeriod(Period.YEARLY);
        otherAgreement.setSignedDate(LocalDate.now().minusDays(10));
        agreementRepository.save(otherAgreement);

        // When
        orderService.createOrderFromSchedule();

        // Then
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1); // Powinno być tylko jedno zamówienie (od firmy 1)

        Order generatedOrder = orders.get(0);
        assertThat(generatedOrder.getAgreementNumber()).isEqualTo("AGR-SCHEDULED-1");
        // Upewniamy się, że zamówienie jest przypisane do klienta pierwszej firmy
        assertThat(generatedOrder.getClient().getId()).isEqualTo(client.getId());
        assertThat(generatedOrder.getPriority()).isEqualTo(Priority.NORMAL);
    }

    @Test
    @DisplayName("findOrdersByCompanyName: Powinien znaleźć zamówienia po nazwie firmy klienta")
    void shouldFindOrdersByCompanyName() {
        // Given
        // Firma A
        Company compA = createValidCompany("Alpha Corp");
        compA.setNip("1111111111111");
        compA.setRegon("111111111");
        compA.setEmail("a@alpha.pl");
        companyRepository.save(compA);

        Client clientA = new Client();
        clientA.setCompany(compA);
        clientA.setFirstName("Jan");
        clientA.setLastName("Alpha");
        clientA.setPhoneNumber("111");
        clientA.setEmail("jan@alpha.pl");
        clientA.setType("Client");
        clientRepository.save(clientA);

        Order orderA = new Order();
        orderA.setClient(clientA);
        orderA.setAgreementNumber("AGR-ALPHA");
        orderA.setNote("Order for Alpha");
        orderRepository.save(orderA);

        // Firma B
        Company compB = createValidCompany("Beta Corp");
        compB.setNip("2222222222222");
        compB.setRegon("222222222");
        compB.setEmail("b@beta.pl");
        companyRepository.save(compB);

        Client clientB = new Client();
        clientB.setCompany(compB);
        clientB.setFirstName("Piotr");
        clientB.setLastName("Beta");
        clientB.setPhoneNumber("222");
        clientB.setEmail("piotr@beta.pl");
        clientB.setType("Client");
        clientRepository.save(clientB);

        Order orderB = new Order();
        orderB.setClient(clientB);
        orderB.setAgreementNumber("AGR-BETA");
        orderB.setNote("Order for Beta");
        orderRepository.save(orderB);

        // When
        List<Order> result = orderService.findOrdersByCompanyName("Alpha");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAgreementNumber()).isEqualTo("AGR-ALPHA");
        // Sprawdzamy czy relacja została zaciągnięta poprawnie
        assertThat(result.get(0).getClient().getCompany().getName()).isEqualTo("Alpha Corp");
    }

    @Test
    @DisplayName("search: Powinien filtrować zamówienia (Specification)")
    void shouldSearchOrders() {
        // Given
        Order o1 = new Order();
        o1.setAgreementNumber("AGR-SEARCH-1");
        o1.setStatus("DONE");
        orderRepository.save(o1);

        Order o2 = new Order();
        o2.setAgreementNumber("AGR-SEARCH-2");
        o2.setStatus("NEW");
        orderRepository.save(o2);

        // When
        // UWAGA: Tutaj zakładam, że OrderSC ma pole 'status' typu String
        // lub pole 'agreementNumber'. Dostosuj 'criteria' do swojej klasy OrderSC.
        // Poniżej przykład wyszukiwania po statusie (jeśli builder to obsługuje):
        OrderSC criteria = new OrderSC();
        criteria.setStatus("NEW");

        // Jeśli SC nie ma statusu, a ma np. agreementNumber:
        // criteria.setAgreementNumber("AGR-SEARCH-2");

        List<OrderDto> result = orderService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAgreementNumber()).isEqualTo("AGR-SEARCH-2");
    }

    // --- Metoda pomocnicza ---
    private Company createValidCompany(String name) {
        Address address = new Address();
        address.setCity("Warszawa");
        address.setStreet("Testowa");
        address.setZipCode("00-111");
        address.setStreetNumber(1);

        Company company = new Company();
        company.setName(name);
        company.setNip("1234567890123");
        company.setRegon("123456789");
        company.setEmail("kontakt@" + name.replaceAll("\\s+", "") + ".pl");
        company.setPhoneNumber("123456789");
        company.setAddress(address);

        return company;
    }

}
