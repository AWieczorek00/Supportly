package supportly.supportlybackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> findAllTaskForEmployeeByIndividualId(Long individualId) {
        return taskRepository.findAllByEmployeeIndividualId(individualId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono żadnego zadania dla takie pracownika"));
    }

    public Task createTask(Task task) {
        task.setExecutionTime(task.getExecutionTime().plusDays(1));
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
        }).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zadania o id"+task.getId()));
    }

    public Task taskCompletion(Task task) {
        return taskRepository.findById(task.getId())
                .map(taskUpdate -> {
                    taskUpdate.setDone(task.getDone());
                    return taskRepository.save(taskUpdate);
                }).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zadania o id"+task.getId()));
    }
}
