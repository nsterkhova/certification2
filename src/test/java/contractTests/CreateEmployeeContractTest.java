package contractTests;

import com.alibaba.fastjson.JSON;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testData.CommonTestData;
import testData.Company;
import testData.Employee;
import utils.DBconnect;
import utils.GetTokenHelper;
import utils.PropertiesManager;

import java.util.HashMap;
import java.util.Map;

import static employeeControllerModel.EmployeeService.createEmployeeResponse;
import static io.restassured.RestAssured.given;

public class CreateEmployeeContractTest {
    public static final String baseUri = PropertiesManager.getPropertyValue("base.url") + "/employee";

    @Test
    @DisplayName("Проверка обязательности заголовка x-client-token")
    public void createEmployeeWithoutTokenTest() {
        Employee employeeToCreate = new Employee();
        String requestBody = JSON.toJSONString(employeeToCreate);
        given().log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .post(baseUri)
                .then().log().all()
                .statusCode(401)
                .body("statusCode", Matchers.is(401))
                .body("message", Matchers.is("Unauthorized"));
    }

    @Test
    @DisplayName("Проверка значения заголовка x-client-token")
    public void createEmployeeByAdminTest() {
        Employee employeeToCreate = new Employee();
        String requestBody = JSON.toJSONString(employeeToCreate);
        String token = "wrong_token";
        createEmployeeResponse(token, requestBody, baseUri)
                .then().log().all()
                .statusCode(401)
                .body("statusCode", Matchers.is(401))
                .body("message", Matchers.is("Unauthorized"));
    }

    @Test
    @DisplayName("Проверка успешного выполнения запроса на создания пользовтеля")
    public void createEmployeeSuccessfulTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        String token = GetTokenHelper.generateTokenForAdmin();
        Employee employeeToCreate = CommonTestData.NEW_EMPLOYEE;
        employeeToCreate.setCompanyId(companyForTest.getCompanyId());
        String requestBody = JSON.toJSONString(employeeToCreate);

        try {
            createEmployeeResponse(token, requestBody, baseUri)
                    .then().log().all()
                    .statusCode(201)
                    .body("id", Matchers.greaterThan(0));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Проверка успешного выполнения запроса с телом запроса из обязательных параметров")
    public void onlyRequiredParamsTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        String token = GetTokenHelper.generateTokenForAdmin();
        Map<String, Object> employeeToCreate = new HashMap<>();
        employeeToCreate.put("companyId", companyForTest.getCompanyId());
        employeeToCreate.put("firstName", "Name");
        employeeToCreate.put("lastName", "Lastname");
        employeeToCreate.put("phone", "88005553233");
        String requestBody = JSON.toJSONString(employeeToCreate);

        try {
            createEmployeeResponse(token, requestBody, baseUri)
                    .then().log().all()
                    .statusCode(201)
                    .body("id", Matchers.greaterThan(0));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }
}
