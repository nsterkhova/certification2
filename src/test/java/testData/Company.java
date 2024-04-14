package testData;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Company {
    private Integer companyId;
    private Boolean isActive;
    private String companyName;
    private String companyDescription;

    public Company(Boolean isActive, String companyName, String companyDescription){
        this.isActive = isActive;
        this.companyName = companyName;
        this.companyDescription = companyDescription;
    }
}
