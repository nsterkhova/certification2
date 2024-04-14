package testData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Integer id;
    private Boolean isActive;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String email;
    private String birthdate;
    private String avatar_url;
    private Integer companyId;
    private String createDateTime;
    private String lastChangedDateTime;

    public Employee(Boolean isActive, String firstName, String lastName, String middleName, String phone, String email, String birthdate, String avatar_url) {
        this.isActive = isActive;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phone = phone;
        this.email = email;
        this.birthdate = birthdate;
        this.avatar_url = avatar_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id)
                && Objects.equals(companyId, employee.companyId)
                && Objects.equals(isActive, employee.isActive)
                && Objects.equals(firstName, employee.firstName)
                && Objects.equals(lastName, employee.lastName)
                && Objects.equals(middleName, employee.middleName)
                && Objects.equals(phone, employee.phone)
                && Objects.equals(email, employee.email)
                && Objects.equals(birthdate, employee.birthdate)
                && Objects.equals(avatar_url, employee.avatar_url);
    }

    public boolean equalsForNewEmployee(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(companyId, employee.companyId)
                && Objects.equals(isActive, employee.isActive)
                && Objects.equals(firstName, employee.firstName)
                && Objects.equals(lastName, employee.lastName)
                && Objects.equals(middleName, employee.middleName)
                && Objects.equals(phone, employee.phone)
                && Objects.equals(birthdate, employee.birthdate);
    }
}