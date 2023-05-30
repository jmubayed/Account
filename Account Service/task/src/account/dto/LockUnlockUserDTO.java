package account.dto;

import account.enumeration.Operation;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LockUnlockUserDTO {

    @NotEmpty
    private String user;
    private Operation operation;
}
