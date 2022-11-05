package ewm.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private int id;
    @Email
    private String email;
    @NotBlank
    private String name;
}
