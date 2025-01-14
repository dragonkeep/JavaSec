package Demo;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/10
 */
public class Person implements Serializable {
    private String name;
    private Integer age;
    private Company company;

    public Person(String name, Integer age, Company company) {
        this.name = name;
        this.age = age;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        System.out.println("person readObject....");
    }
}
