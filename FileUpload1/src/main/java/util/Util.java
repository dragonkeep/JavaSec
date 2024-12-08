package util;

import sun.misc.BASE64Decoder;

import javax.xml.bind.DatatypeConverter;
import java.io.*;

public class Util {
    public static void  serialize(Object o)throws Exception{
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        ObjectOutputStream outputStream=new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(o);
        outputStream.close();
        byte [] bytes=byteArrayOutputStream.toByteArray();
        String base64encode= DatatypeConverter.printBase64Binary(bytes);
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter("Ser.bin"));
        bufferedWriter.write(base64encode);
        bufferedWriter.close();
    }
    public static void unserialize()throws Exception{
        BASE64Decoder decoder=new BASE64Decoder();
        StringBuilder stringBuilder=new StringBuilder();
        BufferedReader bufferedReader=new BufferedReader(new FileReader("Ser.bin"));
        String line;
        while ((line=bufferedReader.readLine())!=null){
            stringBuilder.append(line);
        }
        System.out.println(stringBuilder.toString());
        byte[] decode= decoder.decodeBuffer(stringBuilder.toString());
        ByteArrayInputStream  byteArrayInputStream=new ByteArrayInputStream(decode);
        ObjectInputStream objectInputStream =new ObjectInputStream(byteArrayInputStream);
        Object o=objectInputStream.readObject();
    }
}
