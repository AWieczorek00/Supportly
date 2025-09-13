package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAll(Specification<Order> spec);

    List<Order> findAllByClient_Company_NameContainingIgnoreCase(String companyName);

    Order findOrderById(Long id);
}
