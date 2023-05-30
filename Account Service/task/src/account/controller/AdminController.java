package account.controller;

import account.dto.LockUnlockUserDTO;
import account.dto.UpdateUserRoleDTO;
import account.enumeration.EndPointPath;
import account.security.LoginAttemptService;
import account.service.EventsService;
import account.service.LockUnlockService;
import account.service.RoleService;
import account.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static account.enumeration.Events.*;
import static account.enumeration.Operation.*;

@RestController
@Slf4j
public class AdminController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private LockUnlockService lockUnlockService;

    @Autowired
    private EventsService eventsService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @PutMapping("/api/admin/user/role")
    public ResponseEntity<?> updateOrRemoveUserRoles(@Valid @RequestBody UpdateUserRoleDTO updateUserRoleDTO,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        log.info("/api/admin/user/role");
        log.info(updateUserRoleDTO.toString());
        loginAttemptService.loginSucceeded(userDetails.getUsername());
        Object result = null;
        if(updateUserRoleDTO.getOperation().equals(GRANT)){
            result = roleService.addRole(updateUserRoleDTO);
            eventsService.saveEvent(GRANT_ROLE,
                    userDetails == null ? null : userDetails.getUsername(),
                    "Grant role " + updateUserRoleDTO.getRole() + " to " + updateUserRoleDTO.getUser().toLowerCase(),
                    EndPointPath.GRANT_ROLE.getEndPoint());
        }else if (updateUserRoleDTO.getOperation().equals(REMOVE)){
            result = roleService.deleteRole(updateUserRoleDTO);
            eventsService.saveEvent(REMOVE_ROLE,
                    userDetails == null ? null : userDetails.getUsername(),
                    "Remove role " + updateUserRoleDTO.getRole() + " from " + updateUserRoleDTO.getUser().toLowerCase(),
                    EndPointPath.REMOVE_ROLE.getEndPoint());
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/admin/user/{userEmail}")
    public ResponseEntity<?> getEmployeePayrollInfo(@PathVariable String userEmail,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        log.info("/api/admin/user/{userEmail}");
        log.info(userDetails.getUsername());
        loginAttemptService.loginSucceeded(userDetails.getUsername());
        Object result = userService.deleteUser(userEmail);
        eventsService.saveEvent(DELETE_USER,
                userDetails.getUsername(),
                userEmail,
                EndPointPath.DELETE_USER.getEndPoint());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/admin/user/")
    public ResponseEntity<?> getAllUsers(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("/api/admin/user");
        loginAttemptService.loginSucceeded(userDetails.getUsername());
        Object result = userService.getAllUser();
        log.info(result.toString());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/api/admin/user/access")
    public ResponseEntity<?> lockOrUnlockUser(@Valid @RequestBody LockUnlockUserDTO lockUnlockUserDTO,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        log.info("/api/admin/user/access");
        loginAttemptService.loginSucceeded(userDetails.getUsername());
        Object result = lockUnlockService.lockAndUnlockUser(lockUnlockUserDTO);
        if(LOCK.equals(lockUnlockUserDTO.getOperation())){
            eventsService.saveEvent(LOCK_USER,
                    userDetails.getUsername(),
                    "Lock user " + userDetails.getUsername(),
                    EndPointPath.LOCK_USER.getEndPoint());
        }else{
            eventsService.saveEvent(UNLOCK_USER,
                    userDetails.getUsername(),
                    "Unlock user " + userDetails.getUsername(),
                    EndPointPath.UNLOCK_USER.getEndPoint());
        }
        return ResponseEntity.ok(result);
    }

}
