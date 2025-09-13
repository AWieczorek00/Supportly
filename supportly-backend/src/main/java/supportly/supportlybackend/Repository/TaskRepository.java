package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @EntityGraph(attributePaths = {"order", "employees"})
    List<Task> findAll(Specification<Task> spec);


    @Modifying(clearAutomatically = true)
    @Query("UPDATE Task t SET t.done = :done WHERE t.id = :id")
    int updateDone(@Param("id") Long id, @Param("done") Boolean done);

    List<Task> findAllByEmployees_IndividualIdAndDone(Long individualId,Boolean done);

}
