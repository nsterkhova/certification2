package contractTests;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testData.CommonTestData;
import testData.Company;
import testData.Employee;
import utils.DBconnect;
import utils.PropertiesManager;

import static employeeControllerModel.EmployeeService.getEmployeeResponse;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetEmployeeContractTest {
    public static final String baseUri = PropertiesManager.getPropertyValue("base.url") + "/employee/";

    @Test
    @DisplayName("Получение обязательных параметров в ответе")
    public void getEmployeeRequiredParamsTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);

        try {
            getEmployeeResponse(employeeForTest.getId(), baseUri)
                    .then().log().all()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body(is(notNullValue()))
                    .body("id", Matchers.notNullValue())
                    .body("firstName", Matchers.notNullValue())
                    .body("lastName", Matchers.notNullValue())
                    .body("companyId", Matchers.notNullValue())
                    .body("isActive", Matchers.notNullValue());
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Запрос данных несуществующего пользователя")
    public void getNotExistEmployeeTest() {
        getEmployeeResponse(-1, baseUri)
                .then().log().all()
                .statusCode(200)
                .header("Content-Length", "0");
    }
}
