package account.security;

import account.dto.LockUnlockUserResponseDTO;
import account.model.User;
import account.repository.UserRepository;
import account.service.EventsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static account.enumeration.Events.LOGIN_FAILED;
import static account.enumeration.Operation.LOCK;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private EventsService eventsService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException  {
        Optional<User> user = userRepository.findByEmailIgnoreCase(username);
        if(user.isEmpty()){
            eventsService.saveEvent(LOGIN_FAILED,
                    username,
                    request.getRequestURI(),
                    request.getRequestURI());
            throw new UsernameNotFoundException ("User exist!");
        }
//        else if (user.get().getStatus().equals(LOCK.name())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, new LockUnlockUserResponseDTO(username, LOCK).getStatus());
//        }
        else{
            return new UserDetailsImpl(user.get());
        }
    }
}