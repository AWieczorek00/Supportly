package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supportly.supportlybackend.Model.Template.PartsTemplate;

@Repository
public interface PartsTemplateRepository extends JpaRepository<PartsTemplate,Long> {
}
