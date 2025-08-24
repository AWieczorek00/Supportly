package supportly.supportlybackend.Mapper;

import org.mapstruct.Mapper;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Service.TaskDto;

@Mapper()
public interface TaskMapper {
    TaskDto toDto(Task task);

    Task toEntity(TaskDto dto);
}
