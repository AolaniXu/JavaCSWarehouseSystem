package dao;

import model.InventoryView;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDao {

    // 增加库存（不存在则插入，初始为0再累加）
    public void increase(int productId, int warehouseId, int quantity) {
        String sql = "INSERT INTO inventory (product_id, warehouse_id, quantity) " +
                "VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, warehouseId);
            ps.setInt(3, quantity);
            ps.setInt(4, quantity);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 减少库存（先检查是否足够）
    public boolean decrease(int productId, int warehouseId, int quantity) {
        int current = getQuantity(productId, warehouseId);
        if (current < quantity) {
            return false;
        }

        String sql = "UPDATE inventory SET quantity = quantity - ? " +
                "WHERE product_id = ? AND warehouse_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, warehouseId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 查询库存
    public int getQuantity(int productId, int warehouseId) {
        String sql = "SELECT quantity FROM inventory " +
                "WHERE product_id = ? AND warehouse_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, warehouseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantity");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 查询所有库存（关联商品表和仓库表）
    public List<InventoryView> findAllForTable() {

        List<InventoryView> list = new ArrayList<>();

        String sql = "SELECT i.id, i.product_id, i.warehouse_id, i.quantity, " +
                "p.code AS product_code, p.name AS product_name, " +
                "w.name AS warehouse_name " +
                "FROM inventory i " +
                "LEFT JOIN product p ON i.product_id = p.id " +
                "LEFT JOIN warehouse w ON i.warehouse_id = w.id " +
                "ORDER BY i.id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                InventoryView v = new InventoryView();
                v.setId(rs.getInt("id"));
                v.setProductId(rs.getInt("product_id"));
                v.setWarehouseId(rs.getInt("warehouse_id"));
                v.setQuantity(rs.getInt("quantity"));
                v.setProductCode(rs.getString("product_code"));
                v.setProductName(rs.getString("product_name"));
                v.setWarehouseName(rs.getString("warehouse_name"));
                list.add(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}

