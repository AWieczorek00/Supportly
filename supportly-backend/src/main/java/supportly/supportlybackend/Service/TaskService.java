package supportly.supportlybackend.Service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Model.User;
import supportly.supportlybackend.Repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final MailService mailService;

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> findAllTaskForEmployeeByIndividualId(Long individualId) {
        return taskRepository.findAllByEmployeeIndividualId(individualId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono żadnego zadania dla takie pracownika"));
    }

    public Task createTask(TaskDto taskDto) throws MessagingException {

        User user = userService.getUserByEmail(taskDto.getEmail());

        Task task = new Task();
        task.setExecutionTime(taskDto.getExecutionTime().plusDays(1));
        task.setName(taskDto.getName());
        task.setEmployee(user.getEmployee());
        task.setDone(taskDto.getDone());


        mailService.sendMail(user.getEmail());
        return taskRepository.save(task);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    public Task updateTask(Task task) {
        return taskRepository.findById(task.getId()).map(taskUpdate -> {
            taskUpdate.setName(task.getName());
            taskUpdate.setExecutionTime(task.getExecutionTime());
            taskUpdate.setEmployee(task.getEmployee());
            return taskRepository.save(taskUpdate);
        }).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zadania o id" + task.getId()));
    }

    public Task taskCompletion(Task task) {
        return taskRepository.findById(task.getId())
                .map(taskUpdate -> {
                    taskUpdate.setDone(task.getDone());
                    return taskRepository.save(taskUpdate);
                }).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zadania o id" + task.getId()));
    }

}
