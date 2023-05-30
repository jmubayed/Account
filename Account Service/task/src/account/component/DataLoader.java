package account.component;

import account.model.Role;
import account.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private RoleRepository roleRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        createRoles();
    }

    private void createRoles() {
        try {
            roleRepository.save(new Role(null,"ROLE_ADMINISTRATOR"));
            roleRepository.save(new Role(null,"ROLE_USER"));
            roleRepository.save(new Role(null,"ROLE_ACCOUNTANT"));
            roleRepository.save(new Role(null,"ROLE_AUDITOR"));
        } catch (Exception e) {

        }
    }
}
