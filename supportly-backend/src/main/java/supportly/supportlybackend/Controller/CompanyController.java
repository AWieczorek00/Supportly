package supportly.supportlybackend.Controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import supportly.supportlybackend.Dto.CompanyDto;
import supportly.supportlybackend.Service.CompanyService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/findByName")
    public List<CompanyDto> findAllByName(@RequestParam String name) {
        return companyService.findAllByName(name);
    }
}
