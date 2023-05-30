package account.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class NewPasswordResponseDTO {

    @NonNull
    private String email;
    private String status = "The password has been updated successfully";
}