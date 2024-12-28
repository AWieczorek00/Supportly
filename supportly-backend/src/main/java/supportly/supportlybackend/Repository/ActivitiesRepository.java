package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supportly.supportlybackend.Model.Activities;

@Repository
public interface ActivitiesRepository extends JpaRepository<Activities,Long> {
}
