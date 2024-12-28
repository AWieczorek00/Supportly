package supportly.supportlybackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Model.Template.ActivitiesTemplate;
import supportly.supportlybackend.Repository.ActivitiesTemplateRepository;

import java.util.List;

@Service
public class ActivitiesTemplateService {

    private final ActivitiesTemplateRepository activitiesTemplateRepository;

    @Autowired
    public ActivitiesTemplateService(ActivitiesTemplateRepository activitiesTemplateRepository) {
        this.activitiesTemplateRepository = activitiesTemplateRepository;
    }

    public List<ActivitiesTemplate> findAllActivitiesTemplate() {
        return activitiesTemplateRepository.findAll();
    }

    public ActivitiesTemplate createActivitiesTemplate(ActivitiesTemplate activitiesTemplate) {
        return activitiesTemplateRepository.save(activitiesTemplate);
    }
}
