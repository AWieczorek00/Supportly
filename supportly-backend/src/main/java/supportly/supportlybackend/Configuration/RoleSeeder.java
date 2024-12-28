package supportly.supportlybackend.Configuration;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import supportly.supportlybackend.Model.Role;
import supportly.supportlybackend.Enum.ERole;
import supportly.supportlybackend.Repository.RoleRepository;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;


    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();
    }

    private void loadRoles() {
        ERole[] roleNames = new ERole[] { ERole.USER, ERole.ADMIN, ERole.SUPER_ADMIN };
        Map<ERole, String> roleDescriptionMap = Map.of(
                ERole.USER, "Default user role",
                ERole.ADMIN, "Administrator role",
                ERole.SUPER_ADMIN, "Super Administrator role"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToCreate = new Role(roleName, roleDescriptionMap.get(roleName));

                roleRepository.save(roleToCreate);
            });
        });
    }
}