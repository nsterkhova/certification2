package utils;

import testData.Company;
import testData.Employee;
import testData.User;

import java.sql.*;

public class DBconnect {
    private static String connectionString = PropertiesManager.getPropertyValue("db.host");
    private static String user = PropertiesManager.getPropertyValue("db.login");
    private static String pass = PropertiesManager.getPropertyValue("db.password");
    private static final String SQL_SELECT_FROM_APP_USERS = "select * from app_users where login = ?";
    private static final String SQL_INSERT_INTO_APP_USERS = "insert into app_users(login, password, display_name, role) values (?, ?, ?, ?)";
    private static final String SQL_INSERT_INTO_COMPANY = "insert into company (is_active, name, description) values(?, ?, ?);";
    private static final String SQL_SELECT_FROM_COMPANY = "select * from company where name = ?;";
    private static final String SQL_DELETE_FROM_COMPANY = "delete from company where name = ?;";
    private static final String SQL_INSERT_INTO_EMPLOYEE = "insert into employee(is_active, first_name, last_name, middle_name, phone, email, birthdate, avatar_url, company_id) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_SELECT_FROM_EMPLOYEE = "select * from employee where first_name = ? and company_id = ?;";
    private static final String SQL_SELECT_FROM_EMPLOYEE_BY_ID = "select * from employee where id = ?;";
    private static final String SQL_DELETE_FROM_EMPLOYEE = "delete from employee where company_id = ?;";

    public static ResultSet selectFromDb(String executeQuery, String... params) {
        try (Connection connection = DriverManager.getConnection(connectionString, user, pass)) {
            PreparedStatement statement = connection.prepareStatement(executeQuery);
            if(params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i], Types.OTHER);
                }
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            System.err.println("ОШИБКА: Не удалось получить данные из БД!");
            System.out.println(e);
        }
        return null;
    }

    public static void insertOrDelete(String executeQuery, String... params) {
        try (Connection connection = DriverManager.getConnection(connectionString, user, pass)) {
            PreparedStatement statement = connection.prepareStatement(executeQuery);
            if(params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i], Types.OTHER);
                }
            }
            statement.executeUpdate();
        }catch (SQLException e) {
            System.err.println("ОШИБКА: Не удалось записать данные в БД!");
            System.out.println(e);
        }
    }

    public static boolean isUserExist(User user) {
        try (ResultSet resultSet = DBconnect.selectFromDb(SQL_SELECT_FROM_APP_USERS, user.getUserLogin())){
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e){
            System.err.println("ОШИБКА: Не удалось получить данные из БД!");
            System.out.println(e);
        }
        return false;
    }

    public static void createUser(User user){
        DBconnect.insertOrDelete(SQL_INSERT_INTO_APP_USERS, user.getUserLogin(), user.getUserPassword(), user.getDisplayName(), user.getRole());
    }

    public static Company createCompany(Company company) {
        DBconnect.insertOrDelete(SQL_INSERT_INTO_COMPANY, String.valueOf(company.getIsActive()), company.getCompanyName(), company.getCompanyDescription());
        try (ResultSet resultSet = DBconnect.selectFromDb(SQL_SELECT_FROM_COMPANY, company.getCompanyName())) {
            assert resultSet != null;
            resultSet.next();
            company.setCompanyId(resultSet.getInt("id"));
        }catch (SQLException e){
            System.err.println("ОШИБКА: Не удалось получить id компании!");
            System.out.println(e);
        }
        return company;
    }

    public static void deleteCompany(Company company){
        DBconnect.insertOrDelete(SQL_DELETE_FROM_COMPANY, company.getCompanyName());
    }

    public static Employee createEmployee(Employee employee, Company company){
        employee.setCompanyId(company.getCompanyId());
        DBconnect.insertOrDelete(SQL_INSERT_INTO_EMPLOYEE, String.valueOf(employee.getIsActive()), employee.getFirstName(), employee.getLastName(), employee.getMiddleName(),
                employee.getPhone(), employee.getEmail(), employee.getBirthdate(), employee.getAvatar_url(), String.valueOf(employee.getCompanyId()));
        try (ResultSet resultSet = DBconnect.selectFromDb(SQL_SELECT_FROM_EMPLOYEE, employee.getFirstName(), String.valueOf(employee.getCompanyId()))) {
            assert resultSet != null;
            resultSet.next();
            employee.setId(resultSet.getInt("id"));
        }catch (SQLException e){
            System.err.println("ОШИБКА: Не удалось получить id компании!");
            System.out.println(e);
        }
        return employee;
    }

    public static void deleteAllEmployees(Company company){
        DBconnect.insertOrDelete(SQL_DELETE_FROM_EMPLOYEE, String.valueOf(company.getCompanyId()));
    }

    public static Employee selectEmployeeFromBD(int id){
        try (ResultSet resultSet = DBconnect.selectFromDb(SQL_SELECT_FROM_EMPLOYEE_BY_ID, String.valueOf(id))){
            resultSet.next();
            return new Employee(
                    resultSet.getInt("id"),
                    resultSet.getBoolean("is_active"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("middle_name"),
                    resultSet.getString("phone"),
                    resultSet.getString("email"),
                    resultSet.getString("birthdate"),
                    resultSet.getString("avatar_url"),
                    resultSet.getInt("company_id"),
                    resultSet.getString("create_timestamp"),
                    resultSet.getString("change_timestamp")
            );
        } catch (SQLException e) {
            System.err.println("ОШИБКА: Не удалось получить данные сотрудника!");
            System.out.println(e);
        }
        return null;
    }

}
