package authControllerModel;

import com.alibaba.fastjson.JSON;
import io.restassured.http.ContentType;
import utils.PropertiesManager;
import testData.User;

import static io.restassured.RestAssured.given;

public class AuthService {
    public static final String baseUrl = PropertiesManager.getPropertyValue("base.url") + "/auth/login";

    public static String getToken(User user){
        String requestBody = JSON.toJSONString(new AuthRequest(user));
        return given()
                .log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .when().post(baseUrl)
                .then().log().all()
                .extract().path("userToken");
    }
}
