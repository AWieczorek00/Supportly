package supportly.supportlybackend.Configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import supportly.supportlybackend.Dto.EmployeeDto;
import supportly.supportlybackend.Dto.RegisterUserDto;
import supportly.supportlybackend.Enum.ERole;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Model.Role;
import supportly.supportlybackend.Model.User;
import supportly.supportlybackend.Repository.RoleRepository;
import supportly.supportlybackend.Repository.UserRepository;
import supportly.supportlybackend.Service.EmployeeService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeService employeeService;


    @Override
    public void run(String... args) {
        createRoles();
        createSuperAdministrator();
    }

    private void createRoles() {
        for (ERole roleName : ERole.values()) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                Role role = new Role();
//                role.setName(roleName);
                role.setName(roleName);
                return roleRepository.save(role);
            });
        }
    }

    private void createSuperAdministrator() {
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setUsername("Super Admin");
        userDto.setEmail("super.admin@gmail.com");
        userDto.setPhoneNumber("000000000");
        userDto.setPassword("123456");

        Optional<Role> optionalRole = roleRepository.findByName(ERole.SUPER_ADMIN);
        Optional<Employee> employee = employeeService.findEmployeeByNumberPhone(userDto.getPhoneNumber());
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalRole.isEmpty() || employee.isEmpty() || existingUser.isPresent()) {
            return;
        }


        User user = new User(userDto.getUsername(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), optionalRole.get(),employee.get());

        userRepository.save(user);
    }
}
