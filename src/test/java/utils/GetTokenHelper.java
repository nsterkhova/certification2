package utils;

import authControllerModel.AuthService;
import testData.CommonUsers;

public class GetTokenHelper {
    public static String generateTokenForAdmin() {
        if (!DBconnect.isUserExist(CommonUsers.ADMIN)) {
            DBconnect.createUser(CommonUsers.ADMIN);
        }
        return AuthService.getToken(CommonUsers.ADMIN);
    }

    public static String generateTokenForClient() {
        if (!DBconnect.isUserExist(CommonUsers.CLIENT)) {
            DBconnect.createUser(CommonUsers.CLIENT);
        }
        return AuthService.getToken(CommonUsers.CLIENT);
    }
}
