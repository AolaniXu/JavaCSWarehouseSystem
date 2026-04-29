package dao;

import java.sql.*;

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
}
