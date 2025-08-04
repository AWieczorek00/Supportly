package supportly.supportlybackend.Security.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Dto.LoginDto;
import supportly.supportlybackend.Dto.RegisterUserDto;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Model.Role;
import supportly.supportlybackend.Model.User;
import supportly.supportlybackend.Enum.ERole;
import supportly.supportlybackend.Repository.RoleRepository;
import supportly.supportlybackend.Repository.UserRepository;
import supportly.supportlybackend.Service.EmployeeService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmployeeService employeeService;

    private final AuthenticationManager authenticationManager;


    public User signup(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(ERole.USER);
        Optional<Employee> optionalEmployee = employeeService.findEmployeeByNumberPhone(input.getPhoneNumber());

        if (optionalRole.isEmpty() || optionalEmployee.isEmpty()) {
            throw new RuntimeException("Role or Employee not found");
        }

        //todo: Dokończ rejestracje nowego użytkownika

        User user = new User(input.getUsername(),input.getEmail(),passwordEncoder.encode(input.getPassword()),optionalRole.get(),optionalEmployee.get());

        return userRepository.save(user);
    }

    public User authenticate(LoginDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
