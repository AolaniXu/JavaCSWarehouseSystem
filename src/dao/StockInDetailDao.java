package dao;

import java.sql.*;

import model.StockInDetailDTO;
import util.DBUtil;

public class StockInDetailDao {

    public void insert(StockInDetailDTO dto) {

        String sql = "INSERT INTO stock_in_detail " +
                "(stock_in_id, product_id, quantity, price, amount) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setInt(1, dto.getStockInId());
            ps.setInt(2, dto.getProductId());
            ps.setInt(3, dto.getQuantity());
            ps.setDouble(4, dto.getPrice());
            ps.setDouble(5, dto.getAmount());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }
}