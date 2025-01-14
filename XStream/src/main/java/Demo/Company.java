package Demo;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/10
 */
public class Company implements Serializable {
    private String companyName;
    private String companyLocation;

    public Company() {
    }

    public Company(String companyName, String companyLocation) {
        this.companyName = companyName;
        this.companyLocation = companyLocation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        System.out.println("company readObject.....");
    }
}
