package supportly.supportlybackend.Mapper;

import org.mapstruct.Mapper;
import supportly.supportlybackend.Dto.EmployeeDto;
import supportly.supportlybackend.Model.Employee;

@Mapper()
public interface EmployeeMapper {

    EmployeeDto toDto(Employee employee);
    Employee toEntity(EmployeeDto dto);
}
