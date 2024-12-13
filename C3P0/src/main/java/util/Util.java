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
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter("Ser2.bin"));
        bufferedWriter.write(base64encode);
        bufferedWriter.close();
    }
    public static void unserialize()throws Exception{
        BASE64Decoder decoder=new BASE64Decoder();
        StringBuilder stringBuilder=new StringBuilder();
        BufferedReader bufferedReader=new BufferedReader(new FileReader("Ser2.bin"));
        String line;
        while ((line=bufferedReader.readLine())!=null){
            stringBuilder.append(line);
        }
        byte[] decode= decoder.decodeBuffer(stringBuilder.toString());
        ByteArrayInputStream  byteArrayInputStream=new ByteArrayInputStream(decode);
        ObjectInputStream objectInputStream =new ObjectInputStream(byteArrayInputStream);
        Object o=objectInputStream.readObject();
    }

    public static void serializeToHex(Object obj, String filePath) throws IOException {
        // 序列化对象到字节数组
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(obj);
        }
        byte[] serializedBytes = byteArrayOutputStream.toByteArray();

        // 转换字节数组为十六进制字符串
        String hexString = bytesToHex(serializedBytes);

        // 将十六进制字符串写入文件
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(hexString);
        }
    }

    // 方法2：从文件读取十六进制编码，解码后反序列化为对象
    public static Object deserializeFromHex(String filePath) throws IOException, ClassNotFoundException {
        // 从文件读取十六进制字符串
        StringBuilder hexStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                hexStringBuilder.append(line);
            }
        }
        String hexString = hexStringBuilder.toString();

        // 转换十六进制字符串为字节数组
        byte[] serializedBytes = hexToBytes(hexString);

        // 反序列化字节数组为对象
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(serializedBytes))) {
            return objectInputStream.readObject();
        }
    }

    // 工具方法：将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0'); // 补齐前导零
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // 工具方法：将十六进制字符串转换为字节数组
    private static byte[] hexToBytes(String hexString) {
        int length = hexString.length();
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Invalid hex string length.");
        }
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(hexString.substring(i, i + 2), 16);
        }
        return bytes;
    }

}
