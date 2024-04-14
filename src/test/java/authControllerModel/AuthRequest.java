package authControllerModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import testData.User;

@Data
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;

    public AuthRequest(User user){
        this.username = user.getUserLogin();
        this.password = user.getUserPassword();
    }
}
