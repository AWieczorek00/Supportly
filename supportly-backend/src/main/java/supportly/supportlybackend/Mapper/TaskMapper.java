package supportly.supportlybackend.Mapper;

import org.mapstruct.Mapper;
import supportly.supportlybackend.Model.Task;
import supportly.supportlybackend.Dto.TaskDto;

@Mapper(uses = {OrderMapper.class, EmployeeMapper.class})
public interface TaskMapper {
    TaskDto toDto(Task task);

    Task toEntity(TaskDto dto);
}
