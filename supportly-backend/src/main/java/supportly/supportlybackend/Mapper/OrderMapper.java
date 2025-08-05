package supportly.supportlybackend.Mapper;

import org.mapstruct.Mapper;
import supportly.supportlybackend.Dto.OrderDto;
import supportly.supportlybackend.Model.Order;

@Mapper(uses = {ClientMapper.class, EmployeeMapper.class})

public interface OrderMapper {

    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);


}
