package supportly.supportlybackend.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportly.supportlybackend.Criteria.GenericSpecificationBuilder;
import supportly.supportlybackend.Criteria.TaskSC;
import supportly.supportlybackend.Dto.TaskDto;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
//    private final OrderService orderService;
//    private final UserService userService;
//    private final MailService mailService;

    public List<TaskDto> search(TaskSC criteria) {
        GenericSpecificationBuilder<Task> builder = new GenericSpecificationBuilder<>();
        Specification<Task> spec = builder.build(criteria);
        return taskRepository.findAll(spec).stream().map(Mapper::toDto).collect(Collectors.toList());
    }


    public void createTask(TaskDto task) {
        Task taskEntity = Mapper.toEntity(task);
        taskRepository.save(taskEntity);
    }

    @Transactional
    public TaskDto daneTask(TaskDto task) {
        int updated = taskRepository.updateDone(task.getId(), task.getDone());
        if (updated != 1) {
            throw new IllegalStateException("Nie udało się zaktualizować taska id=" + task.getId());
        }
        return Mapper.toDto(taskRepository.findById(task.getId()).orElseThrow());
    }

    public List<TaskDto> getTasksForEmployee(Long individualId) {
        List<Task> allByEmployeesIndividualId = taskRepository.findAllByEmployees_IndividualIdAndDone(individualId, false);
        return allByEmployeesIndividualId.stream().map(Mapper::toDto).collect(Collectors.toList());
    }
}
