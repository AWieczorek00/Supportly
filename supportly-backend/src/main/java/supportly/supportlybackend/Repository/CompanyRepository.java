package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import supportly.supportlybackend.Model.Company;

import java.util.List;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findAllByNameContains(String name);

    Company findByNip(String nip);

}
