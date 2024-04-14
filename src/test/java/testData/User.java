package testData;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String userLogin;
    private String userPassword;
    private String displayName;
    private String role;
}
