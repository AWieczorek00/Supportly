package supportly.supportlybackend.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Repository.AgreementRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;

    public void add(Agreement agreement) {
        agreementRepository.save(agreement);
    }

    public List<Agreement> findAll(String name) {
        return agreementRepository.findAll();
    }
}
