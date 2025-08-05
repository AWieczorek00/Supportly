package supportly.supportlybackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import supportly.supportlybackend.Model.Client;
import supportly.supportlybackend.Model.Company;

@Repository
public interface ClientRepository extends JpaRepository< Client, Long> {

    Client findClientByCompany(Company company);
}
