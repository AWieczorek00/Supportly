package supportly.supportlybackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import supportly.supportlybackend.Criteria.TaskSC;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Repository.TaskRepository;
import supportly.supportlybackend.Service.TaskDto;
import supportly.supportlybackend.Service.TaskService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
public class TaskServiceIT {


    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    private TaskDto sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new TaskDto();
        sampleTask.setName("Test task");
        sampleTask.setDone(false);

        taskService.createTask(sampleTask);
    }

    @Test
    void shouldCreateTask() {
        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getName()).isEqualTo("Test task");
    }

    @Test
    void shouldSearchTaskByName() {
        TaskSC criteria = new TaskSC();
        criteria.setName("Test task");

        List<TaskDto> result = taskService.search(criteria);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).contains("Test task");
    }

    @Test
    void shouldUpdateTaskDoneStatus() {
        Task existing = taskRepository.findAll().get(0);

        TaskDto dto = new TaskDto();
        dto.setId(existing.getId());
        dto.setName(existing.getName());
        dto.setDone(true);

        TaskDto updated = taskService.daneTask(dto);

        assertThat(updated.getDone()).isTrue();
        assertThat(taskRepository.findById(existing.getId()).orElseThrow().getDone()).isTrue();
    }

    @Test
    void shouldGetTasksForEmployee() {
        // przygotowanie testowych danych
        Task task = taskRepository.findAll().get(0);
        // zakładam, że masz relację Task -> Employees i ustawiasz individualId
        // np. task.setEmployees(List.of(new Employee(...)))
        // taskRepository.save(task);

        List<TaskDto> result = taskService.getTasksForEmployee(123L);

        assertThat(result).isEmpty();
    }
}
