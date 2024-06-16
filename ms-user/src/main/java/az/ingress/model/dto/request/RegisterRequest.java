package az.ingress.model.dto.request;

import az.ingress.annotation.ValidPassword;
import az.ingress.annotation.ValidUsername;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @ValidUsername
    private String email;

    @ValidPassword
    private String password;

    @ValidPassword
    private String repeatPassword;

    private String name;

    private String surname;
}
