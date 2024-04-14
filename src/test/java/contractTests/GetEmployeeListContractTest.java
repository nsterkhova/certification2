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

import static employeeControllerModel.EmployeeService.getEmployeeListResponse;

public class GetEmployeeListContractTest {
    public static final String baseUri = PropertiesManager.getPropertyValue("base.url") + "/employee";

    @Test
    @DisplayName("Получение пустого списка сотрудников")
    public void getEmptyListOfEmployeeTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        try {
            getEmployeeListResponse(companyForTest, baseUri)
                    .then().log().all()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("", Matchers.hasSize(0));
        } finally {
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Получение списка сотрудников не существующей компании")
    public void getListOfEmployeeCompanyNotExistTest() {
        Company companyForTest = CommonTestData.NOT_EXIST_COMPANY;
        getEmployeeListResponse(companyForTest, baseUri)
                .then().log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("", Matchers.hasSize(0));
    }

    @Test
    @DisplayName("Получение списка из одного сотрудника")
    public void getListOfEmployeeTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);
        try {
            getEmployeeListResponse(companyForTest, baseUri)
                    .then().log().all()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("", Matchers.hasSize(1))
                    .body("[0].id", Matchers.is(employeeForTest.getId()))
                    .body("[0].firstName", Matchers.is(employeeForTest.getFirstName()))
                    .body("[0].lastName", Matchers.is(employeeForTest.getLastName()))
                    .body("[0].companyId", Matchers.is(employeeForTest.getCompanyId()))
                    .body("[0].isActive", Matchers.is(employeeForTest.getIsActive()));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }
}
