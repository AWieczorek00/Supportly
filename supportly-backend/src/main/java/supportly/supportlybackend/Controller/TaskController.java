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
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @GetMapping("/search")
    public ResponseEntity<List<TaskDto>> search(@RequestBody TaskSC criteria) {
        List<TaskDto> taskList = taskService.search(criteria);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<Void> createTask(@RequestBody TaskDto taskBody) throws MessagingException {
        taskService.createTask(taskBody);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


//    @PutMapping("/update")
//    public ResponseEntity<Task> updateTask(@RequestBody Task taskBody) {
//        Task task = taskService.updateTask(taskBody);
//        return new ResponseEntity<>(task, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
//        taskService.deleteTaskById(id);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @PutMapping("/update/done")
//    public ResponseEntity<Task> updateTaskCompleted(@RequestBody Task taskBody) {
//        Task task = taskService.taskCompletion(taskBody);
//        return new ResponseEntity<>(task, HttpStatus.OK);
//    }
}
