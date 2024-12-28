package supportly.supportlybackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportly.supportlybackend.Model.Order;
import supportly.supportlybackend.Repository.OrderRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final ActivitiesService activitiesService;
    private final PartService partService;
    private final ClientService clientService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ActivitiesService activitiesService, PartService partService, ClientService clientService) {
        this.orderRepository = orderRepository;
        this.activitiesService = activitiesService;
        this.partService = partService;
        this.clientService = clientService;
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional()
    public Order createNewOrder(Order orderBody) {
        activitiesService.createAllActivitiesFromList(orderBody.getActivitiesList());
        partService.createAllParts(orderBody.getPartList());
        if (orderBody.getClient().getId() == null) {
            clientService.createClient(orderBody.getClient());
        }
        return orderRepository.saveAndFlush(orderBody);
    }

    @Transactional
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public Order duplicateOrderById(Long id) {
        return orderRepository.findById(id)
                .map(order -> {
                    Order orderDuplicate = new Order(
                            0l, order.getClient(), null, null, null, LocalDate.now(), null,
                            0f, 0f, order.getPriority(), order.getStatus(), order.getPeriod(), order.getNote()
                    );
                    return orderRepository.save(orderDuplicate);
                }).orElseThrow(() -> new ResourceNotFoundException("Nie zaleziono takiego zelecenia do powielenia"));
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zlecenia o takim id: " + id));
    }

    @Transactional
    public Order updateOrder(Order orderBody) {
        return orderRepository.findById(orderBody.getId()).map(orderUpdate -> {
            orderUpdate.setClient(orderBody.getClient());
            orderUpdate.setActivitiesList(activitiesService.updateActivitiesList(orderBody.getActivitiesList(), orderUpdate.getActivitiesList()));
            orderUpdate.setEmployeeList(orderBody.getEmployeeList());
            orderUpdate.setPartList(partService.updatePartList(orderBody.getPartList(), orderUpdate.getPartList()));
            orderUpdate.setPeriod(orderBody.getPeriod());
            orderUpdate.setStatus(orderBody.getStatus());
            orderUpdate.setPriority(orderBody.getPriority());
            orderUpdate.setDateOfAdmission(orderBody.getDateOfAdmission());
            orderUpdate.setDateOfExecution(orderBody.getDateOfExecution());
            orderUpdate.setNote(orderBody.getNote());
            return orderRepository.save(orderUpdate);
        }).orElseThrow(() -> new ResourceNotFoundException("nie znaleziono"));
    }
}

