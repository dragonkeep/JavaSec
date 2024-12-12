import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;

public class C3P0 {
    public static void main(String[] args) throws Exception{
        DataSource dataSource = new ComboPooledDataSource();
        Connection conn = dataSource.getConnection();
    }
}
