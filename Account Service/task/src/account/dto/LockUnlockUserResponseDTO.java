package account.dto;

import account.enumeration.Operation;
import lombok.Data;
@Data
public class LockUnlockUserResponseDTO {

    private String status;

    public LockUnlockUserResponseDTO(String username, Operation operation) {
        this.status = "User " + username + " " + operation.getName();
    }
}
