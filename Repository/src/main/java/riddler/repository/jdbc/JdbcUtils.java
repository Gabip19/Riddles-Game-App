package riddler.repository.jdbc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private final Properties properties;
    private Connection connection = null;

    public JdbcUtils(Properties props) {
        properties = props;
    }

    private Connection createConnection() {
        String url = properties.getProperty("database.url");
        String user = properties.getProperty("database.user");
        String password = properties.getProperty("database.password");
        Connection con = null;

        try {
            if (user != null && password != null) {
                con = DriverManager.getConnection(url, user, password);
            } else {
                con = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            System.out.println("CONNECTION ERROR: " + e);
        }

        return con;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed())
                connection = createConnection();
        } catch (SQLException e) {
            System.out.println("CONNECTION ERROR: " + e);
        }
        return connection;
    }
}
