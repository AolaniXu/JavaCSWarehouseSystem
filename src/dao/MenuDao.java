package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.MenuItem;

public class MenuDao {

    // 你可以替换成自己的数据库工具类
    private static final String URL = "jdbc:mysql://localhost:3306/warehouse_system";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public List<MenuItem> findAllMenus() {

        List<MenuItem> list = new ArrayList<>();

        String sql = "SELECT code, title, is_menu, func_class_name FROM sys_menu ORDER BY code";

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                MenuItem item = new MenuItem();

                item.setCode(rs.getInt("code"));
                item.setTitle(rs.getString("title"));
                item.setIsMenu(rs.getBoolean("is_menu"));
                item.setFuncClassName(rs.getString("func_class_name"));

                list.add(item);
                // 调试输出
                System.out.println("load: " + item.getCode() + " " + item.getTitle());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 调试输出总数
        System.out.println("DAO size = " + list.size());

        return list;
    }
}
