package supportly.supportlybackend.Mapper;

import org.mapstruct.factory.Mappers;
import supportly.supportlybackend.Dto.AgreementDto;
import supportly.supportlybackend.Dto.CompanyDto;
import supportly.supportlybackend.Dto.EmployeeDto;
import supportly.supportlybackend.Dto.OrderDto;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Model.Order;

public class Mapper {

    private static final AgreementMapper agreementMapper = Mappers.getMapper(AgreementMapper.class);
    private static final CompanyMapper companyMapper = Mappers.getMapper(CompanyMapper.class);
    private static final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);
    private static final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    public static Agreement toEntity(AgreementDto agreementDto) {
        return agreementMapper.toEntity(agreementDto);
    }

    public static AgreementDto toDto(Agreement agreement) {
        return agreementMapper.toDto(agreement);
    }

    public static Company toEntity(CompanyDto companyDto) {
        return companyMapper.toEntity(companyDto);
    }

    public static CompanyDto toDto(Company company) {
        return companyMapper.toDto(company);
    }

    public static Employee toEntity(EmployeeDto employeeDto) {
        return employeeMapper.toEntity(employeeDto);
    }

    public static EmployeeDto toDto(Employee employee) {
        return employeeMapper.toDto(employee);
    }

    public static Order toEntity(OrderDto orderDto) {
        return orderMapper.toEntity(orderDto);
    }

    public static OrderDto toDto(Order order) {
        return orderMapper.toDto(order);
    }

}
