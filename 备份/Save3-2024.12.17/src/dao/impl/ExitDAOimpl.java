package dao.impl;

import dao.ExitDAO;
import util.DBUtil;

import java.sql.*;
import java.util.Map;

public class ExitDAOimpl implements ExitDAO {

    @Override
    public boolean addExitRecord(Map<String, Object> exitData) throws SQLException {
        String sql = "INSERT INTO exit_data VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, (String) exitData.get("id"));
            ps.setString(2, (String) exitData.get("companyName"));
            ps.setString(3, (String) exitData.get("contactPerson"));
            ps.setString(4, (String) exitData.get("contactPhone"));
            ps.setString(5, (String) exitData.get("warehouse"));
            ps.setString(6, (String) exitData.get("cropName"));
            ps.setString(7, (String) exitData.get("weight"));
            ps.setString(8, (String) exitData.get("moisture"));
            ps.setString(9, (String) exitData.get("price"));
            ps.setString(10, (String) exitData.get("driverName"));
            ps.setString(11, (String) exitData.get("driverPhone"));
            ps.setString(12, (String) exitData.get("licensePlate"));
            ps.setString(13, (String) exitData.get("exitDate"));
            ps.setString(14, (String) exitData.get("totalAmount"));
            ps.setBytes(15, (byte[]) exitData.get("image"));

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateWarehouseStock(String warehouseName, String cropName, double weight) throws SQLException {
        String sql = "UPDATE warehouse SET cropweight = cropweight - ? WHERE warehouse_name = ? AND cropname = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, weight);
            ps.setString(2, warehouseName);
            ps.setString(3, cropName);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public double getWarehouseCurrentWeight(String warehouseName) throws SQLException {
        String sql = "SELECT cropweight FROM warehouse WHERE warehouse_name = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, warehouseName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("cropweight");
            }
            return 0.0;
        }
    }

    @Override
    public String getWarehouseCropName(String warehouseName) throws SQLException {
        String sql = "SELECT cropname FROM warehouse WHERE warehouse_name = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, warehouseName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("cropname");
            }
            return null;
        }
    }
}