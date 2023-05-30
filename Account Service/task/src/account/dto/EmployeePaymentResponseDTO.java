package account.dto;

import lombok.Data;

@Data
public class EmployeePaymentResponseDTO {

    private String name;
    private String lastname;
    private String period;
    private String salary;

    public EmployeePaymentResponseDTO(String name, String lastName, String period, String salary) {
        this.name = name;
        this.lastname = lastName;
        this.period = period;
        this.salary = salary;
    }
}
