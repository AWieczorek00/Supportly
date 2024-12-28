package supportly.supportlybackend.Security.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Dto.LoginDto;
import supportly.supportlybackend.Dto.RegisterUserDto;
import supportly.supportlybackend.Model.Role;
import supportly.supportlybackend.Model.User;
import supportly.supportlybackend.Enum.ERole;
import supportly.supportlybackend.Repository.RoleRepository;
import supportly.supportlybackend.Repository.UserRepository;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository, RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(ERole.USER);

        if (optionalRole.isEmpty()) {
            return null;
        }


        User user = new User(input.getFullName(),input.getEmail(),passwordEncoder.encode(input.getPassword()),optionalRole.get());


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
