package account.controller;

import account.security.LoginAttemptService;
import account.service.EventsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuditorController {

    @Autowired
    private EventsService eventsService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @GetMapping("/api/security/events/")
    public ResponseEntity<?> getAllEvents(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("antes pase caches");
        loginAttemptService.loginSucceeded(userDetails.getUsername());
        log.info("pase caches");
        return ResponseEntity.ok(eventsService.getAllEvents());
    }
}
