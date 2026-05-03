package dao;

import model.StockOutDetailDTO;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockOutDetailDao {

    // 插入明细
    public void insert(StockOutDetailDTO dto) {

        String sql = "INSERT INTO stock_out_detail " +
                "(stock_out_id, product_id, quantity, price, amount) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dto.getStockOutId());
            ps.setInt(2, dto.getProductId());
            ps.setInt(3, dto.getQuantity());
            ps.setDouble(4, dto.getPrice());
            ps.setDouble(5, dto.getAmount());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据出库单ID查询明细
    public List<StockOutDetailDTO> findByStockOutId(int stockOutId) {

        List<StockOutDetailDTO> list = new ArrayList<>();

        String sql = "SELECT d.id, d.stock_out_id, d.product_id, d.quantity, d.price, d.amount, " +
                "p.code " +
                "FROM stock_out_detail d " +
                "LEFT JOIN product p ON d.product_id = p.id " +
                "WHERE d.stock_out_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, stockOutId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StockOutDetailDTO dto = new StockOutDetailDTO();
                    dto.setId(rs.getInt("id"));
                    dto.setStockOutId(rs.getInt("stock_out_id"));
                    dto.setProductId(rs.getInt("product_id"));
                    dto.setQuantity(rs.getInt("quantity"));
                    dto.setPrice(rs.getDouble("price"));
                    dto.setAmount(rs.getDouble("amount"));
                    dto.setProductCode(rs.getString("code"));
                    list.add(dto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 根据出库单ID删除明细
    public boolean deleteByStockOutId(int stockOutId) {

        String sql = "DELETE FROM stock_out_detail WHERE stock_out_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, stockOutId);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
