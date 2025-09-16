package supportly.supportlybackend;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import supportly.supportlybackend.Dto.OrderDto;
import supportly.supportlybackend.Enum.Priority;
import supportly.supportlybackend.Model.Client;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Model.Order;
import supportly.supportlybackend.Repository.ClientRepository;
import supportly.supportlybackend.Repository.OrderRepository;
import supportly.supportlybackend.Service.OrderService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")

public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldCreateNewOrder() {
        // given
        OrderDto dto = new OrderDto();
        dto.setAgreementNumber("AG-123");
        dto.setPriority(String.valueOf(Priority.NORMAL));
        dto.setDateOfAdmission(LocalDate.now());

        // when
        Order saved = orderService.createNewOrder(dto);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAgreementNumber()).isEqualTo("AG-123");
        assertThat(orderRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void shouldUpdateOrder() {
        // given
        Order order = new Order();
        order.setAgreementNumber("AG-OLD");
        order.setPriority(Priority.HIGH);
        order.setDateOfAdmission(LocalDate.now());
        order = orderRepository.saveAndFlush(order);

        order.setAgreementNumber("AG-UPDATED");
        order.setPriority(Priority.LOW);

        // when
        Order updated = orderService.updateOrder(order);

        // then
        assertThat(updated.getAgreementNumber()).isEqualTo("AG-UPDATED");
        assertThat(updated.getPriority()).isEqualTo(Priority.LOW);
    }

    @Test
    void shouldFindOrdersByCompanyName() {
        // given
        Client client = new Client();
        client.setPhoneNumber("123456789");
        client.setEmail("TestClient@gmail.com");
        client.setType("Client");
        Company company = new Company();
        company.setName("Test Company");
        company.setEmail("TestCompany@gmail.com");
        company.setPhoneNumber("123456789");
        company.setRegon("123456789");
        company.setNip("123456789");
        client.setCompany(company);
        Order order = new Order();
        order.setAgreementNumber("AG-COMP");
        order.setDateOfAdmission(LocalDate.now());
        order.setClient(client);
        clientRepository.save(client);

        orderRepository.saveAndFlush(order);

        // when
        List<Order> found = orderService.findOrdersByCompanyName("test");

        // then
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getClient().getCompany().getName()).containsIgnoringCase("test");
    }

}
