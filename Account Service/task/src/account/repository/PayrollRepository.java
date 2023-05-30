package account.repository;

import account.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    Optional<Payroll> findByEmployeeAndPeriod(String employee, String period);
    Optional<List<Payroll>> findAllByEmployeeIgnoreCaseOrderByPeriodDesc(String employee);

}
