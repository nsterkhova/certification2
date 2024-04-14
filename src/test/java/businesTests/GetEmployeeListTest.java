package businesTests;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testData.CommonTestData;
import testData.Company;
import testData.Employee;
import utils.DBconnect;
import utils.PropertiesManager;

import java.util.List;

import static employeeControllerModel.EmployeeService.getEmployeeListResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetEmployeeListTest {
    public static final String baseUri = PropertiesManager.getPropertyValue("base.url") + "/employee";

    @Test
    @DisplayName("Получение списка из одного сотрудника")
    public void getOneItemListOfEmployeeTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee employeeForTest = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);

        try {
            List<Employee> employee = getEmployeeListResponse(companyForTest, baseUri)
                    .then().log().all()
                    .extract().jsonPath()
                    .getList("$", Employee.class);
            assertEquals(employeeForTest, employee.get(0));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Получение списка из нескольких сотрудников")
    public void getListOfEmployeeTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        Employee activeEmployee = DBconnect.createEmployee(CommonTestData.NEW_EMPLOYEE, companyForTest);
        Employee notActiveEmployee = DBconnect.createEmployee(CommonTestData.NOT_ACTIVE_EMPLOYEE, companyForTest);

        try {
            List<Employee> employee = getEmployeeListResponse(companyForTest, baseUri)
                    .then().log().all()
                    .body("", Matchers.hasSize(2))
                    .extract().jsonPath()
                    .getList("$", Employee.class);
            assertTrue(employee.contains(activeEmployee));
            assertTrue(employee.contains(notActiveEmployee));

        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }
}
