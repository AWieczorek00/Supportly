package supportly.supportlybackend.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Model.Client;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;


    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Client findClientByCompany(Company company) {
        return clientRepository.findClientByCompany(company);
    }

    public List<Client> findClientByCompanyNameContaining(String companyName) {
        return clientRepository.findClientByCompanyNameContainingIgnoreCase(companyName);
    }
}
