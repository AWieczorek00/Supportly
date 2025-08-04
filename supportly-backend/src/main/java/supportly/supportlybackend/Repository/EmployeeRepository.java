package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Model.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByIndividualId(Long individualId);

    Optional<Employee> findByPhoneNumber(String phoneNumber);

    List<Employee> findAll(Specification<Employee> spec);

}
