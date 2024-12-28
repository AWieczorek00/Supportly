package supportly.supportlybackend.Configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import supportly.supportlybackend.Dto.RegisterUserDto;
import supportly.supportlybackend.Enum.ERole;
import supportly.supportlybackend.Model.Role;
import supportly.supportlybackend.Model.User;
import supportly.supportlybackend.Repository.RoleRepository;
import supportly.supportlybackend.Repository.UserRepository;

import java.util.Optional;

@Component
public class AdminSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
        userDto.setFullName("Super Admin");
        userDto.setEmail("super.admin@email.com");
        userDto.setPassword("123456");

        Optional<Role> optionalRole = roleRepository.findByName(ERole.SUPER_ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        User user = new User(userDto.getFullName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), optionalRole.get());

        userRepository.save(user);
    }
}
