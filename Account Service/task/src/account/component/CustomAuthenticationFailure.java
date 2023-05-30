//package account.component;
//
//import account.model.User;
//import account.repository.UserRepository;
//import account.security.LoginAttemptService;
//import account.service.EventsService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.ApplicationListener;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//import static account.enumeration.Events.*;
//import static account.enumeration.Operation.LOCK;
//
//@Component
//@Slf4j
//public class CustomAuthenticationFailure implements
//        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    @Autowired
//    private HttpServletRequest request;
//
//    @Autowired
//    private LoginAttemptService loginAttemptService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private EventsService eventsService;
//
//    @Autowired
//    private ApplicationEventPublisher applicationEventPublisher;
//
//    @Override
//    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
//
//
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
////        log.info("intentos de login fallidos");
////        log.info( event.getAuthentication().getName());
////        eventsService.saveEvent(LOGIN_FAILED,
////                event.getAuthentication().getName(),
////                request.getRequestURI(),
////                request.getRequestURI());
////        loginAttemptService.loginFailed(event.getAuthentication().getName());
////        if(loginAttemptService.isBlocked(event.getAuthentication().getName())){
////            eventsService.saveEvent(BRUTE_FORCE,
////                    event.getAuthentication().getName(),
////                    request.getRequestURI(),
////                    request.getRequestURI());
////            User user = userRepository
////                    .findByEmailIgnoreCase(event.getAuthentication().getName())
////                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!"));
////            user.setStatus(LOCK.name());
////            userRepository.save(user);
////            eventsService.saveEvent(LOCK_USER,
////                    event.getAuthentication().getName(),
////                    "Lock user " + event.getAuthentication().getName(),
////                    request.getRequestURI());
////        }
//    }
//}
//
