package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.MenuItem;
import util.DBUtil;

public class MenuDao {

    public List<MenuItem> findAllMenus() {

        List<MenuItem> list = new ArrayList<>();

        String sql = "SELECT code, title, is_menu, func_class_name FROM sys_menu ORDER BY code";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
                conn = DBUtil.getConnection();
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

            while (rs.next()) {

                MenuItem item = new MenuItem();

                item.setCode(rs.getInt("code"));
                item.setTitle(rs.getString("title"));
                item.setIsMenu(rs.getBoolean("is_menu"));
                item.setFuncClassName(rs.getString("func_class_name"));

                list.add(item);
                // 调试输出
                // System.out.println("load: " + item.getCode() + " " + item.getTitle());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        // 调试输出总数
        System.out.println("DAO size = " + list.size());

        return list;
    }
}
