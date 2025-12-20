package supportly.supportlybackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import supportly.supportlybackend.Criteria.TaskSC;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Repository.EmployeeRepository;
import supportly.supportlybackend.Repository.TaskRepository;
import supportly.supportlybackend.Dto.TaskDto;
import supportly.supportlybackend.Service.TaskService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
public class TaskServiceTest {


    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

//    @BeforeEach
//    void setUp() {
//        sampleTask = new TaskDto();
//        sampleTask.setName("Test task");
//        sampleTask.setDone(false);
//
//        taskService.createTask(sampleTask);
//    }
//
//    @Test
//    void shouldCreateTask() {
//        List<Task> tasks = taskRepository.findAll();
//        assertThat(tasks).hasSize(1);
//        assertThat(tasks.get(0).getName()).isEqualTo("Test task");
//    }
//
//    @Test
//    void shouldSearchTaskByName() {
//        TaskSC criteria = new TaskSC();
//        criteria.setName("Test task");
//
//        List<TaskDto> result = taskService.search(criteria);
//
//        assertThat(result).isNotEmpty();
//        assertThat(result.get(0).getName()).contains("Test task");
//    }
//
//    @Test
//    void shouldUpdateTaskDoneStatus() {
//        Task existing = taskRepository.findAll().get(0);
//
//        TaskDto dto = new TaskDto();
//        dto.setId(existing.getId());
//        dto.setName(existing.getName());
//        dto.setDone(true);
//
//        TaskDto updated = taskService.daneTask(dto);
//
//        assertThat(updated.getDone()).isTrue();
//        assertThat(taskRepository.findById(existing.getId()).orElseThrow().getDone()).isTrue();
//    }
//
//    @Test
//    void shouldGetTasksForEmployee() {
//        // przygotowanie testowych danych
//        Task task = taskRepository.findAll().get(0);
//        // zakładam, że masz relację Task -> Employees i ustawiasz individualId
//        // np. task.setEmployees(List.of(new Employee(...)))
//        // taskRepository.save(task);
//
//        List<TaskDto> result = taskService.getTasksForEmployee(123L);
//
//        assertThat(result).isEmpty();
//    }

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("createTask: Powinien zapisać nowe zadanie w bazie")
    void shouldCreateTask() {
        // Given
        TaskDto dto = new TaskDto();
        dto.setName("Naprawa drukarki");
        dto.setDone(false);
        dto.setExecutionTime(LocalDate.now().plusDays(1));

        // When
        taskService.createTask(dto);

        // Then
        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getName()).isEqualTo("Naprawa drukarki");
        assertThat(tasks.get(0).getDone()).isFalse();
    }

    @Test
    @DisplayName("daneTask: Powinien zaktualizować status done na true")
    void shouldUpdateTaskStatusToDone() {
        // Given
        Task task = new Task();
        task.setName("Test Task");
        task.setDone(false);
        task = taskRepository.save(task);

        TaskDto inputDto = new TaskDto();
        inputDto.setId(task.getId());
        inputDto.setDone(true); // Chcemy zmienić na true

        // When
        TaskDto result = taskService.daneTask(inputDto);

        // Then
        assertThat(result.getDone()).isTrue();

        // Weryfikacja bezpośrednio w bazie
        Task fromDb = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(fromDb.getDone()).isTrue();
    }

    @Test
    @DisplayName("daneTask: Powinien rzucić wyjątek, gdy ID zadania nie istnieje")
    void shouldThrowExceptionWhenUpdatingNonExistentTask() {
        // Given
        TaskDto inputDto = new TaskDto();
        inputDto.setId(999L);
        inputDto.setDone(true);

        // When & Then
        assertThatThrownBy(() -> taskService.daneTask(inputDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Nie udało się zaktualizować taska id=999");
    }

    @Test
    @DisplayName("getTasksForEmployee: Powinien zwrócić tylko niewykonane zadania dla konkretnego pracownika")
    void shouldReturnOpenTasksForSpecificEmployee() {
        // Given
        // 1. Tworzymy Pracownika
        Employee employee = new Employee();
        employee.setFirstName("Jan");
        employee.setLastName("Kowalski");
        // Ustaw wymagane pola (np. phone, email) jeśli encja tego wymaga
        employee.setPhoneNumber("123456789");
        employee = employeeRepository.save(employee);

        // 2. Tworzymy Zadanie A (Dla tego pracownika, NIEWYKONANE) -> Powinno znaleźć
        Task taskOpen = new Task();
        taskOpen.setName("Zadanie Otwarte");
        taskOpen.setDone(false);
        taskOpen.setEmployees(List.of(employee));
        taskRepository.save(taskOpen);

        // 3. Tworzymy Zadanie B (Dla tego pracownika, WYKONANE) -> Nie powinno znaleźć
        Task taskClosed = new Task();
        taskClosed.setName("Zadanie Zamknięte");
        taskClosed.setDone(true);
        taskClosed.setEmployees(List.of(employee));
        taskRepository.save(taskClosed);

        // 4. Tworzymy Zadanie C (Dla INNEGO pracownika) -> Nie powinno znaleźć
        Employee otherEmployee = new Employee();
        otherEmployee.setFirstName("Adam");
        otherEmployee.setLastName("Inny");
        otherEmployee.setPhoneNumber("987654321");
        employeeRepository.save(otherEmployee);

        Task taskOther = new Task();
        taskOther.setName("Zadanie Innego");
        taskOther.setDone(false);
        taskOther.setEmployees(List.of(otherEmployee));
        taskRepository.save(taskOther);

        // When
        List<TaskDto> result = taskService.getTasksForEmployee(employee.getIndividualId());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Zadanie Otwarte");
    }

//    @Test
//    @DisplayName("search: Powinien filtrować zadania po kryteriach (np. nazwa)")
//    void shouldSearchTasksByCriteria() {
//        // Given
//        Task t1 = new Task();
//        t1.setName("Konfiguracja VPN");
//        t1.setDone(false);
//        taskRepository.save(t1);
//
//        Task t2 = new Task();
//        t2.setName("Instalacja Office");
//        t2.setDone(false);
//        taskRepository.save(t2);
//
//        TaskSC criteria = new TaskSC();
//        criteria.setName("Konfiguracja"); // Zakładam, że builder obsługuje 'contains' lub 'equals'
//
//        // When
//        List<TaskDto> result = taskService.search(criteria);
//
//        // Then
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).getName()).isEqualTo("Konfiguracja VPN");
//    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus2() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus3() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus4() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus5() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus6() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus7() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus8() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus9() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus10() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus11() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus12() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus13() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus14() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus15() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus16() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus17() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus18() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus19() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus20() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus21() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus22() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus23() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }

    @Test
    @DisplayName("search: Powinien filtrować po statusie done")
    void shouldSearchTasksByDoneStatus24() {
        // Given
        Task t1 = new Task();
        t1.setName("Task 1");
        t1.setDone(true);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("Task 2");
        t2.setDone(false);
        taskRepository.save(t2);

        TaskSC criteria = new TaskSC();
        criteria.setDone(true);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 1");
    }
}
