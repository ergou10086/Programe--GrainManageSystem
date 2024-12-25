package dao.impl;

import dao.IGrainChangeHistoryDAO;
import exceptions.DAOException;
import exceptions.GetHistoryExceptions;
import util.DatabaseHelper;

import javax.management.RuntimeErrorException;
import java.sql.*;
import java.util.*;

// 主要用于处理与粮食变更情况历史记录相关的数据库操作类
public class GrainChangeHistoryDAOimpl implements IGrainChangeHistoryDAO {

    // 将粮食信息的变更历史记录到grain_change_history表中，插入
    /**
     * 记录粮食信息变更历史
     * @param grainId 粮食ID
     * @param changeType 变更类型（出库/入库/售完/补充）
     * @param changeDetail 变更详情
     * @param operator 操作人
     */
    public void recordChange(String grainId, String changeType, String changeDetail, String operator) throws SQLException {
        //  插入一条记录到grain_change_history表中的sql语句
        String sql = "INSERT INTO grain_change_history (grain_id, change_type, change_detail, operator) " + "VALUES (?,?,?,?)";
        try {
            DatabaseHelper.executeUpdate(sql, grainId, changeType, changeDetail, operator);
        } catch (RuntimeErrorException e) {
            throw new DAOException.InsertFailedException("数据库插入操作失败");
        }
    }


    /**
     * 获取指定粮食的所有变更历史记录
     * @param grainId 粮食ID
     * @return 变更历史记录列表
     */
    public List<Map<String, Object>> getChangeHistory(String grainId) throws SQLException {
        String sql = "SELECT * FROM grain_change_history WHERE grain_id = ? ORDER BY change_time DESC";
        try {
            return DatabaseHelper.executeQuery(sql, grainId);
        } catch (Exception e) {
            throw new DAOException.QueryFailedException("查询变更历史失败：");
        }
    }



    /**
     * 获取指定时间段内的变更历史记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变更历史记录列表
     */
    public List<Map<String, Object>> getChangeHistoryByTimeRange(String startTime, String endTime) throws SQLException {
        // grain_change_history 和 graindata，给它们取了别名 h 和 g
        // 左连接，以graindata表的id_grain字段匹配，并对查询结果进行排序。
        String sql = "SELECT h.*, g.grain_name, g.grain_code FROM grain_change_history h " +
                "LEFT JOIN graindata g ON h.grain_id = g.id_grain " +
                "WHERE h.change_time BETWEEN ? AND ? " +
                "ORDER BY h.change_time DESC";
        try{
            return DatabaseHelper.executeQuery(sql, startTime, endTime);
        }catch (Exception e){
            throw new DAOException.QueryFailedException("查询时间段变更历史失败：" + e.getMessage());
        }
    }


    // 获取指定操作者的变更历史记录
    public List<Map<String, Object>> getChangeHistoryByOperator(String operator) throws SQLException {
        // grain_change_history表（别名h）中选择所有字段（h.*），以及从graindata表（别名g）中选择grain_name和grain_code字段
        // 左连接，以graindata表的id_grain字段匹配，并对查询结果进行排序。
        String sql = "SELECT h.*, g.grain_name, g.grain_code FROM grain_change_history h " +
                "LEFT JOIN graindata g ON h.grain_id = g.id_grain " +
                "WHERE h.operator =? " +
                "ORDER BY h.change_time DESC";
        try {
            return DatabaseHelper.executeQuery(sql, operator);
        } catch (Exception e) {
            throw new DAOException.QueryFailedException("查询操作者变更历史失败：" + e.getMessage());
        }
    }



    /**
     * 获取指定变更类型的历史记录
     * @param changeType 变更类型
     * @return 变更历史记录列表
     */
    public List<Map<String, Object>> getChangeHistoryByType(String changeType) throws SQLException {
        // grain_change_history表（别名h）中选择所有字段（h.*），以及从graindata表（别名g）中选择grain_name和grain_code字段
        String sql = "SELECT h.*, g.grain_name, grain_code FROM grain_change_history h" +
                // 左连接grain_change_history和graindata表，连接条件是grain_change_history表中的grain_id字段等于graindata表中的id_grain字段
                "LEFT JOIN graindata g ON h.grain_id = g.id_grain " +
                "WHERE h.change_type =? " +
                // 查询结果按照change_time字段降序排列，最新的变更记录排在最前面
                "ORDER BY h.change_time DESC";
        try {
            return DatabaseHelper.executeQuery(sql, changeType);
        } catch (Exception e) {
            try {
                throw new GetHistoryExceptions.GetHistoryFailedException("查询变更类型历史失败：" + e.getMessage());
            } catch (GetHistoryExceptions.GetHistoryFailedException ex) {
                throw new SQLException(ex);
            }
        }
    }



    /**
     * 统计指定时间段内各类变更的数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变更类型及其对应的数量
     */
    public Map<String, Integer> getChangeTypeStatistics(String startTime, String endTime) throws SQLException {
        // 指定了按照change_type字段进行分组,分别统计数量
        String sql = "SELECT change_type, COUNT(*) AS count FROM grain_change_history " +
                "WHERE change_time BETWEEN? AND? " + "GROUP BY change_type";
        try{
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql, startTime, endTime);
            Map<String, Integer> statistics = new HashMap<>();
            for(Map<String, Object> row : results){
                statistics.put((String) row.get("change_type"), ((Number)row.get("count")).intValue());
            }
            return statistics;
        }catch (Exception e){
            try {
                throw new GetHistoryExceptions.CountHistoryFailedException("统计变更类型失败：" + e.getMessage());
            } catch (GetHistoryExceptions.CountHistoryFailedException ex) {
                throw new SQLException(ex);
            }
        }
    }




    /**
     * 危险操作，删除指定时间的历史记录，不是管理员不能用
     * @param changeTime 指定时间
     * @return 删除的记录数
     */
    @Override
    public int deleteOldRecords(String changeTime) throws SQLException {
        String sql = "DELETE FROM grain_change_history WHERE DATE_FORMAT(change_time, '%Y-%m-%d %H:%i:%s') = ?";
        try{
            System.out.println("Attempting to delete record with time: " + changeTime);
            int result = DatabaseHelper.executeUpdate(sql, changeTime);
            if (result == 0) {
                throw new SQLException("未找到要删除的记录");
            }
            return result;
        }catch (Exception e) {
            try {
                throw new GetHistoryExceptions.DeleteHistoryFailedException("删除历史记录失败：" + e.getMessage());
            } catch (GetHistoryExceptions.DeleteHistoryFailedException ex) {
                throw new SQLException(ex);
            }
        }
    }

}
