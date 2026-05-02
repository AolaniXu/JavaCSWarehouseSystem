package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Product;
import util.DBUtil;

public class ProductDao {

    public Integer findIdByCode(String code) {

        String sql = "SELECT id FROM product WHERE code = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, code);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        return null;
    }

    public List<Product> findAll() {

        List<Product> list = new ArrayList<>();

        String sql = "SELECT id, code, name, spec, unit FROM product ORDER BY code";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setCode(rs.getString("code"));
                p.setName(rs.getString("name"));
                p.setSpec(rs.getString("spec"));
                p.setUnit(rs.getString("unit"));

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        return list;
    }
}
