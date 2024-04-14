package testData;

public class CommonTestData {
    /**
     * Активный сотрудник
     */
    public static final Employee NEW_EMPLOYEE = new Employee(true, "Тест", "Тестовый", "Тестович", "88005553535", "test@test.com", "1900-01-01", "https://test.com");

    /**
     * Неактивный сотрудник
     */
    public static final Employee NOT_ACTIVE_EMPLOYEE = new Employee(false, "Test", "Employee", "Notactive", "+79001234567", "test@gmail.com", "2000-01-01", "https://test-avatar.com");

    /**
     * Компания для тестирования
     */
    public static final Company NEW_COMPANY = new Company (true, "NEW_EMPLOYEE_COMPANY", "COMPANY FOR TESTS");

    /**
     * Не существующая в БД компания для тестирования
     */
    public static final Company NOT_EXIST_COMPANY = new Company (-1,true, "NOT_EXIST_COMPANY", "COMPANY FOR TESTS");
}
