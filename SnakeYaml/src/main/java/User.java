/**
 * @Description
 * @Author dragonkeep
 * @Date 2024/12/18
 */
public class User {
    String name;
    int age;
    public User(){}

    public User(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("User构造方法");
    }

    public String getName() {
        System.out.println("getName方法");
        return name;
    }

    public void setName(String name) {
        System.out.println("setName方法");
        this.name = name;
    }

    public int getAge() {
        System.out.println("setName方法");
        return age;
    }

    public void setAge(int age) {
        System.out.println("setAge方法");
        this.age = age;
    }
}
