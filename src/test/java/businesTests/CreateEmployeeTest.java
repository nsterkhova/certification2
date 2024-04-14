package businesTests;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testData.CommonTestData;
import testData.Company;
import testData.Employee;
import utils.DBconnect;
import utils.GetTokenHelper;
import utils.PropertiesManager;

import static employeeControllerModel.EmployeeService.createEmployeeResponse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateEmployeeTest {
    public static final String baseUri = PropertiesManager.getPropertyValue("base.url") + "/employee";

    @Test
    @DisplayName("Создание сотрудника пользователем с правами администратора")
    public void createEmployeeByAdminTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        String token = GetTokenHelper.generateTokenForAdmin();
        Employee employeeToCreate = CommonTestData.NEW_EMPLOYEE;
        employeeToCreate.setCompanyId(companyForTest.getCompanyId());
        String requestBody = JSON.toJSONString(employeeToCreate);

        try {
            int id = createEmployeeResponse(token, requestBody, baseUri)
                    .then().log().all()
                    .extract().path("id");
            Employee employeeFromDB = DBconnect.selectEmployeeFromBD(id);
            assertTrue(employeeToCreate.equalsForNewEmployee(employeeFromDB));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }

    @Test
    @DisplayName("Создание сотрудника пользователем с правами клиента")
    public void createEmployeeByClientTest() {
        Company companyForTest = DBconnect.createCompany(CommonTestData.NEW_COMPANY);
        String token = GetTokenHelper.generateTokenForClient();
        Employee employeeToCreate = CommonTestData.NEW_EMPLOYEE;
        employeeToCreate.setCompanyId(companyForTest.getCompanyId());
        String requestBody = JSON.toJSONString(employeeToCreate);

        try {
            int id = createEmployeeResponse(token, requestBody, baseUri)
                    .then().log().all()
                    .extract().path("id");
            Employee employeeFromDB = DBconnect.selectEmployeeFromBD(id);
            assertTrue(employeeToCreate.equalsForNewEmployee(employeeFromDB));
        } finally {
            DBconnect.deleteAllEmployees(companyForTest);
            DBconnect.deleteCompany(companyForTest);
        }
    }
}
