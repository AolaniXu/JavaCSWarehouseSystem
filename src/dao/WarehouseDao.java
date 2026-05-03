package dao;

import model.Warehouse;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDao {

    public List<Warehouse> findAll() {

        List<Warehouse> list = new ArrayList<>();

        String sql = "SELECT id, name, location FROM warehouse ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Warehouse w = new Warehouse();
                w.setId(rs.getInt("id"));
                w.setName(rs.getString("name"));
                w.setLocation(rs.getString("location"));
                list.add(w);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Warehouse findById(int id) {

        String sql = "SELECT id, name, location FROM warehouse WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Warehouse w = new Warehouse();
                    w.setId(rs.getInt("id"));
                    w.setName(rs.getString("name"));
                    w.setLocation(rs.getString("location"));
                    return w;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

