package supportly.supportlybackend.Service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Criteria.GenericSpecificationBuilder;
import supportly.supportlybackend.Criteria.TaskSC;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Model.Order;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Model.User;
import supportly.supportlybackend.Repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public List<Task> findAllTaskForEmployeeByIndividualId(Long individualId) {
        return taskRepository.findAllByEmployeeIndividualId(individualId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono żadnego zadania dla takie pracownika"));
    }

//    public Task createTask(TaskDto taskDto) throws MessagingException {
//
//        User user = userService.getUserByEmail(taskDto.getEmail());
//
//        Task task = new Task();
//        task.setExecutionTime(taskDto.getExecutionTime().plusDays(1));
//        task.setName(taskDto.getName());
//        task.setDone(taskDto.getDone());
//
//        mailService.sendMail(user.getEmail());
//        return taskRepository.save(task);
//    }
//
//    public void addEmployeesToTask(TaskDto task) {
//        return taskRepository.findTaskByOrder(Mapper.toEntity(task.getOrder())).map(taskUpdate -> {
//            taskUpdate.setEmployee(task.getEmployees());
//            return taskRepository.save(taskUpdate);
//        }).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zadania o id" + task.getId()));
//
//    }
//
//    public void deleteTaskById(Long id) {
//        taskRepository.deleteById(id);
//    }
//
//    public Task updateTask(Task task) {
//        return taskRepository.findById(task.getId()).map(taskUpdate -> {
//            taskUpdate.setName(task.getName());
//            taskUpdate.setExecutionTime(task.getExecutionTime());
//            taskUpdate.setEmployee(task.getEmployee());
//            return taskRepository.save(taskUpdate);
//        }).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zadania o id" + task.getId()));
//    }
//
//    public Task taskCompletion(Task task) {
//        return taskRepository.findById(task.getId())
//                .map(taskUpdate -> {
//                    taskUpdate.setDone(task.getDone());
//                    return taskRepository.save(taskUpdate);
//                }).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zadania o id" + task.getId()));
//    }

    public void createTask(TaskDto task) {
        ArrayList<Task> taskList = new ArrayList<>();

        task.getEmployees().forEach(employee -> {
            Task taskToAdd = new Task();
            taskToAdd.setDone(false);
            taskToAdd.setName(task.getName());
            taskToAdd.setEmployee(employee);
            taskToAdd.setOrder(Mapper.toEntity(task.getOrder()));
            taskToAdd.setExecutionTime(task.getExecutionTime());

            taskList.add(taskToAdd);
        });


        taskRepository.saveAll(taskList);
        task.getEmployees().forEach(employee -> {
            Optional<User> user = userService.getUserByEmployee(employee);
            if (user.isPresent()) {
                try {
                    mailService.sendMail(user.get().getEmail());
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    public void groupTask() {
        TaskSC taskSC = new TaskSC();
        taskSC.setOrder(new Order());
        taskSC.setEmployee(new Employee());

        List<TaskDto> taskDtoList = search(taskSC);
        taskDtoList.stream().map(taskDto -> {
            Task entity = Mapper.toEntity(taskDto);
            entity.setGroup(taskDto);
        });



    }

}
