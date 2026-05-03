package dao;

import util.DBUtil;
import java.sql.*;

public class InventoryDao {

    // 增加库存（不存在则插入，初始为0再累加）
    public void increase(int productId, int quantity) {
        String sql = "INSERT INTO inventory (product_id, warehouse_id, quantity) " +
                "VALUES (?, 1, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, quantity);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 减少库存（先检查是否足够）
    public boolean decrease(int productId, int quantity) {
        // 先检查库存是否足够
        int current = getQuantity(productId);
        if (current < quantity) {
            return false;  // 库存不足
        }

        String sql = "UPDATE inventory SET quantity = quantity - ? " +
                "WHERE product_id = ? AND warehouse_id = 1";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 查询库存
    public int getQuantity(int productId) {
        String sql = "SELECT quantity FROM inventory " +
                "WHERE product_id = ? AND warehouse_id = 1";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
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
}
