package util;

import java.sql.*;

public class DBUtil {

    private static final String URL =
        "jdbc:mysql://localhost:3306/warehouse_system?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 关闭资源
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (Exception ignored) {}

        try {
            if (stmt != null) stmt.close();
        } catch (Exception ignored) {}

        try {
            if (conn != null) conn.close();
        } catch (Exception ignored) {}
    }
}