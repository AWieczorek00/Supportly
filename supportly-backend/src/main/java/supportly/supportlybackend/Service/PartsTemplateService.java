package supportly.supportlybackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Model.Template.PartsTemplate;
import supportly.supportlybackend.Repository.PartsTemplateRepository;

import java.util.List;

@Service
public class PartsTemplateService {

    private final PartsTemplateRepository partsTemplateRepository;

    @Autowired
    public PartsTemplateService(PartsTemplateRepository partsTemplateRepository) {
        this.partsTemplateRepository = partsTemplateRepository;
    }

    public List<PartsTemplate> findAll(){
        return partsTemplateRepository.findAll();
    }
}
