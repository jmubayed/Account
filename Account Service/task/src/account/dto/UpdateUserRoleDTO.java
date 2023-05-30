package account.dto;

import account.enumeration.Operation;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UpdateUserRoleDTO {

    @NotEmpty
    private String user;
    private String role;
    private Operation operation;
}
