package testData;

public class CommonUsers {
    /**
     * Пользователь с правами админа для внесения изменений в базу
     */
    public static final User ADMIN = new User("testadmin", "P@ssw0rd", "ADMIN FOR TESTS", "admin");

    /**
     * Пользователь с правами админа для внесения изменений в базу
     */
    public static final User CLIENT = new User("testclient", "P@ssw0rd", "CLIENT FOR TESTS", "client");
}
