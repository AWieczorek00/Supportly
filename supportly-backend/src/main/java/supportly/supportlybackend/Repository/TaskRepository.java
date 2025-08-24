package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Model.Order;
import supportly.supportlybackend.Model.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Override
    Optional<Task> findById(Long id);

    Optional<Task> findTaskByOrder(Order order);

    List<Task> findAll(Specification<Task> spec);

}
