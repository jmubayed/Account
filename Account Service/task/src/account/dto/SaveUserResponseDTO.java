package account.dto;

import lombok.Data;

import java.util.List;
@Data
public class SaveUserResponseDTO {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private List<String> roles;

    public SaveUserResponseDTO(Long id, String name, String lastName, String email, List<String> roles) {
        this.id = id;
        this.name = name;
        this.lastname = lastName;
        this.email = email;
        this.roles = roles;
    }
}
