/**
 * @Description
 * @Author dragonkeep
 * @Date 2024/12/22
 */
public class User {
    private String name;
    private int age;

    public User(String name,int age){
        this.name=name;
        this.age=age;
    }
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String greet() {
        return "Hello, " + name;
    }
}
