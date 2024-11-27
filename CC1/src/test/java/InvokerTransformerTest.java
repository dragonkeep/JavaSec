import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class InvokerTransformerTest {
    @Test
    public void TestTransformer(){
        try {
            // 创建 InvokerTransformer，调用 exec 方法
            Transformer transformer = new InvokerTransformer(
                    "exec",                   // 方法名
                    new Class[]{String.class}, // 方法参数类型
                    new Object[]{"calc"}       // 方法参数值
            );

            // 执行 transform，传递 Runtime.getRuntime() 作为输入
            transformer.transform(Runtime.getRuntime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
