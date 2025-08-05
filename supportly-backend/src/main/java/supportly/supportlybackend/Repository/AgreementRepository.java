package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import supportly.supportlybackend.Model.Agreement;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long>, JpaSpecificationExecutor<Agreement> {

    List<Agreement> findAll(Specification<Agreement> spec);

    List<Agreement> findAllByNextServiceDate(LocalDate nextServiceDate);
}
