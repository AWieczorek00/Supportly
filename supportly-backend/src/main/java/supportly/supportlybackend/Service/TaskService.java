package supportly.supportlybackend.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Criteria.GenericSpecificationBuilder;
import supportly.supportlybackend.Criteria.TaskSC;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final MailService mailService;

    public List<TaskDto> search(TaskSC criteria) {
        GenericSpecificationBuilder<Task> builder = new GenericSpecificationBuilder<>();
        Specification<Task> spec = builder.build(criteria);
        return taskRepository.findAll(spec).stream().map(Mapper::toDto).collect(Collectors.toList());
    }


    public void createTask(TaskDto task) {
        taskRepository.saveAndFlush(Mapper.toEntity(task));
    }
}
