package supportly.supportlybackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supportly.supportlybackend.Model.Template.ActivitiesTemplate;
import supportly.supportlybackend.Service.ActivitiesTemplateService;

import java.util.List;

@RestController
@RequestMapping("/activitiesTemplate")
@CrossOrigin(origins = "*")
public class ActivitiesTemplateController {

    private final ActivitiesTemplateService activitiesTemplateService;

    @Autowired
    public ActivitiesTemplateController(ActivitiesTemplateService activitiesTemplateService) {
        this.activitiesTemplateService = activitiesTemplateService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ActivitiesTemplate>> getAllActivities() {
        List<ActivitiesTemplate> activitiesList = activitiesTemplateService.findAllActivitiesTemplate();
        return new ResponseEntity<>(activitiesList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ActivitiesTemplate> createActivitiesTemplate(@RequestBody ActivitiesTemplate activitiesTemplateBody) {
        ActivitiesTemplate activities = activitiesTemplateService.createActivitiesTemplate(activitiesTemplateBody);
        return new ResponseEntity<>(activities, HttpStatus.CREATED);
    }
}
