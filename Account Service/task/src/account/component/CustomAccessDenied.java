//package account.component;
//
//import account.model.User;
//import account.repository.UserRepository;
//import account.security.LoginAttemptService;
//import account.service.EventsService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import static account.enumeration.Events.*;
//import static account.enumeration.Operation.LOCK;
//import static account.enumeration.Role.*;
//
//@Component
//@Slf4j
//public class CustomAccessDenied implements AccessDeniedHandler {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Autowired
//    private EventsService eventsService;
//
//    @Autowired
//    private LoginAttemptService loginAttemptService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        response.setContentType("application/json");
//        Map<String, Object> errorDetails = new LinkedHashMap<>();
//        errorDetails.put("timestamp", LocalDateTime.now().toString());
//        if(checkCondition(request)){
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            errorDetails.put("status", HttpStatus.FORBIDDEN.value());
//            errorDetails.put("error",HttpStatus.FORBIDDEN.getReasonPhrase());
//            errorDetails.put("message", "Access Denied!");
//            eventsService.saveEvent(ACCESS_DENIED,
//                    request.getRemoteUser(),
//                    request.getRequestURI(),
//                    request.getRequestURI());
//        }
//        else{
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());
//            errorDetails.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
//            errorDetails.put("message", "Unauthorized");
//            eventsService.saveEvent(LOGIN_FAILED,
//                    request.getRemoteUser(),
//                    request.getRequestURI(),
//                    request.getRequestURI());
//            loginAttemptService.loginFailed(request.getRemoteUser());
//            if(loginAttemptService.isBlocked(request.getRemoteUser())){
//                eventsService.saveEvent(BRUTE_FORCE,
//                        request.getRemoteUser(),
//                        request.getRequestURI(),
//                        request.getRequestURI());
//                User user = userRepository
//                        .findByEmailIgnoreCase(request.getRemoteUser())
//                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!"));
//                user.setStatus(LOCK.name());
//                userRepository.save(user);
//                eventsService.saveEvent(LOCK_USER,
//                        request.getRemoteUser(),
//                        "Lock user " + request.getRemoteUser(),
//                        request.getRequestURI());
//            }
//        }
//        errorDetails.put("path", request.getRequestURI());
//        String errorResponse = objectMapper.writeValueAsString(errorDetails);
//        response.getWriter().write(errorResponse);
//        response.getWriter().flush();
//    }
//
//    private boolean checkCondition(HttpServletRequest request){
//        if(request.getRequestURI().contains("admin") && !request.isUserInRole(ADMINISTRATOR.getRole())){
//            return true;
//        }else if(request.getRequestURI().contains("acct/payments") && !request.isUserInRole(ACCOUNTANT.getRole())){
//            return true;
//        }else if(request.getRequestURI().contains("empl/payment") && request.isUserInRole(ADMINISTRATOR.getRole())){
//            return true;
//        }else return request.getRequestURI().contains("security/events") && !request.isUserInRole(AUDITOR.getRole());
//    }
//}
