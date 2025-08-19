package supportly.supportlybackend.Mapper;

import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Service.TaskDto;

public interface TaskMapper {
    TaskDto toDto(Task task);

    Task toEntity(TaskDto dto);
}
