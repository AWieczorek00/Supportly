package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supportly.supportlybackend.Model.Part;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
}
