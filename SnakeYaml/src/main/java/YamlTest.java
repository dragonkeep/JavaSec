import org.yaml.snakeyaml.Yaml;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2024/12/18
 */
public class YamlTest {
    public static void Serialize(){
        User user = new User();
        user.setName("Dragonkeep");
        user.setAge(18);
        Yaml yaml = new Yaml();
        String dump = yaml.dump(user);
        System.out.println(dump);
    }

    public static void Deserialize(){
        String s = "!!User {age: 18, name: Dragonkeep}";
        Yaml yaml = new Yaml();
        User user = yaml.load(s);

    }
    public static void main(String[] args) {
        //Serialize();
        Deserialize();
    }
}
