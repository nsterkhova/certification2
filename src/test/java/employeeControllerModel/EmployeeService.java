package employeeControllerModel;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import testData.Company;

import static io.restassured.RestAssured.given;

public class EmployeeService {
    public static Response getEmployeeListResponse(Company company, String baseUri) {
        return given()
                .log().all()
                .baseUri(baseUri)
                .queryParam("company", company.getCompanyId())
                .get()
                .thenReturn();
    }

    public static Response createEmployeeResponse(String token, String requestBody, String baseUri) {
        return given().header("x-client-token", token)
                .log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .post(baseUri)
                .thenReturn();
    }

    public static Response getEmployeeResponse(int id, String baseUri){
        return given()
                .log().all()
                .get(baseUri + id)
                .thenReturn();
    }

    public static Response patchEmployeeResponse(String token, int id, String requestBody, String baseUri) {
        return given().header("x-client-token", token)
                .log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .baseUri(baseUri)
                .patch(baseUri + id)
                .thenReturn();
    }
}
