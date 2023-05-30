package account.controller;

import account.dto.NewPasswordDTO;
import account.dto.NewPasswordResponseDTO;
import account.enumeration.EndPointPath;
import account.enumeration.Events;
import account.model.User;
import account.security.LoginAttemptService;
import account.service.EventsService;
import account.service.UserService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static account.enumeration.Events.CHANGE_PASSWORD;
import static account.enumeration.Events.CREATE_USER;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventsService eventsService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @PostMapping("/api/auth/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody User user,  @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails != null){
            loginAttemptService.loginSucceeded(userDetails.getUsername());
        }
        log.info("/api/auth/signup");
        log.info(user.getEmail());
        log.info(user.getPassword());
        Object result = userService.saveUser(user);
        eventsService.saveEvent(CREATE_USER,
                userDetails == null ? null : userDetails.getUsername(),
                user.getEmail(),
                EndPointPath.CREATE_USER.getEndPoint());
        return ResponseEntity.ok(result);
    }
    @PostMapping("/api/auth/changepass")
    public NewPasswordResponseDTO changePassword(@Valid @RequestBody NewPasswordDTO newPassword,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        loginAttemptService.loginSucceeded(userDetails.getUsername());
        log.info("/api/auth/changepass");
        log.info(userDetails.getUsername());
        log.info(userDetails.getPassword());
        log.info(newPassword.getNewPassword());
        NewPasswordResponseDTO result = userService.changePassword(userDetails.getUsername(), newPassword.getNewPassword());
        log.info(String.valueOf(result));
        eventsService.saveEvent(CHANGE_PASSWORD,
                userDetails.getUsername(),
                userDetails.getUsername(),
                EndPointPath.CHANGE_PASSWORD.getEndPoint());
        return result;
    }
}