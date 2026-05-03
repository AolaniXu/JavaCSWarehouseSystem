package dao;

import model.StockInDTO;
import model.StockInView;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockInDao {

    public int insert(StockInDTO dto) {

        String sql = "INSERT INTO stock_in " +
                "(warehouse_id, create_time, invoice_no, supplier, operator, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            if (dto.getCreateTime() == null) {
                dto.setCreateTime(new Date());
            }

            // ⭐关键：获取自增主键
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, dto.getWarehouseId());
            ps.setTimestamp(2, new Timestamp(dto.getCreateTime().getTime()));
            ps.setString(3, dto.getInvoiceNo());
            ps.setString(4, dto.getSupplier());
            ps.setString(5, dto.getOperator());
            ps.setInt(6, dto.getStatus());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("主表插入失败");
            }

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        return -1;
    }

    public List<StockInView> findAllForTable() {

        List<StockInView> list = new ArrayList<>();

        String sql = "SELECT id, warehouse_id, create_time, invoice_no, " +
                "supplier, operator, status, biz_time " +
                "FROM stock_in " +
                "ORDER BY id DESC";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                StockInView v = new StockInView();

                v.setId(rs.getInt("id"));
                v.setWarehouseId(rs.getInt("warehouse_id"));
                v.setCreateTime(rs.getString("create_time"));
                v.setInvoiceNo(rs.getString("invoice_no"));
                v.setSupplier(rs.getString("supplier"));
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

    // 根据 ID 查询单条记录
    public StockInView findById(int id) {

        String sql = "SELECT id, warehouse_id, create_time, invoice_no, " +
                "supplier, operator, status, biz_time " +
                "FROM stock_in WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StockInView v = new StockInView();
                    v.setId(rs.getInt("id"));
                    v.setWarehouseId(rs.getInt("warehouse_id"));
                    v.setCreateTime(rs.getString("create_time"));
                    v.setInvoiceNo(rs.getString("invoice_no"));
                    v.setSupplier(rs.getString("supplier"));
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
    public boolean update(StockInDTO dto) {

        String sql = "UPDATE stock_in SET " +
                "invoice_no = ?, supplier = ?, operator = ?, status = ? " +
                "WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dto.getInvoiceNo());
            ps.setString(2, dto.getSupplier());
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

        String sql = "DELETE FROM stock_in WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 获取所有记录的 ID 列表（倒序，最新的在前）
    public List<Integer> findAllIds() {

        List<Integer> ids = new ArrayList<>();

        String sql = "SELECT id FROM stock_in ORDER BY id DESC";

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

}
