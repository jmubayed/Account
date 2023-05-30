package account.model;

import account.validation.ValidPeriod;
import account.validation.ValidSalary;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity(name = "Payroll")
@Table(name = "payroll")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String employee;

    @NotEmpty
    @ValidPeriod
    private String period;

    @NotNull
    @ValidSalary
    private Long salary;
}