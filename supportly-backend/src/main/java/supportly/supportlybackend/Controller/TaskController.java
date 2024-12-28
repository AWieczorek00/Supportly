package supportly.supportlybackend.Controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import supportly.supportlybackend.Dto.EmailDto;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Service.MailService;
import supportly.supportlybackend.Service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class TaskController {

    private final TaskService taskService;

    private final MailService mailService;

    @Autowired
    public TaskController(TaskService taskService, MailService mailService) {
        this.taskService = taskService;
        this.mailService = mailService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> taskList = taskService.findAllTasks();
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @GetMapping("/employee/all")
    public ResponseEntity<List<Task>> getAllTaskByEmployee(@RequestParam Long individualId) {
        List<Task> taskList = taskService.findAllTaskForEmployeeByIndividualId(individualId);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Task> createTask(@RequestBody Task taskBody) throws MessagingException {
        Task task = taskService.createTask(taskBody);
        EmailDto emailDto = new EmailDto(taskBody.getEmployee().getEmail(), "Dostałeś/aś nowe zadanie",
                "<p style=\"text - align:center;\"><strong>Do twojego konta zostało dodane nowe zadanie.</strong></p>" +
                "\n" +
                "<p style=\" text - align:center;\"><strong>Sprawdzi je u siebie</strong></p>", true);
        mailService.sendMail(emailDto);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task taskBody) {
        Task task = taskService.updateTask(taskBody);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/done")
    public ResponseEntity<Task> updateTaskCompleted(@RequestBody Task taskBody) {
        Task task = taskService.taskCompletion(taskBody);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
