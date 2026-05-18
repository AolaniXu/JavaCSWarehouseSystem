package dao;

import model.Product;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    public List<Product> findAll() {

        List<Product> list = new ArrayList<>();

        String sql = "SELECT id, code, name, spec, unit FROM product ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

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
        }

        return list;
    }

    public Product findById(int id) {

        String sql = "SELECT id, code, name, spec, unit FROM product WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setCode(rs.getString("code"));
                    p.setName(rs.getString("name"));
                    p.setSpec(rs.getString("spec"));
                    p.setUnit(rs.getString("unit"));
                    return p;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int findIdByCode(String code) {

        String sql = "SELECT id FROM product WHERE code = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}
