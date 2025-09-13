package supportly.supportlybackend.Controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import supportly.supportlybackend.Criteria.TaskSC;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Service.MailService;
import supportly.supportlybackend.Service.TaskDto;
import supportly.supportlybackend.Service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @PostMapping("/search")
    public ResponseEntity<List<TaskDto>> search(@RequestBody TaskSC criteria) {
        List<TaskDto> taskList = taskService.search(criteria);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<Void> createTask(@RequestBody TaskDto taskBody)  {
        taskService.createTask(taskBody);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{individualId}")
    public ResponseEntity<List<TaskDto>> getTasksForEmployee(@PathVariable Long individualId) {
        taskService.getTasksForEmployee(individualId);
        return new ResponseEntity<>(taskService.getTasksForEmployee(individualId), HttpStatus.OK);
    }

    @PutMapping("/update/done")
    public ResponseEntity<TaskDto> daneTask(@RequestBody TaskDto taskBody) {
        TaskDto task = taskService.daneTask(taskBody);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
