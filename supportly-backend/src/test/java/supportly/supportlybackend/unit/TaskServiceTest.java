package supportly.supportlybackend.unit;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import supportly.supportlybackend.Criteria.TaskSC;
import supportly.supportlybackend.Dto.TaskDto;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Repository.TaskRepository;
import supportly.supportlybackend.Service.TaskService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    // Obiekt do obsługi mockowania metod statycznych (Mapper)
    private MockedStatic<Mapper> mapperMock;

    @BeforeEach
    void setUp() {
        // Otwieramy mockowanie statyczne przed każdym testem
        mapperMock = Mockito.mockStatic(Mapper.class);
    }

    @AfterEach
    void tearDown() {
        // Zamykamy mockowanie statyczne po każdym teście, aby nie wpływało na inne
        mapperMock.close();
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto2() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto3() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto4() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto5() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto6() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto7() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto8() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto9() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto10() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto11() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto12() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto13() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto14() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto15() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto16() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto17() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto18() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto19() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto20() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("search: Powinien zwrócić listę TaskDto na podstawie kryteriów")
    void search_ShouldReturnListOfTaskDto21() {
        // Given
        TaskSC criteria = new TaskSC();
        criteria.setName("Test Task");

        Task taskEntity = new Task();
        taskEntity.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);

        // Mockowanie zachowania repozytorium
        // Używamy any(Specification.class), bo builder jest tworzony wewnątrz metody
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of(taskEntity));

        // Mockowanie Mappera
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(taskDto);
        verify(taskRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("createTask: Powinien zapisać zmapowaną encję w repozytorium")
    void createTask_ShouldSaveMappedEntity() {
        // Given
        TaskDto taskDto = new TaskDto();
        taskDto.setName("New Task");
        Task taskEntity = new Task();

        mapperMock.when(() -> Mapper.toEntity(taskDto)).thenReturn(taskEntity);

        // When
        taskService.createTask(taskDto);

        // Then
        verify(taskRepository).save(taskEntity);
        mapperMock.verify(() -> Mapper.toEntity(taskDto));
    }

    @Test
    @DisplayName("daneTask: Powinien zaktualizować status i zwrócić DTO, gdy update się powiedzie")
    void daneTask_ShouldUpdateAndReturnDto_WhenUpdateSuccess() {
        // Given
        Long taskId = 100L;
        Boolean doneStatus = true;
        TaskDto inputDto = new TaskDto();
        inputDto.setId(taskId);
        inputDto.setDone(doneStatus);

        Task taskEntity = new Task();
        TaskDto outputDto = new TaskDto();
        outputDto.setId(taskId);
        outputDto.setDone(doneStatus);

        // Mock: updateDone zwraca 1 (sukces)
        when(taskRepository.updateDone(taskId, doneStatus)).thenReturn(1);
        // Mock: pobranie encji po ID
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        // Mock: mapowanie
        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(outputDto);

        // When
        TaskDto result = taskService.daneTask(inputDto);

        // Then
        assertThat(result).isEqualTo(outputDto);
        verify(taskRepository).updateDone(taskId, doneStatus);
        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("daneTask: Powinien rzucić IllegalStateException, gdy update zwróci 0")
    void daneTask_ShouldThrowException_WhenUpdateFails() {
        // Given
        Long taskId = 100L;
        TaskDto inputDto = new TaskDto();
        inputDto.setId(taskId);
        inputDto.setDone(true);

        // Mock: updateDone zwraca 0 (brak zmiany w bazie)
        when(taskRepository.updateDone(taskId, true)).thenReturn(0);

        // When & Then
        assertThatThrownBy(() -> taskService.daneTask(inputDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Nie udało się zaktualizować taska id=" + taskId);

        // Upewniamy się, że findById NIE zostało wywołane
        verify(taskRepository, never()).findById(any());
    }

    @Test
    @DisplayName("daneTask: Powinien rzucić wyjątek, gdy update się udał, ale nie znaleziono encji")
    void daneTask_ShouldThrowException_WhenEntityNotFoundAfterUpdate() {
        // Given
        Long taskId = 100L;
        TaskDto inputDto = new TaskDto();
        inputDto.setId(taskId);
        inputDto.setDone(true);

        when(taskRepository.updateDone(taskId, true)).thenReturn(1);
        // Symulacja sytuacji wyścigu (race condition) lub błędu danych
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        // orElseThrow() bez argumentów rzuca zazwyczaj NoSuchElementException
        assertThatThrownBy(() -> taskService.daneTask(inputDto))
                .isInstanceOf(NoSuchElementException.class);
    }



    @Test
    @DisplayName("getTasksForEmployee: Powinien zwrócić listę zadań dla pracownika")
    void getTasksForEmployee_ShouldReturnMappedTasks() {
        // Given
        Long employeeId = 55L;
        Task taskEntity = new Task();
        TaskDto taskDto = new TaskDto();

        when(taskRepository.findAllByEmployees_IndividualIdAndDone(employeeId, false))
                .thenReturn(List.of(taskEntity));

        mapperMock.when(() -> Mapper.toDto(taskEntity)).thenReturn(taskDto);

        // When
        List<TaskDto> result = taskService.getTasksForEmployee(employeeId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).contains(taskDto);
        verify(taskRepository).findAllByEmployees_IndividualIdAndDone(employeeId, false);
    }

    @Test
    @DisplayName("getTasksForEmployee: Powinien zwrócić pustą listę, gdy brak zadań")
    void getTasksForEmployee_ShouldReturnEmptyList() {
        // Given
        Long employeeId = 55L;

        when(taskRepository.findAllByEmployees_IndividualIdAndDone(employeeId, false))
                .thenReturn(Collections.emptyList());

        // When
        List<TaskDto> result = taskService.getTasksForEmployee(employeeId);

        // Then
        assertThat(result).isEmpty();
        // Mapper nie powinien być wołany
        mapperMock.verifyNoInteractions();
    }
}