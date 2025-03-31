package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supportly.supportlybackend.Model.Agreement;

@Repository
public interface AgreementRepository  extends JpaRepository<Agreement, Long> {



}
