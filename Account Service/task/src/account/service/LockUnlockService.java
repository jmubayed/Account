package account.service;

import account.dto.LockUnlockUserDTO;
import account.dto.LockUnlockUserResponseDTO;
import account.dto.SaveUserResponseDTO;
import account.dto.UpdateUserRoleDTO;
import account.enumeration.Operation;
import account.model.Role;
import account.model.User;
import account.repository.RoleRepository;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;

import static account.enumeration.Operation.GRANT;
import static account.enumeration.Operation.LOCK;
import static account.enumeration.Role.ADMINISTRATOR;

@Service
@Transactional
public class LockUnlockService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public Object lockAndUnlockUser(LockUnlockUserDTO lockUnlockUserDTO){
        User user = userRepository.findByEmailIgnoreCase(lockUnlockUserDTO.getUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        if(lockUnlockUserDTO.getOperation().equals(LOCK)){
            checkIfUserIsAdministration(user);
        }
        user.setStatus(lockUnlockUserDTO.getOperation().name());
        userRepository.save(user);
        return new LockUnlockUserResponseDTO(
                lockUnlockUserDTO.getUser(),
                lockUnlockUserDTO.getOperation()
        );
    }

    private void checkIfUserIsAdministration(User user){
        if(user.getRoles().contains(roleRepository.findByName("ROLE_" + ADMINISTRATOR.getRole()).get())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");
        }
    }
}
