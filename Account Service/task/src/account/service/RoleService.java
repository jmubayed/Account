package account.service;

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
import static account.enumeration.Role.ADMINISTRATOR;
@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public Object addRole(UpdateUserRoleDTO updateUserRoleDTO){
        User user = userRepository.findByEmailIgnoreCase(updateUserRoleDTO.getUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        checkIfUserHaveRole(updateUserRoleDTO.getRole(), user, updateUserRoleDTO.getOperation());
        checkRoles(user, updateUserRoleDTO.getRole());
        Optional<Role> role = roleRepository.findByName("ROLE_" + updateUserRoleDTO.getRole());
        user.getRoles().add(role.orElse(null));
        userRepository.save(user);
        return new SaveUserResponseDTO(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).sorted().collect(Collectors.toList()));
    }

    public SaveUserResponseDTO deleteRole(UpdateUserRoleDTO updateUserRoleDTO){
        User user = userRepository.findByEmailIgnoreCase(updateUserRoleDTO.getUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        checkIfUserIsAdministration(user);
        checkIfUserHaveRole(updateUserRoleDTO.getRole(), user, updateUserRoleDTO.getOperation());
        checkNumberOfUserRole(user);
        Optional<Role> role = roleRepository.findByName("ROLE_" + updateUserRoleDTO.getRole());
        user.getRoles().remove(role.orElse(null));
        userRepository.save(user);
        return new SaveUserResponseDTO(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).sorted().collect(Collectors.toList()));
    }

    private void checkIfUserHaveRole(String role, User user, Operation operation){
        Optional<Role> checkRole = roleRepository.findByName("ROLE_" + role);
        if(operation.equals(GRANT)){
            if(checkRole.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
            }
        }else{
            if(!user.getRoles().contains(checkRole.get())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
            }
        }
    }

    private void checkIfUserIsAdministration(User user){
        if(user.getRoles().contains(roleRepository.findByName("ROLE_" + ADMINISTRATOR.getRole()).get())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
    }

    private void checkNumberOfUserRole(User user){
        if(user.getRoles().size() == 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
        }
    }

    public void checkRoles(User user, String role){
        Optional<Role> checkRole = roleRepository.findByName("ROLE_" + ADMINISTRATOR.getRole());
        if(user.getRoles().contains(checkRole.get()) && !role.equals(ADMINISTRATOR.getRole()) ||
                !user.getRoles().contains(checkRole.get()) && role.equals(ADMINISTRATOR.getRole())
        ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }
    }
}
