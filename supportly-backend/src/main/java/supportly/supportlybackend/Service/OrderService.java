package supportly.supportlybackend.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportly.supportlybackend.Criteria.GenericSpecificationBuilder;
import supportly.supportlybackend.Criteria.OrderSC;
import supportly.supportlybackend.Dto.OrderDto;
import supportly.supportlybackend.Enum.Priority;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Model.Client;
import supportly.supportlybackend.Model.Order;
import supportly.supportlybackend.Repository.OrderRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ActivitiesService activitiesService;
    private final PartService partService;
    private final AgreementService agreementService;
    private final ClientService clientService;




    @Transactional()
    public Order createNewOrder(OrderDto orderBody) {
        return orderRepository.saveAndFlush(Mapper.toEntity(orderBody));
    }

    @Transactional
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

//    @Transactional
//    public Order duplicateOrderById(Long id) {
//        return orderRepository.findById(id)
//                .map(order -> {
//                    Order orderDuplicate = new Order(
//                            0l, order.getClient(), null, null, null, LocalDate.now(), null,
//                            0f, 0f, order.getPriority(), order.getStatus(), order.getPeriod(), order.getNote()
//                    );
//                    return orderRepository.save(orderDuplicate);
//                }).orElseThrow(() -> new ResourceNotFoundException("Nie zaleziono takiego zelecenia do powielenia"));
//    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zlecenia o takim id: " + id));
    }

    @Transactional
    public Order updateOrder(Order orderBody) {
        return orderRepository.findById(orderBody.getId()).map(orderUpdate -> {
            orderUpdate.setClient(orderBody.getClient());
            orderUpdate.setEmployeeList(orderBody.getEmployeeList());
//            orderUpdate.setPartList(partService.updatePartList(orderBody.getPartList(), orderUpdate.getPartList()));
            orderUpdate.setPeriod(orderBody.getPeriod());
            orderUpdate.setStatus(orderBody.getStatus());
            orderUpdate.setPriority(orderBody.getPriority());
            orderUpdate.setDateOfAdmission(orderBody.getDateOfAdmission());
            orderUpdate.setDateOfExecution(orderBody.getDateOfExecution());
            orderUpdate.setNote(orderBody.getNote());
            return orderRepository.save(orderUpdate);
        }).orElseThrow(() -> new ResourceNotFoundException("nie znaleziono"));
    }

    public List<OrderDto> search(OrderSC criteria) {
        GenericSpecificationBuilder<Order> builder = new GenericSpecificationBuilder<>();
        Specification<Order> spec = builder.build(criteria);
        return orderRepository.findAll(spec).stream().map(Mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void createOrderFromSchedule() {
        List<Agreement> agreements = agreementService.findByNextRun(LocalDate.now().plusDays(10));
        orderRepository.saveAll(createOrderList(agreements));
    }

    private List<Order> createOrderList(List<Agreement> agreements) {
        List<Order> orders = new ArrayList<>();

        for (Agreement agreement : agreements) {
            Client client = clientService.findClientByCompany(agreement.getCompany());
            Order order = new Order();
            order.setClient(client);
            order.setPriority(Priority.NORMAL);
            orders.add(order);
        }

        return orders;
    }

    public List<Order> findOrdersByCompanyName(String companyName) {
        return  orderRepository.findAllByClient_Company_NameContainingIgnoreCase(companyName);
    }
}

