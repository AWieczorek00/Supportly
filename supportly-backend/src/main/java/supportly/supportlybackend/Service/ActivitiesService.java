package supportly.supportlybackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Model.Activities;
import supportly.supportlybackend.Repository.ActivitiesRepository;

import java.util.List;

@Service
public class ActivitiesService {

    private final ActivitiesRepository activitiesRepository;

    @Autowired
    public ActivitiesService(ActivitiesRepository activitiesRepository) {
        this.activitiesRepository = activitiesRepository;
    }

    public void createAllActivitiesFromList(List<Activities> activitiesList) {
        activitiesRepository.saveAll(activitiesList);
    }

    public List<Activities> updateActivitiesList(List<Activities> newActivitiesList, List<Activities> oldActivitiesList) {
        activitiesRepository.deleteAll(oldActivitiesList);
        return activitiesRepository.saveAll(newActivitiesList);
    }
}
