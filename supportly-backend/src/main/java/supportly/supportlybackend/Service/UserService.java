package supportly.supportlybackend.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Dto.RegisterUserDto;
import supportly.supportlybackend.Enum.ERole;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Model.Role;
import supportly.supportlybackend.Model.User;
import supportly.supportlybackend.Repository.RoleRepository;
import supportly.supportlybackend.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public User createAdministrator(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(ERole.ADMIN);
        Optional<Employee> optionalEmployee = employeeService.findEmployeeByNumberPhone(input.getPhoneNumber());

        if (optionalRole.isEmpty() || optionalEmployee.isEmpty()) {
            return null;
        }

        User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()), optionalRole.get(), optionalEmployee.get());


        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public Optional<User> getUserByEmployee(Employee employee) {
        return userRepository.findByEmployeeId(employee.getIndividualId());
    }

}