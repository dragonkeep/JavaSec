package util;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import sun.misc.BASE64Decoder;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @Description
 * @Author dragonkeep
 * @Date 2025/1/10
 */
public class Util {
    public static void  serializeHessian(Object o)throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
        hessian2Output.writeObject(o);
        hessian2Output.close();
        byte [] bytes=byteArrayOutputStream.toByteArray();
        String base64encode= DatatypeConverter.printBase64Binary(bytes);
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter("Ser2.bin"));
        bufferedWriter.write(base64encode);
        bufferedWriter.close();
    }
    public static void unserializeHessian()throws Exception{
        BASE64Decoder decoder=new BASE64Decoder();
        StringBuilder stringBuilder=new StringBuilder();
        BufferedReader bufferedReader=new BufferedReader(new FileReader("Ser2.bin"));
        String line;
        while ((line=bufferedReader.readLine())!=null){
            stringBuilder.append(line);
        }
        byte[] decode= decoder.decodeBuffer(stringBuilder.toString());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
        Hessian2Input hessian2Input=new Hessian2Input(byteArrayInputStream);
        Object o = hessian2Input.readObject();
    }
    public static HashMap<Object, Object> makeMap (Object v1, Object v2 ) throws Exception {
        HashMap<Object, Object> s = new HashMap<>();
        setValue(s, "size", 2);
        Class<?> nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        setValue(s, "table", tbl);
        return s;
    }
    public static void setValue(Object obj, String name, Object value) throws Exception{
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static Object getValue(Object obj, String name) throws Exception{
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(obj);
    }
}
