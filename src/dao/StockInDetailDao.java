package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.StockInDetailDTO;
import util.DBUtil;

public class StockInDetailDao {

    // 插入单条明细
    public void insert(StockInDetailDTO dto) {

        String sql = "INSERT INTO stock_in_detail " +
                "(stock_in_id, product_id, quantity, price, amount) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dto.getStockInId());
            ps.setInt(2, dto.getProductId());
            ps.setInt(3, dto.getQuantity());
            ps.setDouble(4, dto.getPrice());
            ps.setDouble(5, dto.getAmount());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // 根据入库单ID查询明细列表
    public List<StockInDetailDTO> findByStockInId(int stockInId) {

        List<StockInDetailDTO> list = new ArrayList<>();

        String sql = "SELECT d.id, d.stock_in_id, d.product_id, d.quantity, d.price, d.amount, " +
                "p.code " +
                "FROM stock_in_detail d " +
                "LEFT JOIN product p ON d.product_id = p.id " +
                "WHERE d.stock_in_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, stockInId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StockInDetailDTO dto = new StockInDetailDTO();
                    dto.setId(rs.getInt("id"));
                    dto.setStockInId(rs.getInt("stock_in_id"));
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

    // 根据入库单ID删除明细
    public boolean deleteByStockInId(int stockInId) {

        String sql = "DELETE FROM stock_in_detail WHERE stock_in_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, stockInId);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}