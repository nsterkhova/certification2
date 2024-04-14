package businesTests;

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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetEmployeeByIdTest {
    public static final String baseUri = PropertiesManager.getPropertyValue("base.url") + "/employee/";

    @Test
    @DisplayName("Получение сотрудника по id")
    public void getEmployeeTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);

        try {
            Employee employee = getEmployeeResponse(employeeForTest.getId(), baseUri)
                    .then().log().all()
                    .body(is(notNullValue()))
                    .extract().as(Employee.class);
            assertEquals(employeeForTest, employee);
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }
}
