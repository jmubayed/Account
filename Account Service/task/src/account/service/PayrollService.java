package account.service;

import account.dto.EmployeePaymentResponseDTO;
import account.dto.EmptyJsonResponse;
import account.dto.UpdatePayrollResponseDTO;
import account.model.Payroll;
import account.model.User;
import account.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private UserService userService;

    public UpdatePayrollResponseDTO savePayrollList(List<Payroll> payrollList){
        for(Payroll payroll : payrollList){
            payrollRepository.save(payroll);
        }
        return new UpdatePayrollResponseDTO("Added successfully!");
    }

    public UpdatePayrollResponseDTO updateUserSalary(Payroll payroll){
        Optional<Payroll> record = payrollRepository
                .findByEmployeeAndPeriod(payroll.getEmployee(), payroll.getPeriod());
        if(record.isPresent()){
            record.get().setSalary(payroll.getSalary());
            payrollRepository.save(record.get());
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new UpdatePayrollResponseDTO("Updated successfully!");
    }


    public List<String> errorMessages (BindingResult bindingResult) {
        List<String> errorMessage = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessage.add("payments"
                        .concat(Objects.requireNonNull(error.getCodes())[0]
                                .split("list")[1]).concat(": ")
                        .concat(Objects.requireNonNull(error.getDefaultMessage())));
            }
            Collections.sort(errorMessage);
        }
        return errorMessage;
    }

    public Object getEmployeePaymentInfo(String parameter, String email) {
        checkPeriod(parameter);
        User user = userService.getUser(email);
        return (parameter != null) ?
                getEmployeeInfoByPeriod(user, email, parameter)
                : getAllEmployeeInfo(user, email);
    }
    private String convertToMonthYearFormat(String dateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMMM-yyyy", Locale.ENGLISH);
        LocalDate date = YearMonth.parse(dateStr, inputFormatter).atDay(1);
        return outputFormatter.format(date);
    }

    private String convertToDollarCentsFormat(long amount) {
        long dollars = amount / 100;
        long cents = amount % 100;
        String dollarString = dollars + " dollar" + (dollars != 1 ? "(s)" : "");
        String centString = cents + " cent" + (cents != 1 ? "(s)" : "");
        StringBuilder result = new StringBuilder(dollarString);
        if (cents > 0) {
            result.append(" ").append(centString);
        }
        return result.toString();
    }

    private Object getEmployeeInfoByPeriod(User user, String email, String parameter) {
        Optional<Payroll> record = payrollRepository.findByEmployeeAndPeriod(email, parameter);
        if (record.isPresent()) {
            Payroll payroll = record.get();
            return new EmployeePaymentResponseDTO(
                    user.getName(),
                    user.getLastName(),
                    convertToMonthYearFormat(payroll.getPeriod()),
                    convertToDollarCentsFormat(payroll.getSalary())
            );
        }
        return new EmptyJsonResponse();
    }

    private Object getAllEmployeeInfo(User user, String email){
        Optional<List<Payroll>> record = payrollRepository.findAllByEmployeeIgnoreCaseOrderByPeriodDesc(email);
        if (record.isPresent()) {
            List<EmployeePaymentResponseDTO> result = new ArrayList<>();
            for (Payroll payroll : record.get()) {
                result.add(
                        new EmployeePaymentResponseDTO(
                                user.getName(),
                                user.getLastName(),
                                convertToMonthYearFormat(payroll.getPeriod()),
                                convertToDollarCentsFormat(payroll.getSalary())
                        ));
            }
            return result;
        }
        return List.of(new EmptyJsonResponse());
    }

    private void checkPeriod(String parameter){
        if(parameter != null && !parameter.matches("^(0[1-9]|1[0-2])(?!00)-\\d{4}$")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong date!");
        }
    }
}