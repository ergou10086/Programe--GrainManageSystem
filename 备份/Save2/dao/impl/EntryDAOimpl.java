package dao.impl;

import dao.EntryDAO;
import util.DBUtil;

import java.sql.*;
import java.util.Map;

public class EntryDAOimpl implements EntryDAO {

    @Override
    public boolean addEntryRecord(Map<String, Object> entryData) throws SQLException {
        String sql = "INSERT INTO entry_data VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, (String) entryData.get("id"));
            ps.setString(2, (String) entryData.get("cropName"));
            ps.setString(3, (String) entryData.get("warehouse"));
            ps.setString(4, (String) entryData.get("grossWeight"));
            ps.setString(5, (String) entryData.get("tareWeight"));
            ps.setString(6, (String) entryData.get("netWeight"));
            ps.setString(7, (String) entryData.get("moisture"));
            ps.setString(8, (String) entryData.get("driverName"));
            ps.setString(9, (String) entryData.get("phone"));
            ps.setString(10, (String) entryData.get("plateNumber"));
            ps.setString(11, (String) entryData.get("entryDate"));

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateWarehouseStock(String warehouseName, String cropName, double weight) throws SQLException {
        String sql = "UPDATE warehouse SET cropname = ?, cropweight = ? WHERE warehouse_name = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cropName);
            ps.setDouble(2, weight);
            ps.setString(3, warehouseName);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateWarehouseStockTwo(String warehouseName, String cropName, double weight) throws SQLException {
        String sql = "UPDATE warehouse SET cropweight = cropweight + ? WHERE warehouse_name = ? AND cropname = ?";

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