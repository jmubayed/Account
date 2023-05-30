package account.dto;

import lombok.Data;

@Data
public class UpdatePayrollResponseDTO {

    private String status;

    public UpdatePayrollResponseDTO(String message) {
        this.status = message;
    }
}
