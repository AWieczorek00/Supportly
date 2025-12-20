package supportly.supportlybackend.unit;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import supportly.supportlybackend.Criteria.OrderSC;
import supportly.supportlybackend.Dto.OrderDto;
import supportly.supportlybackend.Enum.Priority;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Model.Client;
import supportly.supportlybackend.Model.Company; // Zakładam istnienie tej klasy na podst. kodu
import supportly.supportlybackend.Model.Order;
import supportly.supportlybackend.Repository.OrderRepository;
import supportly.supportlybackend.Service.AgreementService;
import supportly.supportlybackend.Service.ClientService;
import supportly.supportlybackend.Service.OrderService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AgreementService agreementService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private OrderService orderService;

    // Do obsługi statycznego Mappera
    private MockedStatic<Mapper> mapperMock;

    @Captor
    private ArgumentCaptor<List<Order>> orderListCaptor;

    @BeforeEach
    void setUp() {
        mapperMock = Mockito.mockStatic(Mapper.class);
    }

    @AfterEach
    void tearDown() {
        mapperMock.close();
    }

    @Test
    @DisplayName("createNewOrder: Powinien zapisać i zwrócić nowe zamówienie")
    void createNewOrder_ShouldSaveAndReturnOrder() {
        // Given
        OrderDto orderDto = new OrderDto();
        Order orderEntity = new Order();
        Order savedOrder = new Order();
        savedOrder.setId(1L);

        mapperMock.when(() -> Mapper.toEntity(orderDto)).thenReturn(orderEntity);
        when(orderRepository.saveAndFlush(orderEntity)).thenReturn(savedOrder);

        // When
        Order result = orderService.createNewOrder(orderDto);

        // Then
        assertThat(result).isEqualTo(savedOrder);
        verify(orderRepository).saveAndFlush(orderEntity);
    }

    @Test
    @DisplayName("updateOrder: Powinien zaktualizować pola i zwrócić zamówienie, gdy istnieje")
    void updateOrder_ShouldUpdateFieldsAndSave_WhenExists() {
        // Given
        Long orderId = 10L;

        // Dane wejściowe (to co chcemy zaktualizować)
        Order inputOrder = new Order();
        inputOrder.setId(orderId);
        inputOrder.setNote("Updated Note");
        inputOrder.setPriority(Priority.HIGH);
        inputOrder.setDateOfAdmission(LocalDate.now());

        // Istniejące zamówienie w bazie
        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setNote("Old Note");
        existingOrder.setPriority(Priority.NORMAL);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        // When
        Order result = orderService.updateOrder(inputOrder);

        // Then
        // Sprawdzamy czy repozytorium zostało zawołane
        verify(orderRepository).save(existingOrder);

        // Sprawdzamy czy pola w obiekcie existingOrder zostały nadpisane danymi z inputOrder
        assertThat(existingOrder.getNote()).isEqualTo("Updated Note");
        assertThat(existingOrder.getPriority()).isEqualTo(Priority.HIGH);
        // Upewniamy się, że wynik metody to ten sam obiekt
        assertThat(result).isEqualTo(existingOrder);
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound2() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound3() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound4() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound5() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound6() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound7() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound8() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound9() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound10() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound11() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound12() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound13() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound14() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound15() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateOrder: Powinien rzucić ResourceNotFoundException, gdy ID nie istnieje")
    void updateOrder_ShouldThrowException_WhenNotFound16() {
        // Given
        Order inputOrder = new Order();
        inputOrder.setId(999L);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.updateOrder(inputOrder))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("nie znaleziono");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("search: Powinien zwrócić przefiltrowaną listę OrderDto")
    void search_ShouldReturnListOfDto() {
        // Given
        OrderSC criteria = new OrderSC();
        Order orderEntity = new Order();
        OrderDto orderDto = new OrderDto();

        // Mock repozytorium z dowolną specyfikacją
        when(orderRepository.findAll(any(Specification.class))).thenReturn(List.of(orderEntity));
        mapperMock.when(() -> Mapper.toDto(orderEntity)).thenReturn(orderDto);

        // When
        List<OrderDto> result = orderService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(orderDto);
    }

    @Test
    @DisplayName("createOrderFromSchedule: Powinien pobrać umowy i utworzyć z nich zamówienia")
    void createOrderFromSchedule_ShouldCreateOrdersFromAgreements() {
        // Given
        // 1. Mockowanie umów (Agreements)
        Agreement agreement = new Agreement();
        agreement.setAgreementNumber("AGR-123");
        Company company = new Company(); // Zakładam, że Agreement ma pole Company lub obiekt, który pasuje do sygnatury findClientByCompany
        agreement.setCompany(company);

        // Data za 10 dni, której spodziewa się service
        LocalDate expectedDate = LocalDate.now().plusDays(10);

        when(agreementService.findByNextRun(expectedDate)).thenReturn(List.of(agreement));

        // 2. Mockowanie klienta
        Client client = new Client();
        client.setId(100L);
        when(clientService.findClientByCompany(company)).thenReturn(client);

        // When
        orderService.createOrderFromSchedule();

        // Then
        // Przechwytujemy listę przekazaną do saveAll
        verify(orderRepository).saveAll(orderListCaptor.capture());

        List<Order> savedOrders = orderListCaptor.getValue();
        assertThat(savedOrders).hasSize(1);

        Order createdOrder = savedOrders.get(0);

        // Weryfikacja logiki mapowania wewnątrz metody createOrderList
        assertThat(createdOrder.getClient()).isEqualTo(client);
        assertThat(createdOrder.getAgreementNumber()).isEqualTo("AGR-123");
        assertThat(createdOrder.getPriority()).isEqualTo(Priority.NORMAL);
        assertThat(createdOrder.getDateOfAdmission()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("createOrderFromSchedule: Nie powinien nic zapisywać, jeśli brak umów")
    void createOrderFromSchedule_ShouldDoNothing_WhenNoAgreements() {
        // Given
        when(agreementService.findByNextRun(any(LocalDate.class))).thenReturn(Collections.emptyList());

        // When
        orderService.createOrderFromSchedule();

        // Then
        // verify z pustą listą lub w ogóle brak wywołania saveAll zależy od implementacji saveAll w Spring Data,
        // ale zazwyczaj saveAll jest wołane z pustą listą. Sprawdźmy capture.
        verify(orderRepository).saveAll(orderListCaptor.capture());
        assertThat(orderListCaptor.getValue()).isEmpty();

        verifyNoInteractions(clientService);
    }

    @Test
    @DisplayName("findOrdersByCompanyName: Powinien zwrócić listę znalezioną przez repozytorium")
    void findOrdersByCompanyName_ShouldReturnOrders() {
        // Given
        String companyName = "Tech Corp";
        List<Order> orders = List.of(new Order());

        when(orderRepository.findAllByClient_Company_NameContainingIgnoreCase(companyName))
                .thenReturn(orders);

        // When
        List<Order> result = orderService.findOrdersByCompanyName(companyName);

        // Then
        assertThat(result).isSameAs(orders);
        verify(orderRepository).findAllByClient_Company_NameContainingIgnoreCase(companyName);
    }
}