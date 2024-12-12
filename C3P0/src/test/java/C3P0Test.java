import com.mchange.v2.c3p0.ComboPooledDataSource;
import junit.framework.TestCase;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class C3P0Test extends TestCase {
    @Test
    public void testSave() throws SQLException{
        String  sql = "select * from users";
        DataSource dataSource = new ComboPooledDataSource();
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("email");
            int age = resultSet.getInt("name");

            // 5. 打印每一行的数据
            System.out.println("ID: " + id + ", email: " + name + ", name: " + age);
        }
    }

}