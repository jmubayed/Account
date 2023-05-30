package account.exception;

import account.model.Payroll;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

public class CheckPeriodsAreUnique extends RuntimeException{

    public static void arePeriodsUnique(List<Payroll> payrollList) {
        Map<String, List<String>> emailMap = new HashMap<>();
        for (Payroll payroll : payrollList) {
            String period = payroll.getPeriod();
            String email = payroll.getEmployee();
            if(emailMap.containsKey(email)){
                if(emailMap.get(email).contains(period)){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error!");
                }else{
                    emailMap.get(email).add(period);
                }
            }else{
                List<String> periods = new ArrayList<>();
                periods.add(period);
                emailMap.put(email, periods);
            }
        }
    }
}