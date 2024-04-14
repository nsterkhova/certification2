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

import static employeeControllerModel.EmployeeService.patchEmployeeResponse;

public class PatchEmployeeContractTest {
    public static final String baseUri = PropertiesManager.getPropertyValue("base.url") + "/employee/";

    @Test
    @DisplayName("Проверка успешного ответа")
    public void patchEmployeeSuccessTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("lastName", "Lastname after patch");
        requestParams.put("email", "newtest@email.com");
        requestParams.put("url", "test.url");
        requestParams.put("isActive", "false");

        String requestBody = JSON.toJSONString(requestParams);
        String token = GetTokenHelper.generateTokenForAdmin();

        try {
            patchEmployeeResponse(token, employeeForTest.getId(), requestBody, baseUri)
                    .then().log().all()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("id", Matchers.is(employeeForTest.getId()))
                    .body("isActive", Matchers.is("false"))
                    .body("email", Matchers.is("newtest@email.com"))
                    .body("url", Matchers.is("test.url"));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Проверка успешного выполнения пустого запроса")
    public void patchEmployeeEmptyRequestTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);
        String requestBody = "{}";
        String token = GetTokenHelper.generateTokenForAdmin();

        try {
            patchEmployeeResponse(token, employeeForTest.getId(), requestBody, baseUri)
                    .then().log().all()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("id", Matchers.is(employeeForTest.getId()))
                    .body("isActive", Matchers.is(employeeForTest.getIsActive()))
                    .body("email", Matchers.is(employeeForTest.getEmail()))
                    .body("url", Matchers.is(employeeForTest.getAvatar_url()));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Проверка валидации email")
    public void validateEmailNameTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("email", "new_test@email");

        String requestBody = JSON.toJSONString(requestParams);
        String token = GetTokenHelper.generateTokenForAdmin();

        try {
            patchEmployeeResponse(token, employeeForTest.getId(), requestBody, baseUri)
                    .then().log().all()
                    .statusCode(400)
                    .contentType(ContentType.JSON)
                    .body("statusCode", Matchers.is(400))
                    .body("error", Matchers.is("Bad Request"))
                    .body("message[0]", Matchers.is("email must be an email"));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }
}
