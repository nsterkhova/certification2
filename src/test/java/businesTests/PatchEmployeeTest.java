package businesTests;

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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatchEmployeeTest {
    public static final String baseUri = PropertiesManager.getPropertyValue("base.url") + "/employee/";

    @Test
    @DisplayName("Изменение сотрудника пользователем с правами администратора")
    public void patchEmployeeByAdminTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);
        employeeForTest.setLastName("Lastname after patch");
        employeeForTest.setIsActive(false);
        employeeForTest.setEmail("test@email.com");
        employeeForTest.setAvatar_url("test.url");

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("lastName", "Lastname after patch");
        requestParams.put("email", "test@email.com");
        requestParams.put("url", "test.url");
        requestParams.put("isActive", "false");

        String requestBody = JSON.toJSONString(requestParams);
        String token = GetTokenHelper.generateTokenForAdmin();

        try {
            patchEmployeeResponse(token, employeeForTest.getId(), requestBody, baseUri)
                    .then().log().all();
            Employee employeeFromDB = DBconnect.selectEmployeeFromBD(employeeForTest.getId());
            assertEquals(employeeForTest, employeeFromDB);
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Изменение сотрудника пользователем с правами клиента")
    public void patchEmployeeByClientTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NOT_ACTIVE_EMPLOYEE, companyForTest);
        employeeForTest.setLastName("New lastname");
        employeeForTest.setIsActive(true);
        employeeForTest.setEmail("new@email.com");
        employeeForTest.setAvatar_url("new.url");

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("lastName", "New lastname");
        requestParams.put("email", "new@email.com");
        requestParams.put("url", "new.url");
        requestParams.put("isActive", "true");

        String requestBody = JSON.toJSONString(requestParams);
        String token = GetTokenHelper.generateTokenForClient();

        try {
            patchEmployeeResponse(token, employeeForTest.getId(), requestBody, baseUri)
                    .then().log().all();
            Employee employeeFromDB = DBconnect.selectEmployeeFromBD(employeeForTest.getId());
            assertEquals(employeeForTest, employeeFromDB);
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Проверка изменения только email")
    public void changeEmailNameTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("email", "new_test@email.com");

        String requestBody = JSON.toJSONString(requestParams);
        String token = GetTokenHelper.generateTokenForAdmin();

        try {
            patchEmployeeResponse(token, employeeForTest.getId(), requestBody, baseUri)
                    .then().log().all()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("id", Matchers.is(employeeForTest.getId()))
                    .body("isActive", Matchers.is(employeeForTest.getIsActive()))
                    .body("email", Matchers.is("new_test@email.com"))
                    .body("url", Matchers.is(employeeForTest.getAvatar_url()));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Проверка изменения только фамилии")
    public void changeLastNameTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("lastName", "Lastname after patch");

        String requestBody = JSON.toJSONString(requestParams);
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
            Employee employeeFromDB = DBconnect.selectEmployeeFromBD(employeeForTest.getId());
            assertEquals(employeeFromDB.getLastName(), "Lastname after patch");
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Проверка изменения только isActive")
    public void changeIsActiveTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);

        Map<String, String> requestParams = new HashMap<>();
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
                    .body("email", Matchers.is(employeeForTest.getEmail()))
                    .body("url", Matchers.is(employeeForTest.getAvatar_url()));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Проверка изменения только url")
    public void changeUrlTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("url", "new.url");

        String requestBody = JSON.toJSONString(requestParams);
        String token = GetTokenHelper.generateTokenForAdmin();

        try {
            patchEmployeeResponse(token, employeeForTest.getId(), requestBody, baseUri)
                    .then().log().all()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("id", Matchers.is(employeeForTest.getId()))
                    .body("isActive", Matchers.is(employeeForTest.getIsActive()))
                    .body("email", Matchers.is(employeeForTest.getEmail()))
                    .body("url", Matchers.is("new.url"));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }
}
