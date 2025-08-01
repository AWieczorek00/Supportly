package supportly.supportlybackend.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import supportly.supportlybackend.Criteria.AgreementSC;
import supportly.supportlybackend.Dto.AgreementDto;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Service.AgreementService;

import java.util.List;

@RestController
@RequestMapping("/agreement")
@RequiredArgsConstructor
public class AgreementController {


    private final AgreementService service;

    @PostMapping("/search")
    List<AgreementDto> search(@RequestBody AgreementSC criteria) {
        return service.search(criteria);
    }

    @PostMapping("/add")
    void add(@RequestBody AgreementDto agreementDto) {
         service.add(agreementDto);
    }
}
