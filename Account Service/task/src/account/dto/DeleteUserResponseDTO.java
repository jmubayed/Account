package account.dto;

import lombok.Data;
@Data
public class DeleteUserResponseDTO {

    private String user;
    private String status = "Deleted successfully!";

    public DeleteUserResponseDTO(String email) {
        this.user = email;
    }
}
