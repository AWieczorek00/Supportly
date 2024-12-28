package supportly.supportlybackend.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Dto.RegisterUserDto;
import supportly.supportlybackend.Model.Role;
import supportly.supportlybackend.Model.User;
import supportly.supportlybackend.Enum.ERole;
import supportly.supportlybackend.Repository.RoleRepository;
import supportly.supportlybackend.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public User createAdministrator(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(ERole.ADMIN);

        if (optionalRole.isEmpty()) {
            return null;
        }

        User user = new User(input.getFullName(), input.getEmail(), passwordEncoder.encode(input.getPassword()), optionalRole.get());


        return userRepository.save(user);
    }
}