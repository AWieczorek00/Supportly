package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import supportly.supportlybackend.Model.Template.ActivitiesTemplate;

@Repository
public interface ActivitiesTemplateRepository extends JpaRepository<ActivitiesTemplate,Long> {
}
