package util;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LogUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 记录系统日志
     * @param username 操作用户
     * @param operation 操作类型
     * @param detail 操作详情
     */
    public static void recordLog(String username, String operation, String detail) {
        String sql = "INSERT INTO system_logs (username, operation, detail, log_time) VALUES (?, ?, ?, ?)";
        try {
            DatabaseHelper.executeUpdate(sql, 
                username,
                operation,
                detail,
                dateFormat.format(new Date())
            );
        } catch (SQLException e) {
            System.err.println("记录日志失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取指定时间范围内的日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日志列表
     */
    public static List<Map<String, Object>> getLogsByTimeRange(String startTime, String endTime) throws SQLException {
        String sql = "SELECT * FROM system_logs WHERE log_time BETWEEN ? AND ? ORDER BY log_time DESC";
        return DatabaseHelper.executeQuery(sql, startTime, endTime);
    }

    /**
     * 获取指定用户的日志
     * @param username 用户名
     * @return 日志列表
     */
    public static List<Map<String, Object>> getLogsByUser(String username) throws SQLException {
        String sql = "SELECT * FROM system_logs WHERE username = ? ORDER BY log_time DESC";
        return DatabaseHelper.executeQuery(sql, username);
    }

    /**
     * 获取指定操作类型的日志
     * @param operation 操作类型
     * @return 日志列表
     */
    public static List<Map<String, Object>> getLogsByOperation(String operation) throws SQLException {
        String sql = "SELECT * FROM system_logs WHERE operation = ? ORDER BY log_time DESC";
        return DatabaseHelper.executeQuery(sql, operation);
    }

    /**
     * 删除指定ID的日志
     * @param logId 日志ID
     * @return 是否删除成功
     */
    public static boolean deleteLog(int logId) throws SQLException {
        String sql = "DELETE FROM system_logs WHERE log_id = ?";
        return DatabaseHelper.executeUpdate(sql, logId) > 0;
    }

    /**
     * 更新日志表格显示
     */
    public static void updateLogTable(DefaultTableModel model) throws SQLException {
        String sql = "SELECT log_id, log_time, username, operation, detail FROM system_logs ORDER BY log_time DESC";
        List<Map<String, Object>> logs = DatabaseHelper.executeQuery(sql);

        model.setRowCount(0);
        for (Map<String, Object> log : logs) {
            model.addRow(new Object[]{
                    log.get("log_time"),
                    log.get("username"),
                    log.get("operation"),
                    log.get("detail")
            });
        }
    }




    /**
     * 获取日志ID
     */
    public static int getLogId(int row, DefaultTableModel model) {
        Object logIdObj = model.getValueAt(row, 0); // 假设log_id在第一列
        if (logIdObj instanceof Number) {
            return ((Number) logIdObj).intValue();
        }
        throw new IllegalArgumentException("无效的日志ID");
    }

} 