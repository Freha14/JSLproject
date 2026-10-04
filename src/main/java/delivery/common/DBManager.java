package delivery.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBManager {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "ai2gi";
    private static final String PASSWORD = "CHANGE_ME";

    private DBManager() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC 드라이버를 찾을 수 없습니다.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
