package dao;

import model.StockOutDTO;
import model.StockOutView;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockOutDao {

    // 插入主表，返回自增 ID
    public int insert(StockOutDTO dto) {

        String sql = "INSERT INTO stock_out " +
                "(warehouse_id, create_time, invoice_no, customer, operator, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, dto.getWarehouseId());
            ps.setTimestamp(2, new Timestamp(dto.getCreateTime().getTime()));
            ps.setString(3, dto.getInvoiceNo());
            ps.setString(4, dto.getCustomer());
            ps.setString(5, dto.getOperator());
            ps.setInt(6, dto.getStatus());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("主表插入失败");
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // 查询所有记录（表格用）
    public List<StockOutView> findAllForTable() {

        List<StockOutView> list = new ArrayList<>();

        String sql = "SELECT id, warehouse_id, create_time, invoice_no, " +
                "customer, operator, status, biz_time " +
                "FROM stock_out " +
                "ORDER BY id DESC";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                StockOutView v = new StockOutView();
                v.setId(rs.getInt("id"));
                v.setWarehouseId(rs.getInt("warehouse_id"));
                v.setCreateTime(rs.getString("create_time"));
                v.setInvoiceNo(rs.getString("invoice_no"));
                v.setCustomer(rs.getString("customer"));
                v.setOperator(rs.getString("operator"));
                v.setStatus(rs.getInt("status"));
                v.setBizTime(rs.getString("biz_time"));
                list.add(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 根据 ID 查询
    public StockOutView findById(int id) {

        String sql = "SELECT id, warehouse_id, create_time, invoice_no, " +
                "customer, operator, status, biz_time " +
                "FROM stock_out WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StockOutView v = new StockOutView();
                    v.setId(rs.getInt("id"));
                    v.setWarehouseId(rs.getInt("warehouse_id"));
                    v.setCreateTime(rs.getString("create_time"));
                    v.setInvoiceNo(rs.getString("invoice_no"));
                    v.setCustomer(rs.getString("customer"));
                    v.setOperator(rs.getString("operator"));
                    v.setStatus(rs.getInt("status"));
                    v.setBizTime(rs.getString("biz_time"));
                    return v;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 更新主表
    public boolean update(StockOutDTO dto) {

        String sql = "UPDATE stock_out SET " +
                "invoice_no = ?, customer = ?, operator = ?, status = ? " +
                "WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dto.getInvoiceNo());
            ps.setString(2, dto.getCustomer());
            ps.setString(3, dto.getOperator());
            ps.setInt(4, dto.getStatus());
            ps.setInt(5, dto.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 删除主表
    public boolean delete(int id) {

        String sql = "DELETE FROM stock_out WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 获取所有 ID
    public List<Integer> findAllIds() {

        List<Integer> ids = new ArrayList<>();

        String sql = "SELECT id FROM stock_out ORDER BY id DESC";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ids;
    }

    // 更新状态
    public boolean updateStatus(int id, int status) {

        String sql = "UPDATE stock_out SET status = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}