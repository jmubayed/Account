package account.controller;

import account.dto.UpdatePayrollResponseDTO;
import account.model.Payroll;
import account.security.LoginAttemptService;
import account.service.PayrollService;
import account.service.UserService;
import account.validation.ValidList;
import account.validation.ValidPeriod;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static account.exception.CheckPeriodsAreUnique.arePeriodsUnique;

@RestController
@Slf4j
public class BusinessController {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @PostMapping("/api/acct/payments")
    public UpdatePayrollResponseDTO uploadPayrolls(@Valid @RequestBody ValidList<Payroll> payroll
            ,BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("/api/acct/payments");
        loginAttemptService.loginSucceeded(userDetails.getUsername());
        if(bindingResult.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.join(", ", payrollService.errorMessages(bindingResult)));
        }
        arePeriodsUnique(payroll);
        return payrollService.savePayrollList(payroll);
    }

    @PutMapping("/api/acct/payments")
    public UpdatePayrollResponseDTO updateUserSalary(@Valid @RequestBody Payroll payroll,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        log.info("/api/acct/payments");
        loginAttemptService.loginSucceeded(userDetails.getUsername());
        return payrollService.updateUserSalary(payroll);
    }

    @GetMapping("/api/empl/payment")
    public ResponseEntity<?> getEmployeePayrollInfo(@RequestParam(value = "period", required = false) String period,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        log.info("/api/empl/payment");
        loginAttemptService.loginSucceeded(userDetails.getUsername());
        log.info(userDetails.getUsername());
        return ResponseEntity.ok(payrollService.getEmployeePaymentInfo(period, userDetails.getUsername()));
    }
}
