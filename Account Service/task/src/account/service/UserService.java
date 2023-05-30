package account.service;

import account.dto.DeleteUserResponseDTO;
import account.dto.EmptyJsonResponse;
import account.dto.NewPasswordResponseDTO;
import account.dto.SaveUserResponseDTO;
import account.model.Role;
import account.model.User;
import account.repository.RoleRepository;
import account.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static account.enumeration.Operation.UNLOCK;
import static account.enumeration.Role.ADMINISTRATOR;

@Service
@Slf4j
public class UserService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public Object saveUser(User user) {
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
        Optional<Role> role;
        if(userRepository.count() == 0){
            role = roleRepository.findByName("ROLE_ADMINISTRATOR");
        }else{
            role = roleRepository.findByName("ROLE_USER");
        }
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(role.orElse(null));
        user.setStatus(UNLOCK.name());
        userRepository.save(user);
        return new SaveUserResponseDTO(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).sorted().collect(Collectors.toList()));
    }

    public User getUser(String email) {
        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    public Object getAllUser(){
        log.info("estamos aqui");
        Optional<List<User>> users = userRepository.findAllByOrderByIdAsc();
        if(users.isPresent()){
            List<SaveUserResponseDTO> result = new ArrayList<>();
            for(User user : users.get()){
                result.add(
                        new SaveUserResponseDTO(
                                user.getId(),
                                user.getName(),
                                user.getLastName(),
                                user.getEmail(),
                                user.getRoles().stream().map(Role::getName).sorted().collect(Collectors.toList()))
                );
            }
            return result;
        }
        return List.of(new EmptyJsonResponse());

    }
    public NewPasswordResponseDTO changePassword(String email, String newPassword) {
        User user = getUser(email);

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            log.info("contraseñas iguales");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("se hizo el cambio");
        return new NewPasswordResponseDTO(email);
    }

    public Object deleteUser(String email){
        Optional<Role> checkRole = roleRepository.findByName("ROLE_" + ADMINISTRATOR.getRole());
        User user = userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        if(user.getRoles().contains(checkRole.get())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
        userRepository.delete(user);
        return new DeleteUserResponseDTO(email);
    }


}